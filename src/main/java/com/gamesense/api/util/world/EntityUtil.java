//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util.world;

import com.gamesense.api.util.Wrapper;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.io.IOUtils;

public class EntityUtil {
  private static final Minecraft mc = Minecraft.getMinecraft();
  
  public static boolean isPassive(Entity e) {
    if (e instanceof EntityWolf && ((EntityWolf)e).isAngry())
      return false; 
    if (e instanceof net.minecraft.entity.passive.EntityAnimal || e instanceof net.minecraft.entity.EntityAgeable || e instanceof net.minecraft.entity.passive.EntityTameable || e instanceof net.minecraft.entity.passive.EntityAmbientCreature || e instanceof net.minecraft.entity.passive.EntitySquid)
      return true; 
    return (e instanceof EntityIronGolem && ((EntityIronGolem)e).getRevengeTarget() == null);
  }
  
  public static boolean isLiving(Entity e) {
    return e instanceof net.minecraft.entity.EntityLivingBase;
  }
  
  public static BlockPos GetLocalPlayerPosFloored() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  public static boolean isFakeLocalPlayer(Entity entity) {
    return (entity != null && entity.getEntityId() == -100 && Wrapper.getPlayer() != entity);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
  }
  
  public static String getNameFromUUID(String uuid) {
    try {
      String jsonUrl = IOUtils.toString(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names"));
      JsonParser parser = new JsonParser();
      return parser.parse(jsonUrl).getAsJsonArray().get(parser.parse(jsonUrl).getAsJsonArray().size() - 1).getAsJsonObject().get("name").toString();
    } catch (IOException iOException) {
      return null;
    } 
  }
  
  public static Block isColliding(double posX, double posY, double posZ) {
    Block block = null;
    if (mc.player != null) {
      AxisAlignedBB bb = (mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().getEntityBoundingBox().contract(0.0D, 0.0D, 0.0D).offset(posX, posY, posZ) : mc.player.getEntityBoundingBox().contract(0.0D, 0.0D, 0.0D).offset(posX, posY, posZ);
      int y = (int)bb.minY;
      for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
        for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++)
          block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock(); 
      } 
    } 
    return block;
  }
  
  public static double getBaseMoveSpeed() {
    double baseSpeed = 0.2873D;
    if (mc.player != null && mc.player.isPotionActive(Potion.getPotionById(1))) {
      int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
      baseSpeed *= 1.0D + 0.2D * (amplifier + 1);
    } 
    return baseSpeed;
  }
  
  public static boolean isInLiquid() {
    if (mc.player != null) {
      if (mc.player.fallDistance >= 3.0F)
        return false; 
      boolean inLiquid = false;
      AxisAlignedBB bb = (mc.player.getRidingEntity() != null) ? mc.player.getRidingEntity().getEntityBoundingBox() : mc.player.getEntityBoundingBox();
      int y = (int)bb.minY;
      for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
        for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++) {
          Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
          if (!(block instanceof net.minecraft.block.BlockAir)) {
            if (!(block instanceof net.minecraft.block.BlockLiquid))
              return false; 
            inLiquid = true;
          } 
        } 
      } 
      return inLiquid;
    } 
    return false;
  }
  
  public static void setTimer(float speed) {
    (Minecraft.getMinecraft()).timer.tickLength = 50.0F / speed;
  }
  
  public static void resetTimer() {
    (Minecraft.getMinecraft()).timer.tickLength = 50.0F;
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
    return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
    return getInterpolatedAmount(entity, ticks, ticks, ticks);
  }
  
  public static boolean isMobAggressive(Entity entity) {
    if (entity instanceof EntityPigZombie) {
      if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry())
        return true; 
    } else {
      if (entity instanceof EntityWolf)
        return (((EntityWolf)entity).isAngry() && 
          !Wrapper.getPlayer().equals(((EntityWolf)entity).getOwner())); 
      if (entity instanceof EntityEnderman)
        return ((EntityEnderman)entity).isScreaming(); 
    } 
    return isHostileMob(entity);
  }
  
  public static boolean isNeutralMob(Entity entity) {
    return (entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman);
  }
  
  public static boolean isFriendlyMob(Entity entity) {
    return ((entity.isCreatureType(EnumCreatureType.CREATURE, false) && !isNeutralMob(entity)) || entity
      .isCreatureType(EnumCreatureType.AMBIENT, false) || entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof EntityIronGolem || (
      
      isNeutralMob(entity) && !isMobAggressive(entity)));
  }
  
  public static boolean isHostileMob(Entity entity) {
    return (entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity));
  }
  
  public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
  }
  
  public static Vec3d getInterpolatedRenderPos(Entity entity, float ticks) {
    return getInterpolatedPos(entity, ticks).subtract((Wrapper.getMinecraft().getRenderManager()).renderPosX, (Wrapper.getMinecraft().getRenderManager()).renderPosY, (Wrapper.getMinecraft().getRenderManager()).renderPosZ);
  }
  
  public static boolean isInWater(Entity entity) {
    if (entity == null)
      return false; 
    double y = entity.posY + 0.01D;
    for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
      for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
        BlockPos pos = new BlockPos(x, (int)y, z);
        if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockLiquid)
          return true; 
      } 
    } 
    return false;
  }
  
  public static boolean isDrivenByPlayer(Entity entityIn) {
    return (Wrapper.getPlayer() != null && entityIn != null && entityIn.equals(Wrapper.getPlayer().getRidingEntity()));
  }
  
  public static boolean isAboveWater(Entity entity) {
    return isAboveWater(entity, false);
  }
  
  public static boolean isAboveWater(Entity entity, boolean packet) {
    if (entity == null)
      return false; 
    double y = entity.posY - (packet ? 0.03D : (isPlayer(entity) ? 0.2D : 0.5D));
    for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
      for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
        BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
        if (Wrapper.getWorld().getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockLiquid)
          return true; 
      } 
    } 
    return false;
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
  
  public static boolean isPlayer(Entity entity) {
    return entity instanceof EntityPlayer;
  }
  
  public static double getRelativeX(float yaw) {
    return MathHelper.sin(-yaw * 0.017453292F);
  }
  
  public static double getRelativeZ(float yaw) {
    return MathHelper.cos(yaw * 0.017453292F);
  }
}
