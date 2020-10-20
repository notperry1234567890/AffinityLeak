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

public class AutoCrystalRewrite extends Module {
  public static Setting.Boolean explode;
  
  public static Setting.Boolean antiWeakness;
  
  public static Setting.Boolean place;
  
  public static Setting.Boolean raytrace;
  
  public static Setting.Boolean rotate;
  
  public static Setting.Boolean spoofRotations;
  
  public static Setting.Boolean chat;
  
  public static Setting.Boolean showDamage;
  
  public static Setting.Boolean singlePlace;
  
  public static Setting.Boolean antiSuicide;
  
  public static Setting.Boolean autoSwitch;
  
  public static Setting.Boolean endCrystalMode;
  
  public static Setting.Boolean cancelCrystal;
  
  public static Setting.Integer placeDelay;
  
  public static Setting.Integer antiSuicideValue;
  
  public static Setting.Integer facePlace;
  
  public static Setting.Integer attackSpeed;
  
  public static Setting.Double maxSelfDmg;
  
  public static Setting.Double minBreakDmg;
  
  public static Setting.Double enemyRange;
  
  public static Setting.Double walls;
  
  public static Setting.Double minDmg;
  
  public static Setting.Double range;
  
  public static Setting.Double placeRange;
  
  public static Setting.Mode handBreak;
  
  public static Setting.Mode breakMode;
  
  public static Setting.Mode hudDisplay;
  
  public static Setting.ColorSetting color;
  
  private static boolean togglePitch = false;
  
  private static boolean isSpoofingAngles;
  
  private static boolean isActive = false;
  
  private static boolean switchCooldown = false;
  
  private static boolean isAttacking = false;
  
  private static boolean isPlacing = false;
  
  private static boolean isBreaking = false;
  
  private static boolean offhand = false;
  
  private static double yaw;
  
  private static double pitch;
  
  private static int oldSlot = -1;
  
  private static int newSlot;
  
  private static int waitCounter;
  
  private static int crystalSlot;
  
  private static long breakSystemTime;
  
  private static final ArrayList<BlockPos> placedCrystals = new ArrayList<>();
  
  private static EnumFacing facing;
  
  private static BlockPos render;
  
  private static BlockPos queuedBlock = null;
  
  private static Entity renderEnt;
  
  @EventHandler
  private final Listener<PacketEvent.Receive> packetReceiveListener;
  
  @EventHandler
  private final Listener<PacketEvent.Send> packetSendListener;
  
