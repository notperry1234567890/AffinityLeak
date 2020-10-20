//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.MinecraftForge;

public class Surround extends Module {
  Setting.Boolean chatMsg;
  
  Setting.Boolean triggerSurround;
  
  Setting.Boolean rotate;
  
  Setting.Boolean disableOnJump;
  
  Setting.Boolean centerPlayer;
  
  Setting.Integer tickDelay;
  
  Setting.Integer timeOutTicks;
  
  Setting.Integer blocksPerTick;
  
  private int cachedHotbarSlot = -1;
  
  private int obbyHotbarSlot;
  
  private boolean noObby = false;
  
  private boolean isSneaking = false;
  
  private boolean firstRun = false;
  
  private int runTimeTicks = 0;
  
  private int delayTimeTicks = 0;
  
  private int offsetSteps = 0;
  
  private Vec3d centeredBlock = Vec3d.ZERO;
  
  public Surround() {
    super("Surround", Module.Category.Combat);
  }
  
  public void setup() {
    this.triggerSurround = registerBoolean("Triggerable", "Triggerable", false);
    this.disableOnJump = registerBoolean("Disable On Jump", "DisableOnJump", false);
    this.rotate = registerBoolean("Rotate", "Rotate", true);
    this.centerPlayer = registerBoolean("Center Player", "CenterPlayer", false);
    this.tickDelay = registerInteger("Tick Delay", "TickDelay", 5, 0, 10);
    this.timeOutTicks = registerInteger("Timeout Ticks", "TimeoutTicks", 40, 1, 100);
    this.blocksPerTick = registerInteger("Blocks Per Tick", "BlocksPerTick", 4, 0, 8);
    this.chatMsg = registerBoolean("Chat Msgs", "ChatMsgs", true);
  }
  
  public void onEnable() {
    MinecraftForge.EVENT_BUS.register(this);
    if (mc.player == null) {
      disable();
      return;
    } 
    if (this.chatMsg.getValue())
      Command.sendClientMessage("§aSurround turned ON!"); 
    this.centeredBlock = getCenterOfBlock(mc.player.posX, mc.player.posY, mc.player.posY);
    this.cachedHotbarSlot = mc.player.inventory.currentItem;
    this.obbyHotbarSlot = -1;
  }
  
