//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util.world;

import com.gamesense.api.util.Wrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockUtils {
  public static final List blackList;
  
  public static final List shulkerList;
  
  static Minecraft mc = Minecraft.getMinecraft();
  
  static {
    blackList = Arrays.asList(new Block[] { Blocks.ENDER_CHEST, (Block)Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, (Block)Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER });
    shulkerList = Arrays.asList(new Block[] { 
          Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, 
          Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX });
    mc = Minecraft.getMinecraft();
  }
  
  public static boolean isEntitiesEmpty(BlockPos pos) {
    List<Entity> entities = (List<Entity>)mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos)).stream().filter(e -> !(e instanceof net.minecraft.entity.item.EntityItem)).filter(e -> !(e instanceof net.minecraft.entity.item.EntityXPOrb)).collect(Collectors.toList());
    return entities.isEmpty();
  }
  
  public static float[] calcAngle(Vec3d from, Vec3d to) {
    double difX = to.x - from.x;
    double difY = (to.y - from.y) * -1.0D;
    double difZ = to.z - from.z;
    double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
    return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0D), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
  }
  
  public static boolean placeBlockScaffold(BlockPos pos, boolean rotate) {
    EnumFacing[] arrayOfEnumFacing;
    int i;
    byte b;
    for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) {
      EnumFacing side = arrayOfEnumFacing[b];
      BlockPos neighbor = pos.offset(side);
      EnumFacing side2 = side.getOpposite();
      if (!canBeClicked(neighbor)) {
        b++;
        continue;
      } 
      Vec3d hitVec = (new Vec3d((Vec3i)neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
      if (rotate)
        faceVectorPacketInstant(hitVec); 
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      processRightClickBlock(neighbor, side2, hitVec);
      mc.player.swingArm(EnumHand.MAIN_HAND);
      mc.rightClickDelayTimer = 0;
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      return true;
    } 
    return false;
  }
  
  private static PlayerControllerMP getPlayerController() {
    return mc.playerController;
  }
  
  public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
    getPlayerController().processRightClickBlock(mc.player, mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
  }
  
  public static IBlockState getState(BlockPos pos) {
    return mc.world.getBlockState(pos);
  }
  
  public static boolean checkForNeighbours(BlockPos blockPos) {
    if (!hasNeighbour(blockPos)) {
      for (EnumFacing side : EnumFacing.values()) {
        BlockPos neighbour = blockPos.offset(side);
        if (hasNeighbour(neighbour))
          return true; 
      } 
      return false;
    } 
    return true;
  }
  
  private static boolean hasNeighbour(BlockPos blockPos) {
    for (EnumFacing side : EnumFacing.values()) {
      BlockPos neighbour = blockPos.offset(side);
      if (!Wrapper.getWorld().getBlockState(neighbour).getMaterial().isReplaceable())
        return true; 
    } 
    return false;
  }
  
  public static Block getBlock(BlockPos pos) {
    return getState(pos).getBlock();
  }
  
  public static boolean canBeClicked(BlockPos pos) {
    return getBlock(pos).canCollideCheck(getState(pos), false);
  }
  
  public static void faceVectorPacketInstant(Vec3d vec) {
    float[] rotations = getNeededRotations2(vec);
    mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
  }
  
  private static float[] getNeededRotations2(Vec3d vec) {
    Vec3d eyesPos = getEyesPos();
    double diffX = vec.x - eyesPos.x;
    double diffY = vec.y - eyesPos.y;
    double diffZ = vec.z - eyesPos.z;
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
    return new float[] { mc.player.rotationYaw + 
        
        MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + 
        
        MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
  }
  
  public static Vec3d getEyesPos() {
    return new Vec3d(mc.player.posX, mc.player.posY + mc.player
        .getEyeHeight(), mc.player.posZ);
  }
  
  public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
    return getInterpolatedAmount(entity, ticks, ticks, ticks);
  }
  
  public static List getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
    ArrayList<BlockPos> circleblocks = new ArrayList();
    int cx = loc.getX();
    int cy = loc.getY();
    int cz = loc.getZ();
    for (int x = cx - (int)r; x <= cx + r; x++) {
      for (int z = cz - (int)r; z <= cz + r; ) {
        int y = sphere ? (cy - (int)r) : cy;
        while (true) {
          float f = sphere ? (cy + r) : (cy + h);
          if (y >= f) {
            z++;
            continue;
          } 
          double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
          if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
            BlockPos l = new BlockPos(x, y + plus_y, z);
            circleblocks.add(l);
          } 
          y++;
        } 
      } 
    } 
    return circleblocks;
  }
  
  public static List<BlockPos> getCircle(BlockPos loc, int y, float r, boolean hollow) {
    List<BlockPos> circleblocks = new ArrayList<>();
    int cx = loc.getX();
    int cz = loc.getZ();
    for (int x = cx - (int)r; x <= cx + r; x++) {
      for (int z = cz - (int)r; z <= cz + r; z++) {
        double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z));
        if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
          BlockPos l = new BlockPos(x, y, z);
          circleblocks.add(l);
        } 
      } 
    } 
    return circleblocks;
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
  }
  
  public static EnumFacing getPlaceableSide(BlockPos pos) {
    for (EnumFacing side : EnumFacing.values()) {
      BlockPos neighbour = pos.offset(side);
      if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
        IBlockState blockState = mc.world.getBlockState(neighbour);
        if (!blockState.getMaterial().isReplaceable())
          return side; 
      } 
    } 
    return null;
  }
}
