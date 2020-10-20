//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.hud.HUD;
import com.gamesense.client.module.modules.misc.AutoGG;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class AutoCrystal extends Module {
  public static Setting.Double range;
  
  public static Setting.Double placeRange;
  
  private static boolean togglePitch = false;
  
  private static boolean isSpoofingAngles;
  
  private static double yaw;
  
  private static double pitch;
  
  private final ArrayList<BlockPos> PlacedCrystals = new ArrayList<>();
  
  @EventHandler
  private final Listener<PacketEvent.Receive> packetReceiveListener;
  
  public boolean isActive;
  
  EnumFacing f;
  
  Setting.Boolean explode;
  
  Setting.Boolean antiWeakness;
  
  Setting.Boolean place;
  
  Setting.Boolean raytrace;
  
  Setting.Boolean rotate;
  
  Setting.Boolean spoofRotations;
  
  @EventHandler
  private final Listener<PacketEvent.Send> packetSendListener;
  
  Setting.Boolean chat;
  
  Setting.Boolean showDamage;
  
  Setting.Boolean singlePlace;
  
  Setting.Boolean antiSuicide;
  
  Setting.Boolean autoSwitch;
  
  Setting.Boolean endCrystalMode;
  
  Setting.Integer placeDelay;
  
  Setting.Integer antiSuicideValue;
  
  Setting.Integer facePlace;
  
  Setting.Integer attackSpeed;
  
  Setting.Double maxSelfDmg;
  
  Setting.Double minBreakDmg;
  
  Setting.Double enemyRange;
  
  Setting.Double walls;
  
  Setting.Double minDmg;
  
  Setting.Mode handBreak;
  
  Setting.Mode breakMode;
  
  Setting.Mode hudDisplay;
  
  Setting.ColorSetting color;
  
  Setting.Boolean cancelCrystal;
  
  private BlockPos render;
  
  private Entity renderEnt;
  
  private boolean switchCooldown;
  
  private boolean isAttacking;
  
  private boolean isPlacing;
  
  private boolean isBreaking;
  
  private int oldSlot;
  
  private int newSlot;
  
  private int waitCounter;
  
  private long breakSystemTime;
  
  public AutoCrystal() {
    super("AutoCrystal", Module.Category.Combat);
    this.packetReceiveListener = new Listener(event -> {
          if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
              for (Entity e : (Minecraft.getMinecraft()).world.loadedEntityList) {
                if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D)
                  e.setDead(); 
              }  
          } 
        }new java.util.function.Predicate[0]);
    this.isActive = false;
    this.packetSendListener = new Listener(event -> {
          Packet packet = event.getPacket();
          if (packet instanceof CPacketPlayer && this.spoofRotations.getValue() && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
          } 
        }new java.util.function.Predicate[0]);
    this.switchCooldown = false;
    this.isAttacking = false;
    this.isPlacing = false;
    this.isBreaking = false;
    this.oldSlot = -1;
  }
  
  public static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
    float doubleExplosionSize = 12.0F;
    double distancedsize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
    Vec3d vec3d = new Vec3d(posX, posY, posZ);
    double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
    double v = (1.0D - distancedsize) * blockDensity;
    float damage = (int)((v * v + v) / 2.0D * 7.0D * doubleExplosionSize + 1.0D);
    double finald = 1.0D;
    if (entity instanceof EntityLivingBase)
      finald = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.world, null, posX, posY, posZ, 6.0F, false, true)); 
    return (float)finald;
  }
  
  public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
    if (entity instanceof EntityPlayer) {
      EntityPlayer ep = (EntityPlayer)entity;
      DamageSource ds = DamageSource.causeExplosionDamage(explosion);
      damage = CombatRules.getDamageAfterAbsorb(damage, ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
      int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
      float f = MathHelper.clamp(k, 0.0F, 20.0F);
      damage *= 1.0F - f / 25.0F;
      if (entity.isPotionActive(Potion.getPotionById(11)))
        damage -= damage / 4.0F; 
      damage = Math.max(damage, 0.0F);
      return damage;
    } 
    damage = CombatRules.getDamageAfterAbsorb(damage, entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    return damage;
  }
  
  private static float getDamageMultiplied(float damage) {
    int diff = mc.world.getDifficulty().getId();
    return damage * ((diff == 0) ? 0.0F : ((diff == 2) ? 1.0F : ((diff == 1) ? 0.5F : 1.5F)));
  }
  
  private static void setYawAndPitch(float yaw1, float pitch1) {
    yaw = yaw1;
    pitch = pitch1;
    isSpoofingAngles = true;
  }
  
  private static void resetRotation() {
    if (isSpoofingAngles) {
      yaw = mc.player.rotationYaw;
      pitch = mc.player.rotationPitch;
      isSpoofingAngles = false;
    } 
  }
  
  public static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
    double dirx = me.posX - px;
    double diry = me.posY - py;
    double dirz = me.posZ - pz;
    double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
    dirx /= len;
    diry /= len;
    dirz /= len;
    double pitch = Math.asin(diry);
    double yaw = Math.atan2(dirz, dirx);
    pitch = pitch * 180.0D / Math.PI;
    yaw = yaw * 180.0D / Math.PI;
    yaw += 90.0D;
    return new double[] { yaw, pitch };
  }
  
  public void setup() {
    ArrayList<String> hands = new ArrayList<>();
    hands.add("Main");
    hands.add("Offhand");
    hands.add("Both");
    ArrayList<String> breakModes = new ArrayList<>();
    breakModes.add("All");
    breakModes.add("Smart");
    breakModes.add("Only Own");
    ArrayList<String> hudModes = new ArrayList<>();
    hudModes.add("Mode");
    hudModes.add("None");
    this.explode = registerBoolean("Break", "Break", true);
    this.place = registerBoolean("Place", "Place", true);
    this.breakMode = registerMode("Break Modes", "BreakModes", breakModes, "All");
    this.handBreak = registerMode("Hand", "Hand", hands, "Main");
    this.antiSuicide = registerBoolean("Anti Suicide", "AntiSuicide", false);
    this.antiSuicideValue = registerInteger("Pause Health", "PauseHealth", 10, 0, 36);
    this.attackSpeed = registerInteger("Attack Speed", "AttackSpeed", 12, 1, 20);
    this.placeDelay = registerInteger("Place Delay", "PlaceDelay", 0, 0, 20);
    placeRange = registerDouble("Place Range", "PlaceRange", 6.0D, 0.0D, 6.0D);
    range = registerDouble("Hit Range", "HitRange", 5.0D, 0.0D, 10.0D);
    this.walls = registerDouble("Walls Range", "WallsRange", 3.5D, 0.0D, 10.0D);
    this.enemyRange = registerDouble("Enemy Range", "EnemyRange", 6.0D, 0.5D, 13.0D);
    this.antiWeakness = registerBoolean("Anti Weakness", "AntiWeakness", true);
    this.showDamage = registerBoolean("Show Damage", "ShowDamage", false);
    this.endCrystalMode = registerBoolean("1.13 Mode", "1.13Mode", false);
    this.singlePlace = registerBoolean("MultiPlace", "MultiPlace", false);
    this.autoSwitch = registerBoolean("Auto Switch", "AutoSwitch", true);
    this.minDmg = registerDouble("Min Damage", "MinDamage", 5.0D, 0.0D, 36.0D);
    this.minBreakDmg = registerDouble("Min Break Dmg", "MinBreakDmg", 10.0D, 1.0D, 36.0D);
    this.maxSelfDmg = registerDouble("Max Self Dmg", "MaxSelfDmg", 10.0D, 1.0D, 36.0D);
    this.facePlace = registerInteger("FacePlace HP", "FacePlaceHP", 8, 0, 36);
    this.raytrace = registerBoolean("Raytrace", "Raytrace", false);
    this.rotate = registerBoolean("Rotate", "Rotate", true);
    this.spoofRotations = registerBoolean("Spoof Angles", "SpoofAngles", true);
    this.cancelCrystal = registerBoolean("Cancel Crystal", "Cancel Crystal", true);
    this.chat = registerBoolean("Toggle Msg", "ToggleMsg", true);
    this.hudDisplay = registerMode("HUD", "HUD", hudModes, "Mode");
    this.color = registerColor("Color", "Color");
  }
  
  public void onUpdate() {
    this.isActive = false;
    this.isBreaking = false;
    this.isPlacing = false;
    if (mc.player == null || mc.player.isDead)
      return; 
    int crystalSlot = (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? mc.player.inventory.currentItem : -1;
    if (crystalSlot == -1)
      for (int l = 0; l < 9; l++) {
        if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
          crystalSlot = l;
          break;
        } 
      }  
    boolean offhand = false;
    if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
      offhand = true;
    } else if (crystalSlot == -1) {
      return;
    } 
    List<BlockPos> blocks = findCrystalBlocks();
    List<Entity> entities = new ArrayList<>();
    entities.addAll((Collection<? extends Entity>)mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted(Comparator.comparing(e -> Float.valueOf(mc.player.getDistance((Entity)e)))).collect(Collectors.toList()));
    BlockPos q = null;
    double damage = 0.5D;
    Iterator<Entity> var9 = entities.iterator();
    label110: while (true) {
      if (!var9.hasNext()) {
        if (damage == 0.5D) {
          this.render = null;
          this.renderEnt = null;
          resetRotation();
          return;
        } 
        this.render = q;
        if (this.place.getValue()) {
          if (this.antiSuicide.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() < this.antiSuicideValue.getValue())
            return; 
          if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
            if (this.autoSwitch.getValue()) {
              mc.player.inventory.currentItem = crystalSlot;
              resetRotation();
              this.switchCooldown = true;
            } 
            return;
          } 
          if (this.rotate.getValue())
            lookAtPacket(q.getX() + 0.5D, q.getY() - 0.5D, q.getZ() + 0.5D, (EntityPlayer)mc.player); 
          RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(q.getX() + 0.5D, q.getY() - 0.5D, q.getZ() + 0.5D));
          if (this.raytrace.getValue()) {
            if (result == null || result.sideHit == null) {
              q = null;
              this.f = null;
              this.render = null;
              resetRotation();
              this.isActive = false;
              this.isPlacing = false;
              return;
            } 
            this.f = result.sideHit;
          } 
          if (this.switchCooldown) {
            this.switchCooldown = false;
            return;
          } 
          if (q != null && mc.player != null) {
            this.isActive = true;
            this.isPlacing = true;
            if (this.raytrace.getValue() && this.f != null) {
              mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, this.f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            } else if (q.getY() == 255) {
              mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            } else {
              mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(q, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
            } 
            mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
            this.PlacedCrystals.add(q);
            if (ModuleManager.isModuleEnabled("AutoGG"))
              AutoGG.INSTANCE.addTargetedPlayer(this.renderEnt.getName()); 
          } 
          if (isSpoofingAngles)
            if (togglePitch) {
              EntityPlayerSP var10000 = mc.player;
              var10000.rotationPitch = (float)(var10000.rotationPitch + 4.0E-4D);
              togglePitch = false;
            } else {
              EntityPlayerSP var10000 = mc.player;
              var10000.rotationPitch = (float)(var10000.rotationPitch - 4.0E-4D);
              togglePitch = true;
            }  
          return;
        } 
      } 
      EntityPlayer entity = (EntityPlayer)var9.next();
      if (entity != mc.player && 
        entity.getHealth() > 0.0F) {
        Iterator<BlockPos> var11 = blocks.iterator();
        while (true) {
          if (!var11.hasNext()) {
            EntityEnderCrystal crystal = mc.world.loadedEntityList.stream().filter(myCrystal -> myCrystal instanceof EntityEnderCrystal).filter(e -> (mc.player.getDistance(e) <= range.getValue())).filter(this::crystalCheck).map(myCrystal -> (EntityEnderCrystal)myCrystal).min(Comparator.comparing(c -> Float.valueOf(mc.player.getDistance((Entity)c)))).orElse(null);
            if (this.explode.getValue() && crystal != null) {
              explodeCrystal(crystal);
              continue label110;
            } 
            resetRotation();
            if (this.oldSlot != -1) {
              mc.player.inventory.currentItem = this.oldSlot;
              this.oldSlot = -1;
            } 
            this.isAttacking = false;
            this.isActive = false;
            this.isBreaking = false;
            continue label110;
          } 
          BlockPos blockPos = var11.next();
          double b = entity.getDistanceSq(blockPos);
          double x = blockPos.getX() + 0.0D;
          double y = blockPos.getY() + 1.0D;
          double z = blockPos.getZ() + 0.0D;
          if (entity.getDistanceSq(x, y, z) < this.enemyRange.getValue() * this.enemyRange.getValue()) {
            double d = calculateDamage(blockPos.getX() + 0.5D, (blockPos.getY() + 1), blockPos.getZ() + 0.5D, (Entity)entity);
            if (d > damage) {
              double targetDamage = calculateDamage(blockPos.getX() + 0.5D, (blockPos.getY() + 1), blockPos.getZ() + 0.5D, (Entity)entity);
              float targetHealth = entity.getHealth() + entity.getAbsorptionAmount();
              if (targetDamage >= this.minDmg.getValue() || targetHealth <= this.facePlace.getValue()) {
                double self = calculateDamage(blockPos.getX() + 0.5D, (blockPos.getY() + 1), blockPos.getZ() + 0.5D, (Entity)mc.player);
                if (self < this.maxSelfDmg.getValue() && 
                  self < (mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
                  damage = d;
                  q = blockPos;
                  this.renderEnt = (Entity)entity;
                } 
              } 
            } 
          } 
        } 
        break;
      } 
    } 
  }
  
  public void explodeCrystal(EntityEnderCrystal crystal) {
    if (this.antiSuicide.getValue() && 
      mc.player.getHealth() + mc.player.getAbsorptionAmount() < this.antiSuicideValue.getValue())
      return; 
    if (!mc.player.canEntityBeSeen((Entity)crystal) && mc.player.getDistance((Entity)crystal) > this.walls.getValue())
      return; 
    if (this.antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
      if (!this.isAttacking) {
        this.oldSlot = mc.player.inventory.currentItem;
        this.isAttacking = true;
      } 
      this.newSlot = -1;
      for (int i = 0; i < 9; i++) {
        ItemStack stack = mc.player.inventory.getStackInSlot(i);
        if (stack != ItemStack.EMPTY) {
          if (stack.getItem() instanceof net.minecraft.item.ItemSword) {
            this.newSlot = i;
            break;
          } 
          if (stack.getItem() instanceof net.minecraft.item.ItemTool) {
            this.newSlot = i;
            break;
          } 
        } 
      } 
      if (this.newSlot != -1) {
        mc.player.inventory.currentItem = this.newSlot;
        this.switchCooldown = true;
      } 
    } 
    if (System.nanoTime() / 1000000L - this.breakSystemTime >= (420 - this.attackSpeed.getValue() * 20)) {
      this.isActive = true;
      this.isBreaking = true;
      if (this.rotate.getValue())
        lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)mc.player); 
      mc.playerController.attackEntity((EntityPlayer)mc.player, (Entity)crystal);
      if (crystal == null)
        return; 
      if (this.handBreak.getValue().equalsIgnoreCase("Offhand") && !(mc.player.getHeldItemOffhand()).isEmpty) {
        mc.player.swingArm(EnumHand.OFF_HAND);
        if (this.cancelCrystal.getValue()) {
          crystal.setDead();
          mc.world.removeAllEntities();
          mc.world.getLoadedEntityList();
        } 
      } else {
        mc.player.swingArm(EnumHand.MAIN_HAND);
        if (this.cancelCrystal.getValue()) {
          crystal.setDead();
          mc.world.removeAllEntities();
          mc.world.getLoadedEntityList();
        } 
      } 
      if (this.handBreak.getValue().equalsIgnoreCase("Both")) {
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.OFF_HAND);
        if (this.cancelCrystal.getValue()) {
          crystal.setDead();
          mc.world.removeAllEntities();
          mc.world.getLoadedEntityList();
        } 
      } 
      if (this.cancelCrystal.getValue()) {
        crystal.setDead();
        mc.world.removeAllEntities();
        mc.world.getLoadedEntityList();
        this.breakSystemTime = System.nanoTime() / 1000000L;
      } 
      this.isActive = false;
      this.isBreaking = false;
    } 
    if (!this.singlePlace.getValue())
      return; 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (this.render != null) {
      GameSenseTessellator.prepare(7);
      GameSenseTessellator.drawBox(this.render, new APColor(this.color.getValue(), 50), 63);
      GameSenseTessellator.release();
      GameSenseTessellator.prepare(7);
      GameSenseTessellator.drawBoundingBoxBlockPos(this.render, 1.0F, new APColor(this.color.getValue(), 255));
      GameSenseTessellator.release();
    } 
    if (this.showDamage.getValue() && 
      this.render != null && this.renderEnt != null) {
      GlStateManager.pushMatrix();
      GameSenseTessellator.glBillboardDistanceScaled(this.render.getX() + 0.5F, this.render.getY() + 0.5F, this.render.getZ() + 0.5F, (EntityPlayer)mc.player, 1.0F);
      double d = calculateDamage(this.render.getX() + 0.5D, (this.render.getY() + 1), this.render.getZ() + 0.5D, this.renderEnt);
      String damageText = ((Math.floor(d) == d) ? (String)Integer.valueOf((int)d) : String.format("%.1f", new Object[] { Double.valueOf(d) })) + "";
      GlStateManager.disableDepth();
      GlStateManager.translate(-(mc.fontRenderer.getStringWidth(damageText) / 2.0D), 0.0D, 0.0D);
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), damageText, 0, 0, new APColor(255, 255, 255));
      GlStateManager.popMatrix();
    } 
  }
  
  private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
    double[] v = calculateLookAt(px, py, pz, me);
    setYawAndPitch((float)v[0], (float)v[1]);
  }
  
  private boolean crystalCheck(Entity crystal) {
    if (!(crystal instanceof EntityEnderCrystal))
      return false; 
    if (this.breakMode.getValue().equalsIgnoreCase("All"))
      return true; 
    if (this.breakMode.getValue().equalsIgnoreCase("Only Own"))
      for (BlockPos pos : new ArrayList(this.PlacedCrystals)) {
        if (pos != null && pos.getDistance((int)crystal.posX, (int)crystal.posY, (int)crystal.posZ) <= 3.0D)
          return true; 
      }  
    if (this.breakMode.getValue().equalsIgnoreCase("Smart")) {
      EntityLivingBase target = (this.renderEnt != null) ? (EntityLivingBase)this.renderEnt : GetNearTarget(crystal);
      if (target == null)
        return false; 
      float targetDmg = calculateDamage(crystal.posX + 0.5D, crystal.posY + 1.0D, crystal.posZ + 0.5D, (Entity)target);
      return (targetDmg >= this.minBreakDmg.getValue() || (targetDmg > this.minBreakDmg.getValue() && target.getHealth() > this.facePlace.getValue()));
    } 
    return false;
  }
  
  private boolean validTarget(Entity entity) {
    if (entity == null)
      return false; 
    if (!(entity instanceof EntityLivingBase))
      return false; 
    if (Friends.isFriend(entity.getName()))
      return false; 
    if (entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0F)
      return false; 
    if (entity instanceof EntityPlayer)
      return (entity != mc.player); 
    return false;
  }
  
  private EntityLivingBase GetNearTarget(Entity distanceTarget) {
    return mc.world.loadedEntityList.stream()
      .filter(entity -> validTarget(entity))
      .map(entity -> (EntityLivingBase)entity)
      .min(Comparator.comparing(entity -> Float.valueOf(distanceTarget.getDistance((Entity)entity))))
      .orElse(null);
  }
  
  public boolean canPlaceCrystal(BlockPos blockPos) {
    BlockPos boost = blockPos.add(0, 1, 0);
    BlockPos boost2 = blockPos.add(0, 2, 0);
    if (!this.endCrystalMode.getValue())
      return ((mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world
        .getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world
        .getBlockState(boost).getBlock() == Blocks.AIR && mc.world
        .getBlockState(boost2).getBlock() == Blocks.AIR && mc.world
        .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world
        .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty()); 
    return ((mc.world.getBlockState(blockPos).getBlock() == Blocks.BEDROCK || mc.world
      .getBlockState(blockPos).getBlock() == Blocks.OBSIDIAN) && mc.world
      .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world
      .getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost2)).isEmpty());
  }
  
  private List<BlockPos> findCrystalBlocks() {
    NonNullList<BlockPos> positions = NonNullList.create();
    positions.addAll((Collection)getSphere(getPlayerPos(), (float)placeRange.getValue(), (int)placeRange.getValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
    return (List<BlockPos>)positions;
  }
  
  public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
    List<BlockPos> circleblocks = new ArrayList<>();
    int cx = loc.getX();
    int cy = loc.getY();
    int cz = loc.getZ();
    for (int x = cx - (int)r; x <= cx + r; x++) {
      for (int z = cz - (int)r; z <= cz + r; ) {
        int y = sphere ? (cy - (int)r) : cy;
        for (;; z++) {
          if (y < (sphere ? (cy + r) : (cy + h))) {
            double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
            if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
              BlockPos l = new BlockPos(x, y + plus_y, z);
              circleblocks.add(l);
            } 
            y++;
            continue;
          } 
        } 
      } 
    } 
    return circleblocks;
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
    this.PlacedCrystals.clear();
    this.isActive = false;
    this.isPlacing = false;
    this.isBreaking = false;
    if (this.chat.getValue() && mc.player != null)
      Command.sendRawMessage("§aAutoCrystal turned ON!"); 
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
    this.render = null;
    this.renderEnt = null;
    resetRotation();
    this.PlacedCrystals.clear();
    this.isActive = false;
    this.isPlacing = false;
    this.isBreaking = false;
    if (this.chat.getValue())
      Command.sendRawMessage("§cAutoCrystal turned OFF!"); 
  }
  
  public String getHudInfo() {
    String t = "";
    if (this.hudDisplay.getValue().equalsIgnoreCase("Mode")) {
      if (this.breakMode.getValue().equalsIgnoreCase("All"))
        t = "[" + ChatFormatting.WHITE + "All" + ChatFormatting.GRAY + "]"; 
      if (this.breakMode.getValue().equalsIgnoreCase("Smart"))
        t = "[" + ChatFormatting.WHITE + "Smart" + ChatFormatting.GRAY + "]"; 
      if (this.breakMode.getValue().equalsIgnoreCase("Only Own"))
        t = "[" + ChatFormatting.WHITE + "Own" + ChatFormatting.GRAY + "]"; 
    } 
    if (this.hudDisplay.getValue().equalsIgnoreCase("None"))
      t = ""; 
    return t;
  }
}