  public void onDisable() {
    MinecraftForge.EVENT_BUS.unregister(this);
    if (mc.player == null)
      return; 
    if (this.chatMsg.getValue()) {
      if (this.noObby)
        return; 
      Command.sendClientMessage("§cSurround turned OFF!");
    } 
    if (this.obbyHotbarSlot != this.cachedHotbarSlot && this.cachedHotbarSlot != -1)
      mc.player.inventory.currentItem = this.cachedHotbarSlot; 
    if (this.isSneaking) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      this.isSneaking = false;
    } 
    this.cachedHotbarSlot = -1;
    this.obbyHotbarSlot = -1;
    this.centeredBlock = Vec3d.ZERO;
    this.noObby = false;
    this.firstRun = false;
  }
  
  public void onUpdate() {
    if (mc.player == null) {
      disable();
      return;
    } 
    if (this.disableOnJump.getValue() && 
      !mc.player.onGround) {
      disable();
      return;
    } 
    if (mc.player.posY <= 0.0D)
      return; 
    if (this.centerPlayer.getValue() && this.centeredBlock != Vec3d.ZERO && mc.player.onGround) {
      double xDeviation = Math.abs(this.centeredBlock.x - mc.player.posX);
      double zDeviation = Math.abs(this.centeredBlock.z - mc.player.posZ);
      if (xDeviation <= 0.1D && zDeviation <= 0.1D) {
        this.centeredBlock = Vec3d.ZERO;
      } else {
        double newX, newZ;
        if (mc.player.posX > Math.round(mc.player.posX)) {
          newX = Math.round(mc.player.posX) + 0.5D;
        } else if (mc.player.posX < Math.round(mc.player.posX)) {
          newX = Math.round(mc.player.posX) - 0.5D;
        } else {
          newX = mc.player.posX;
        } 
        if (mc.player.posZ > Math.round(mc.player.posZ)) {
          newZ = Math.round(mc.player.posZ) + 0.5D;
        } else if (mc.player.posZ < Math.round(mc.player.posZ)) {
          newZ = Math.round(mc.player.posZ) - 0.5D;
        } else {
          newZ = mc.player.posZ;
        } 
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(newX, mc.player.posY, newZ, true));
        mc.player.setPosition(newX, mc.player.posY, newZ);
      } 
    } 
    if (this.firstRun) {
      this.firstRun = false;
      if (findObsidianSlot() == -1)
        return; 
    } else {
      if (this.delayTimeTicks < this.tickDelay.getValue()) {
        this.delayTimeTicks++;
        return;
      } 
      this.delayTimeTicks = 0;
    } 
    if (findObsidianSlot() == -1)
      return; 
    if (this.triggerSurround.getValue() && this.runTimeTicks >= this.timeOutTicks.getValue()) {
      this.runTimeTicks = 0;
      disable();
      return;
    } 
    int blocksPlaced = 0;
    while (blocksPlaced <= this.blocksPerTick.getValue()) {
      Vec3d[] offsetPattern = Offsets.SURROUND;
      int maxSteps = Offsets.SURROUND.length;
      if (this.offsetSteps >= maxSteps) {
        this.offsetSteps = 0;
        break;
      } 
      BlockPos offsetPos = new BlockPos(offsetPattern[this.offsetSteps]);
      BlockPos targetPos = (new BlockPos(mc.player.getPositionVector())).add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
      boolean tryPlacing = true;
      if (!mc.world.getBlockState(targetPos).getMaterial().isReplaceable())
        tryPlacing = false; 
      for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(targetPos))) {
        if (entity instanceof net.minecraft.entity.item.EntityItem || entity instanceof net.minecraft.entity.item.EntityXPOrb) {
          tryPlacing = false;
          break;
        } 
      } 
      if (tryPlacing && placeBlock(targetPos))
        blocksPlaced++; 
      this.offsetSteps++;
    } 
    this.runTimeTicks++;
  }
  
  private int findObsidianSlot() {
    int slot = -1;
    for (int i = 0; i < 9; i++) {
      ItemStack stack = mc.player.inventory.getStackInSlot(i);
      if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
        Block block = ((ItemBlock)stack.getItem()).getBlock();
        if (block instanceof net.minecraft.block.BlockObsidian) {
          slot = i;
          break;
        } 
      } 
    } 
    return slot;
  }
  
  private boolean placeBlock(BlockPos pos) {
    Block block = mc.world.getBlockState(pos).getBlock();
    if (!(block instanceof net.minecraft.block.BlockAir) && !(block instanceof net.minecraft.block.BlockLiquid))
      return false; 
    EnumFacing side = BlockUtils.getPlaceableSide(pos);
    if (side == null)
      return false; 
    BlockPos neighbour = pos.offset(side);
    EnumFacing opposite = side.getOpposite();
    if (!BlockUtils.canBeClicked(neighbour))
      return false; 
    Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
    Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
    int obsidianSlot = findObsidianSlot();
    if (mc.player.inventory.currentItem != obsidianSlot) {
      this.obbyHotbarSlot = obsidianSlot;
      mc.player.inventory.currentItem = obsidianSlot;
    } 
    if ((!this.isSneaking && BlockUtils.blackList.contains(neighbourBlock)) || BlockUtils.shulkerList.contains(neighbourBlock)) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      this.isSneaking = true;
    } 
    if (obsidianSlot == -1) {
      this.noObby = true;
      return false;
    } 
    if (this.rotate.getValue())
      BlockUtils.faceVectorPacketInstant(hitVec); 
    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    mc.player.swingArm(EnumHand.MAIN_HAND);
    mc.rightClickDelayTimer = 4;
    return true;
  }
  
  private Vec3d getCenterOfBlock(double playerX, double playerY, double playerZ) {
    double newX = Math.floor(playerX) + 0.5D;
    double newY = Math.floor(playerY);
    double newZ = Math.floor(playerZ) + 0.5D;
    return new Vec3d(newX, newY, newZ);
  }
  
  private static class Offsets {
    private static final Vec3d[] SURROUND = new Vec3d[] { new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, 1.0D), new Vec3d(-1.0D, -1.0D, 0.0D), new Vec3d(0.0D, -1.0D, -1.0D) };
  }
}
