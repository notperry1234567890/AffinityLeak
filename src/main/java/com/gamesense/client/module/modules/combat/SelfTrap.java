//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class SelfTrap extends Module {
  Setting.Mode mode;
  
  private Setting.Integer blocksPerTick;
  
  private Setting.Integer tickDelay;
  
  private Setting.Boolean rotate;
  
  private EntityPlayer closestTarget;
  
  private String lastTickTargetName;
  
  private int playerHotbarSlot = -1;
  
  private int lastHotbarSlot = -1;
  
  private int delayStep = 0;
  
  private boolean isSneaking = false;
  
  private int offsetStep = 0;
  
  private boolean firstRun;
  
  public SelfTrap() {
    super("SelfTrap", Module.Category.Combat);
  }
  
  public void setup() {
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Normal");
    modes.add("NoStep");
    modes.add("Simple");
    this.mode = registerMode("Mode", "Mode", modes, "Normal");
    this.rotate = registerBoolean("Rotate", "Rotate", true);
    this.blocksPerTick = registerInteger("Blocks Per Tick", "BlocksPerTick", 5, 0, 10);
    this.tickDelay = registerInteger("Delay", "Delay", 0, 0, 10);
  }
  
  protected void onEnable() {
    if (mc.player == null) {
      disable();
      return;
    } 
    this.firstRun = true;
    this.playerHotbarSlot = mc.player.inventory.currentItem;
    this.lastHotbarSlot = -1;
  }
  
  protected void onDisable() {
    if (mc.player == null)
      return; 
    if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1)
      mc.player.inventory.currentItem = this.playerHotbarSlot; 
    if (this.isSneaking) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      this.isSneaking = false;
    } 
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
  }
  
  public void onUpdate() {
    if (mc.player == null)
      return; 
    if (!this.firstRun) {
      if (this.delayStep < this.tickDelay.getValue()) {
        this.delayStep++;
        return;
      } 
      this.delayStep = 0;
    } 
    findClosestTarget();
    if (this.closestTarget == null) {
      if (this.firstRun)
        this.firstRun = false; 
      return;
    } 
    if (this.firstRun) {
      this.firstRun = false;
      this.lastTickTargetName = this.closestTarget.getName();
    } else if (!this.lastTickTargetName.equals(this.closestTarget.getName())) {
      this.lastTickTargetName = this.closestTarget.getName();
      this.offsetStep = 0;
    } 
    List<Vec3d> placeTargets = new ArrayList<>();
    if (this.mode.getValue().equalsIgnoreCase("Normal"))
      Collections.addAll(placeTargets, Offsets.TRAP); 
    if (this.mode.getValue().equalsIgnoreCase("NoStep"))
      Collections.addAll(placeTargets, Offsets.TRAPFULLROOF); 
    if (this.mode.getValue().equalsIgnoreCase("Simple"))
      Collections.addAll(placeTargets, Offsets.TRAPSIMPLE); 
    int blocksPlaced = 0;
    while (blocksPlaced < this.blocksPerTick.getValue()) {
      if (this.offsetStep >= placeTargets.size()) {
        this.offsetStep = 0;
        break;
      } 
      BlockPos offsetPos = new BlockPos(placeTargets.get(this.offsetStep));
      BlockPos targetPos = (new BlockPos(this.closestTarget.getPositionVector())).down().add(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ());
      if (placeBlockInRange(targetPos))
        blocksPlaced++; 
      this.offsetStep++;
    } 
    if (blocksPlaced > 0) {
      if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
        mc.player.inventory.currentItem = this.playerHotbarSlot;
        this.lastHotbarSlot = this.playerHotbarSlot;
      } 
      if (this.isSneaking) {
        mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        this.isSneaking = false;
      } 
    } 
  }
  
  private boolean placeBlockInRange(BlockPos pos) {
    Block block = mc.world.getBlockState(pos).getBlock();
    if (!(block instanceof net.minecraft.block.BlockAir) && !(block instanceof net.minecraft.block.BlockLiquid))
      return false; 
    for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
      if (!(entity instanceof net.minecraft.entity.item.EntityItem) && !(entity instanceof net.minecraft.entity.item.EntityXPOrb))
        return false; 
    } 
    EnumFacing side = BlockUtils.getPlaceableSide(pos);
    if (side == null)
      return false; 
    BlockPos neighbour = pos.offset(side);
    EnumFacing opposite = side.getOpposite();
    if (!BlockUtils.canBeClicked(neighbour))
      return false; 
    Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
    Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
    int obiSlot = findObiInHotbar();
    if (obiSlot == -1)
      disable(); 
    if (this.lastHotbarSlot != obiSlot) {
      mc.player.inventory.currentItem = obiSlot;
      this.lastHotbarSlot = obiSlot;
    } 
    if ((!this.isSneaking && BlockUtils.blackList.contains(neighbourBlock)) || BlockUtils.shulkerList.contains(neighbourBlock)) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      this.isSneaking = true;
    } 
    if (this.rotate.getValue())
      BlockUtils.faceVectorPacketInstant(hitVec); 
    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    mc.player.swingArm(EnumHand.MAIN_HAND);
    mc.rightClickDelayTimer = 4;
    return true;
  }
  
  private int findObiInHotbar() {
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
  
  private void findClosestTarget() {
    List<EntityPlayer> playerList = mc.world.playerEntities;
    this.closestTarget = null;
    for (EntityPlayer target : playerList) {
      if (target == mc.player)
        this.closestTarget = target; 
    } 
  }
  
  private static class Offsets {
    private static final Vec3d[] TRAP = new Vec3d[] { 
        new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(1.0D, 2.0D, 0.0D), 
        new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 3.0D, -1.0D), new Vec3d(0.0D, 3.0D, 0.0D) };
    
    private static final Vec3d[] TRAPFULLROOF = new Vec3d[] { 
        new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 2.0D, -1.0D), new Vec3d(1.0D, 2.0D, 0.0D), 
        new Vec3d(0.0D, 2.0D, 1.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(0.0D, 3.0D, -1.0D), new Vec3d(0.0D, 3.0D, 0.0D), new Vec3d(0.0D, 4.0D, 0.0D) };
    
    private static final Vec3d[] TRAPSIMPLE = new Vec3d[] { 
        new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(1.0D, 1.0D, 0.0D), new Vec3d(0.0D, 1.0D, -1.0D), new Vec3d(0.0D, 1.0D, 1.0D), new Vec3d(-1.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 2.0D, 0.0D), new Vec3d(-1.0D, 3.0D, 0.0D), 
        new Vec3d(0.0D, 3.0D, 0.0D) };
  }
}