  public AutoCrystalRewrite() {
    super("AutoCrystalRewrite", Module.Category.Combat);
    this.packetReceiveListener = new Listener(event -> {
          if (!(event.getPacket() instanceof SPacketSoundEffect))
            return; 
          SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
          if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
            for (Entity e : (Minecraft.getMinecraft()).world.loadedEntityList) {
              if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D)
                e.setDead(); 
            }  
        }new java.util.function.Predicate[0]);
    this.packetSendListener = new Listener(event -> {
          if (!(event.getPacket() instanceof CPacketPlayer))
            return; 
          CPacketPlayer packet = (CPacketPlayer)event.getPacket();
          if (spoofRotations.getValue() && isSpoofingAngles) {
            packet.yaw = (float)yaw;
            packet.pitch = (float)pitch;
          } 
        }new java.util.function.Predicate[0]);
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
    hudModes.add("Current Target");
    hudModes.add("Both");
    hudModes.add("None");
    explode = registerBoolean("Break", "Break", true);
    place = registerBoolean("Place", "Place", true);
    antiSuicide = registerBoolean("Anti Suicide", "AntiSuicide", false);
    antiWeakness = registerBoolean("Anti Weakness", "AntiWeakness", true);
    showDamage = registerBoolean("Show Damage", "ShowDamage", false);
    endCrystalMode = registerBoolean("1.13 Mode", "1.13Mode", false);
    singlePlace = registerBoolean("MultiPlace", "MultiPlace", false);
    autoSwitch = registerBoolean("Auto Switch", "AutoSwitch", true);
    raytrace = registerBoolean("Raytrace", "Raytrace", false);
    rotate = registerBoolean("Rotate", "Rotate", true);
    spoofRotations = registerBoolean("Spoof Angles", "SpoofAngles", true);
    cancelCrystal = registerBoolean("Cancel Crystal", "Cancel Crystal", true);
    chat = registerBoolean("Toggle Msg", "ToggleMsg", true);
    antiSuicideValue = registerInteger("Pause Health", "PauseHealth", 10, 0, 36);
    attackSpeed = registerInteger("Attack Speed", "AttackSpeed", 12, 1, 20);
    placeDelay = registerInteger("Place Delay", "PlaceDelay", 0, 0, 20);
    facePlace = registerInteger("FacePlace HP", "FacePlaceHP", 8, 0, 36);
    placeRange = registerDouble("Place Range", "PlaceRange", 6.0D, 0.0D, 6.0D);
    range = registerDouble("Hit Range", "HitRange", 5.0D, 0.0D, 10.0D);
    walls = registerDouble("Walls Range", "WallsRange", 3.5D, 0.0D, 10.0D);
    enemyRange = registerDouble("Enemy Range", "EnemyRange", 6.0D, 0.5D, 13.0D);
    minDmg = registerDouble("Min Damage", "MinDamage", 5.0D, 0.0D, 36.0D);
    minBreakDmg = registerDouble("Min Break Dmg", "MinBreakDmg", 10.0D, 1.0D, 36.0D);
    maxSelfDmg = registerDouble("Max Self Dmg", "MaxSelfDmg", 10.0D, 1.0D, 36.0D);
    breakMode = registerMode("Break Modes", "BreakModes", breakModes, "All");
    handBreak = registerMode("Hand", "Hand", hands, "Main");
    hudDisplay = registerMode("HUD", "HUD", hudModes, "Mode");
    color = registerColor("Color", "Color");
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
    placedCrystals.clear();
    isActive = false;
    isPlacing = false;
    isBreaking = false;
    if (chat.getValue() && mc.player != null)
      Command.sendClientMessage("§aAutoCrystal turned ON!"); 
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
    render = null;
    renderEnt = null;
    resetRotation();
    placedCrystals.clear();
    isActive = false;
    isPlacing = false;
    isBreaking = false;
    if (chat.getValue())
      Command.sendClientMessage("§cAutoCrystal turned OFF!"); 
  }
  
  public void onUpdate() {
    isActive = false;
    isBreaking = false;
    isPlacing = false;
    if (mc.player == null || mc.player.isDead)
      return; 
    crystalSlot = getCrystalItemSlot();
    if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
      offhand = true;
    } else if (crystalSlot == -1) {
      return;
    } 
    List<BlockPos> blocks = findCrystalBlocks();
    List<Entity> entities = new ArrayList<>((Collection<? extends Entity>)mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).sorted(Comparator.comparing(e -> Float.valueOf(mc.player.getDistance((Entity)e)))).collect(Collectors.toList()));
    double damage = 0.5D;
    Iterator<Entity> var9 = entities.iterator();
    label48: while (true) {
      if (!var9.hasNext()) {
        if (damage <= 1.5D) {
          render = null;
          renderEnt = null;
          resetRotation();
          return;
        } 
        render = queuedBlock;
        calcAndPlaceCrystal();
        return;
      } 
      EntityPlayer entity = (EntityPlayer)var9.next();
      if (entity != mc.player && 
        entity.getHealth() > 0.0F) {
        Iterator<BlockPos> var11 = blocks.iterator();
        while (true) {
          if (!var11.hasNext()) {
            EntityEnderCrystal crystal = getTargetCrystal();
            if (explode.getValue() && crystal != null) {
              explodeCrystal(crystal);
              continue label48;
            } 
            resetRotation();
            if (oldSlot != -1) {
              mc.player.inventory.currentItem = oldSlot;
              oldSlot = -1;
            } 
            isAttacking = false;
            isActive = false;
            isBreaking = false;
            continue label48;
          } 
          BlockPos blockPos = var11.next();
          double x = blockPos.getX() + 0.0D;
          double y = blockPos.getY() + 1.0D;
          double z = blockPos.getZ() + 0.0D;
          if (entity.getDistanceSq(x, y, z) < enemyRange.getValue() * enemyRange.getValue()) {
            double d = calculateDamage(blockPos.getX() + 0.5D, (blockPos.getY() + 1), blockPos.getZ() + 0.5D, (Entity)entity);
            if (d > damage) {
              double targetDamage = calculateDamage(blockPos.getX() + 0.5D, (blockPos.getY() + 1), blockPos.getZ() + 0.5D, (Entity)entity);
              float targetHealth = entity.getHealth() + entity.getAbsorptionAmount();
              if (targetDamage >= minDmg.getValue() || targetHealth <= facePlace.getValue()) {
                double self = calculateDamage(blockPos.getX() + 0.5D, (blockPos.getY() + 1), blockPos.getZ() + 0.5D, (Entity)mc.player);
                if (self < maxSelfDmg.getValue() && 
                  self < (mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
                  damage = d;
                  queuedBlock = blockPos;
                  renderEnt = (Entity)entity;
                } 
              } 
            } 
          } 
        } 
        break;
      } 
    } 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (render != null) {
      GameSenseTessellator.prepare(7);
      GameSenseTessellator.drawBox(render, new APColor(color.getValue(), 50), 63);
      GameSenseTessellator.release();
      GameSenseTessellator.prepare(7);
      GameSenseTessellator.drawBoundingBoxBlockPos(render, 1.0F, new APColor(color.getValue(), 255));
      GameSenseTessellator.release();
    } 
    if (showDamage.getValue() && 
      render != null && renderEnt != null) {
      GlStateManager.pushMatrix();
      GameSenseTessellator.glBillboardDistanceScaled(render.getX() + 0.5F, render.getY() + 0.5F, render.getZ() + 0.5F, (EntityPlayer)mc.player, 1.0F);
      double d = calculateDamage(render.getX() + 0.5D, (render.getY() + 1), render.getZ() + 0.5D, renderEnt);
      String damageText = ((Math.floor(d) == d) ? (String)Integer.valueOf((int)d) : String.format("%.1f", new Object[] { Double.valueOf(d) })) + "";
      GlStateManager.disableDepth();
      GlStateManager.translate(-(mc.fontRenderer.getStringWidth(damageText) / 2.0D), 0.0D, 0.0D);
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), damageText, 0, 0, new APColor(255, 255, 255));
      GlStateManager.popMatrix();
    } 
  }
  
  private static boolean crystalCheck(Entity crystal) {
    if (!(crystal instanceof EntityEnderCrystal))
      return false; 
    if (breakMode.getValue().equalsIgnoreCase("All"))
      return true; 
    if (breakMode.getValue().equalsIgnoreCase("Only Own"))
      for (BlockPos pos : placedCrystals) {
        if (pos != null && pos.getDistance((int)crystal.posX, (int)crystal.posY, (int)crystal.posZ) <= 3.0D)
          return true; 
      }  
    if (breakMode.getValue().equalsIgnoreCase("Smart")) {
      EntityLivingBase target = (renderEnt != null) ? (EntityLivingBase)renderEnt : getNearTarget(crystal);
      if (target == null)
        return false; 
      float targetDmg = calculateDamage(crystal.posX + 0.5D, crystal.posY + 1.0D, crystal.posZ + 0.5D, (Entity)target);
      return (targetDmg >= minBreakDmg.getValue() || (targetDmg > minBreakDmg.getValue() && target.getHealth() > facePlace.getValue()));
    } 
    return false;
  }
  
  private static boolean validTarget(Entity entity) {
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
  
  private static boolean canPlaceCrystal(BlockPos blockPos) {
    BlockPos boost = blockPos.add(0, 1, 0);
    BlockPos boost2 = blockPos.add(0, 2, 0);
    if (!endCrystalMode.getValue())
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
  
  private static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
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
  
  private static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
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
  
  private static int getCrystalItemSlot() {
    if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL)
      return mc.player.inventory.currentItem; 
    for (int slot = 0; slot < 9; slot++) {
      if (mc.player.inventory.getStackInSlot(slot).getItem() == Items.END_CRYSTAL)
        return slot; 
    } 
    return -1;
  }
  
  private static double[] calculateLookAt(double px, double py, double pz, EntityPlayer me) {
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
  
  private static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  private static EntityLivingBase getNearTarget(Entity distanceTarget) {
    return mc.world.loadedEntityList.stream()
      .filter(AutoCrystalRewrite::validTarget)
      .map(entity -> (EntityLivingBase)entity)
      .min(Comparator.comparing(distanceTarget::getDistance))
      .orElse(null);
  }
  
  private static EntityEnderCrystal getTargetCrystal() {
    return breakMode.getValue().equals("All") ? mc.world.loadedEntityList
      .stream()
      .filter(myCrystal -> myCrystal instanceof EntityEnderCrystal)
      .filter(e -> (mc.player.getDistance(e) <= range.getValue()))
      .filter(placedCrystals::contains)
      .map(myCrystal -> (EntityEnderCrystal)myCrystal)
      .min(Comparator.comparing(c -> Float.valueOf(mc.player.getDistance((Entity)c))))
      .orElse(null) : mc.world.loadedEntityList
      
      .stream()
      .filter(myCrystal -> myCrystal instanceof EntityEnderCrystal)
      .filter(e -> (mc.player.getDistance(e) <= range.getValue()))
      .filter(AutoCrystalRewrite::crystalCheck)
      .filter(entity -> placedCrystals.contains(new BlockPos(entity.posX, entity.posY, entity.posZ)))
      .map(myCrystal -> (EntityEnderCrystal)myCrystal)
      .min(Comparator.comparing(c -> Float.valueOf(mc.player.getDistance((Entity)c))))
      .orElse(null);
  }
  
  private static List<BlockPos> findCrystalBlocks() {
    NonNullList<BlockPos> positions = NonNullList.create();
    positions.addAll((Collection)getSphere(getPlayerPos(), (float)placeRange.getValue(), (int)placeRange.getValue(), false, true, 0).stream().filter(AutoCrystalRewrite::canPlaceCrystal).collect(Collectors.toList()));
    return (List<BlockPos>)positions;
  }
  
  private static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
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
  
  private static void setYawAndPitch(float yaw1, float pitch1) {
    yaw = yaw1;
    pitch = pitch1;
    isSpoofingAngles = true;
  }
  
  private static void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
    double[] v = calculateLookAt(px, py, pz, me);
    setYawAndPitch((float)v[0], (float)v[1]);
  }
  
  private static void resetRotation() {
    if (isSpoofingAngles) {
      yaw = mc.player.rotationYaw;
      pitch = mc.player.rotationPitch;
      isSpoofingAngles = false;
    } 
  }
  
  private static void explodeCrystal(EntityEnderCrystal crystal) {
    if (antiSuicide.getValue() && 
      mc.player.getHealth() + mc.player.getAbsorptionAmount() < antiSuicideValue.getValue())
      return; 
    if (!mc.player.canEntityBeSeen((Entity)crystal) && mc.player.getDistance((Entity)crystal) > walls.getValue())
      return; 
    if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
      if (!isAttacking) {
        oldSlot = mc.player.inventory.currentItem;
        isAttacking = true;
      } 
      newSlot = -1;
      for (int i = 0; i < 9; i++) {
        ItemStack stack = mc.player.inventory.getStackInSlot(i);
        if (stack != ItemStack.EMPTY) {
          if (stack.getItem() instanceof net.minecraft.item.ItemSword) {
            newSlot = i;
            break;
          } 
          if (stack.getItem() instanceof net.minecraft.item.ItemTool) {
            newSlot = i;
            break;
          } 
        } 
      } 
      if (newSlot != -1) {
        mc.player.inventory.currentItem = newSlot;
        switchCooldown = true;
      } 
    } 
    if (System.nanoTime() / 1000000L - breakSystemTime >= (420 - attackSpeed.getValue() * 20)) {
      isActive = true;
      isBreaking = true;
      if (rotate.getValue())
        lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, (EntityPlayer)mc.player); 
      mc.playerController.attackEntity((EntityPlayer)mc.player, (Entity)crystal);
      if (crystal == null)
        return; 
      if (handBreak.getValue().equalsIgnoreCase("Offhand") && !(mc.player.getHeldItemOffhand()).isEmpty) {
        mc.player.swingArm(EnumHand.OFF_HAND);
      } else {
        mc.player.swingArm(EnumHand.MAIN_HAND);
      } 
      if (cancelCrystal.getValue())
        doCancelCrystal(crystal); 
      if (handBreak.getValue().equalsIgnoreCase("Both")) {
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.OFF_HAND);
        if (cancelCrystal.getValue())
          doCancelCrystal(crystal); 
      } 
      if (cancelCrystal.getValue()) {
        doCancelCrystal(crystal);
        breakSystemTime = System.nanoTime() / 1000000L;
      } 
      isActive = false;
      isBreaking = false;
    } 
    if (!singlePlace.getValue())
      return; 
  }
  
  private static void calcAndPlaceCrystal() {
    if (place.getValue()) {
      if (antiSuicide.getValue() && mc.player
        .getHealth() + mc.player
        .getAbsorptionAmount() < antiSuicideValue
        .getValue())
        return; 
      if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
        if (autoSwitch.getValue()) {
          mc.player.inventory.currentItem = crystalSlot;
          resetRotation();
          switchCooldown = true;
        } 
        return;
      } 
      if (rotate.getValue())
        lookAtPacket(queuedBlock.getX() + 0.5D, queuedBlock.getY() - 0.5D, queuedBlock.getZ() + 0.5D, (EntityPlayer)mc.player); 
      if (raytrace.getValue()) {
        RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player
              
              .getEyeHeight(), mc.player.posZ), new Vec3d(queuedBlock
              
              .getX() + 0.5D, queuedBlock
              .getY() - 0.5D, queuedBlock
              .getZ() + 0.5D));
        if (result == null || result.sideHit == null) {
          queuedBlock = null;
          facing = null;
          render = null;
          resetRotation();
          isActive = false;
          isPlacing = false;
          return;
        } 
        facing = result.sideHit;
      } 
      if (switchCooldown) {
        switchCooldown = false;
        return;
      } 
      if (queuedBlock != null && mc.player != null) {
        isActive = true;
        isPlacing = true;
        if (raytrace.getValue() && facing != null) {
          mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(queuedBlock, facing, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
        } else if (queuedBlock.getY() == 255) {
          mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(queuedBlock, EnumFacing.DOWN, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
        } else {
          mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(queuedBlock, EnumFacing.UP, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
        } 
        mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
        placedCrystals.add(queuedBlock);
        addToAutoGG(renderEnt.getName());
      } 
      trySpoofAngles();
    } 
  }
  
  private static void doCancelCrystal(EntityEnderCrystal crystal) {
    crystal.setDead();
    mc.world.removeEntity((Entity)crystal);
    mc.world.removeAllEntities();
    mc.world.getLoadedEntityList();
  }
  
  private static void addToAutoGG(String name) {
    if (ModuleManager.isModuleEnabled("AutoGG"))
      AutoGG.INSTANCE.addTargetedPlayer(name); 
  }
  
  private static void trySpoofAngles() {
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
  }
  
  public String getHudInfo() {
    String t = "";
    if (hudDisplay.getValue().equalsIgnoreCase("Mode")) {
      if (breakMode.getValue().equalsIgnoreCase("All"))
        t = "[" + ChatFormatting.WHITE + "All" + ChatFormatting.GRAY + "]"; 
      if (breakMode.getValue().equalsIgnoreCase("Smart"))
        t = "[" + ChatFormatting.WHITE + "Smart" + ChatFormatting.GRAY + "]"; 
      if (breakMode.getValue().equalsIgnoreCase("Only Own"))
        t = "[" + ChatFormatting.WHITE + "Own" + ChatFormatting.GRAY + "]"; 
    } 
    if (hudDisplay.getValue().equalsIgnoreCase("None"))
      t = ""; 
    if (hudDisplay.getValue().equalsIgnoreCase("Current Target"));
    return t;
  }
}
