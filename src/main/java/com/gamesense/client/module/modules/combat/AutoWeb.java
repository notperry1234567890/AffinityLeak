//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.Wrapper;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.client.module.Module;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class AutoWeb extends Module {
  private final Vec3d[] offsetList;
  
  Setting.Boolean rotate;
  
  Setting.Double range;
  
  Setting.Integer bpt;
  
  Setting.Boolean spoofRotations;
  
  Setting.Boolean spoofHotbar;
  
  private boolean slowModeSwitch;
  
  private int playerHotbarSlot;
  
  private EntityPlayer closestTarget;
  
  private int lastHotbarSlot;
  
  private int offsetStep;
  
  public AutoWeb() {
    super("AutoWeb", Module.Category.Combat);
    this.offsetList = new Vec3d[] { new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(0.0D, 0.0D, 0.0D) };
    this.slowModeSwitch = false;
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
    this.offsetStep = 0;
  }
  
  public void setup() {
    this.rotate = registerBoolean("Rotate", "Rotate", false);
    this.range = registerDouble("Range", "Range", 4.0D, 0.0D, 6.0D);
    this.bpt = registerInteger("Blocks Per Tick", "BlocksPerTick", 8, 1, 15);
    this.spoofRotations = registerBoolean("Spoof Rotations", "SpoofRotations", false);
    this.spoofHotbar = registerBoolean("Silent Switch", "SilentSwitch", false);
  }
  
  public void onUpdate() {
    if (this.closestTarget == null)
      return; 
    if (this.slowModeSwitch) {
      this.slowModeSwitch = false;
      return;
    } 
    for (int i = 0; i < (int)Math.floor(this.bpt.getValue()); i++) {
      if (this.offsetStep >= this.offsetList.length) {
        endLoop();
        return;
      } 
      Vec3d offset = this.offsetList[this.offsetStep];
      placeBlock((new BlockPos(this.closestTarget.getPositionVector())).down().add(offset.x, offset.y, offset.z));
      this.offsetStep++;
    } 
    this.slowModeSwitch = true;
  }
  
  private void placeBlock(BlockPos blockPos) {
    if (!Wrapper.getWorld().getBlockState(blockPos).getMaterial().isReplaceable())
      return; 
    if (!BlockUtils.checkForNeighbours(blockPos))
      return; 
    placeBlockExecute(blockPos);
  }
  
  public void placeBlockExecute(BlockPos pos) {
    Vec3d eyesPos = new Vec3d((Wrapper.getPlayer()).posX, (Wrapper.getPlayer()).posY + Wrapper.getPlayer().getEyeHeight(), (Wrapper.getPlayer()).posZ);
    EnumFacing[] values = EnumFacing.values();
    int length = values.length;
    int n = 0;
    if (0 >= length)
      return; 
    EnumFacing side = values[0];
    BlockPos neighbor = pos.offset(side);
    EnumFacing side2 = side.getOpposite();
    Vec3d hitVec = (new Vec3d((Vec3i)neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
    if (this.spoofRotations.getValue())
      BlockUtils.faceVectorPacketInstant(hitVec); 
    boolean needSneak = false;
    Block blockBelow = mc.world.getBlockState(neighbor).getBlock();
    if (BlockUtils.blackList.contains(blockBelow) || BlockUtils.shulkerList.contains(blockBelow))
      needSneak = true; 
    if (needSneak)
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING)); 
    int obiSlot = findWebInHotBar();
    if (obiSlot == -1) {
      disable();
      return;
    } 
    if (this.lastHotbarSlot != obiSlot) {
      if (this.spoofHotbar.getValue()) {
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(obiSlot));
      } else {
        (Wrapper.getPlayer()).inventory.currentItem = obiSlot;
      } 
      this.lastHotbarSlot = obiSlot;
    } 
    mc.playerController.processRightClickBlock(Wrapper.getPlayer(), mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
    mc.player.connection.sendPacket((Packet)new CPacketAnimation(EnumHand.MAIN_HAND));
    if (needSneak)
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING)); 
  }
  
  private int findWebInHotBar() {
    int slot = -1;
    for (int i = 0; i < 9; i++) {
      ItemStack stack = (Wrapper.getPlayer()).inventory.getStackInSlot(i);
      if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
        Block block = ((ItemBlock)stack.getItem()).getBlock();
        if (block instanceof net.minecraft.block.BlockWeb) {
          slot = i;
          break;
        } 
      } 
    } 
    return slot;
  }
  
  private void findTarget() {
    List<EntityPlayer> playerList = (Wrapper.getWorld()).playerEntities;
    for (EntityPlayer target : playerList) {
      if (target == mc.player)
        continue; 
      if (Friends.isFriend(target.getName()))
        continue; 
      if (!EntityUtil.isLiving((Entity)target))
        continue; 
      if (target.getHealth() <= 0.0F)
        continue; 
      double currentDistance = Wrapper.getPlayer().getDistance((Entity)target);
      if (currentDistance > this.range.getValue())
        continue; 
      if (this.closestTarget == null) {
        this.closestTarget = target;
        continue;
      } 
      if (currentDistance >= Wrapper.getPlayer().getDistance((Entity)this.closestTarget))
        continue; 
      this.closestTarget = target;
    } 
  }
  
  private void endLoop() {
    this.offsetStep = 0;
    if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1) {
      (Wrapper.getPlayer()).inventory.currentItem = this.playerHotbarSlot;
      this.lastHotbarSlot = this.playerHotbarSlot;
    } 
    findTarget();
  }
  
  protected void onEnable() {
    if (mc.player == null) {
      disable();
      return;
    } 
    this.playerHotbarSlot = (Wrapper.getPlayer()).inventory.currentItem;
    this.lastHotbarSlot = -1;
    findTarget();
  }
  
  protected void onDisable() {
    if (mc.player == null)
      return; 
    if (this.lastHotbarSlot != this.playerHotbarSlot && this.playerHotbarSlot != -1)
      if (this.spoofHotbar.getValue()) {
        mc.player.connection.sendPacket((Packet)new CPacketHeldItemChange(this.playerHotbarSlot));
      } else {
        (Wrapper.getPlayer()).inventory.currentItem = this.playerHotbarSlot;
      }  
    this.playerHotbarSlot = -1;
    this.lastHotbarSlot = -1;
  }
}
