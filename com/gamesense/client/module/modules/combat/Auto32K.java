//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Auto32K extends Module {
  private static final DecimalFormat df = new DecimalFormat("#.#");
  
  private final Setting.Boolean rotate = registerBoolean("Rotate", "Rotate", false);
  
  private final Setting.Boolean grabItem = registerBoolean("Grab Item", "Grab Item", false);
  
  private final Setting.Boolean autoEnableHitAura = registerBoolean("Auto enable Hit Aura", "Auto enable Hit Aura", false);
  
  private final Setting.Boolean debugMessages = registerBoolean("Debug Messages", "Debug Messages", false);
  
  private int stage;
  
  private BlockPos placeTarget;
  
  private int obiSlot;
  
  private int dispenserSlot;
  
  private int shulkerSlot;
  
  private int redstoneSlot;
  
  private int hopperSlot;
  
  private boolean isSneaking;
  
  public Auto32K() {
    super("Auto32K", Module.Category.Combat);
  }
  
  protected void onEnable() {
    if (mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
      disable();
      return;
    } 
    df.setRoundingMode(RoundingMode.CEILING);
    this.stage = 0;
    this.placeTarget = null;
    this.obiSlot = -1;
    this.dispenserSlot = -1;
    this.shulkerSlot = -1;
    this.redstoneSlot = -1;
    this.hopperSlot = -1;
    this.isSneaking = false;
    for (int i = 0; i < 9 && (this.obiSlot == -1 || this.dispenserSlot == -1 || this.shulkerSlot == -1 || this.redstoneSlot == -1 || this.hopperSlot == -1); i++) {
      ItemStack stack = mc.player.inventory.getStackInSlot(i);
      if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {
        Block block = ((ItemBlock)stack.getItem()).getBlock();
        if (block == Blocks.HOPPER) {
          this.hopperSlot = i;
        } else if (BlockUtils.shulkerList.contains(block)) {
          this.shulkerSlot = i;
        } else if (block == Blocks.OBSIDIAN) {
          this.obiSlot = i;
        } else if (block == Blocks.DISPENSER) {
          this.dispenserSlot = i;
        } else if (block == Blocks.REDSTONE_BLOCK) {
          this.redstoneSlot = i;
        } 
      } 
    } 
    if (this.obiSlot == -1 || this.dispenserSlot == -1 || this.shulkerSlot == -1 || this.redstoneSlot == -1 || this.hopperSlot == -1) {
      if (this.debugMessages.getValue())
        Command.sendClientMessage("Auto32k: Items missing, disabling."); 
      disable();
      return;
    } 
    if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.objectMouseOver.getBlockPos().up() == null) {
      if (this.debugMessages.getValue())
        Command.sendClientMessage("Auto32k: Not a valid place target, disabling."); 
      disable();
      return;
    } 
    this.placeTarget = mc.objectMouseOver.getBlockPos().up();
    if (!this.debugMessages.getValue())
      return; 
    Command.sendClientMessage("Auto32k: Place Target is " + this.placeTarget.x + " " + this.placeTarget.y + " " + this.placeTarget.z + " Distance: " + df.format(mc.player.getPositionVector().distanceTo(new Vec3d((Vec3i)this.placeTarget))));
  }
  
  public void onUpdate() {
    if (mc.player == null)
      return; 
    if (ModuleManager.isModuleEnabled("Freecam"))
      return; 
    if (this.stage == 0) {
      mc.player.inventory.currentItem = this.obiSlot;
      placeBlock(new BlockPos((Vec3i)this.placeTarget), EnumFacing.DOWN);
      mc.player.inventory.currentItem = this.dispenserSlot;
      placeBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 1, 0)), EnumFacing.DOWN);
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      this.isSneaking = false;
      mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.placeTarget.add(0, 1, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
      this.stage = 1;
      return;
    } 
    if (this.stage == 1) {
      if (!(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer))
        return; 
      mc.playerController.windowClick(mc.player.openContainer.windowId, 1, this.shulkerSlot, ClickType.SWAP, (EntityPlayer)mc.player);
      mc.player.closeScreen();
      mc.player.inventory.currentItem = this.redstoneSlot;
      placeBlock(new BlockPos((Vec3i)this.placeTarget.add(0, 2, 0)), EnumFacing.DOWN);
      this.stage = 2;
      return;
    } 
    if (this.stage == 2) {
      Block block = mc.world.getBlockState(this.placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()).up()).getBlock();
      if (block instanceof net.minecraft.block.BlockAir)
        return; 
      if (block instanceof net.minecraft.block.BlockLiquid)
        return; 
      mc.player.inventory.currentItem = this.hopperSlot;
      placeBlock(new BlockPos((Vec3i)this.placeTarget.offset(mc.player.getHorizontalFacing().getOpposite())), mc.player.getHorizontalFacing());
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      this.isSneaking = false;
      mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.placeTarget.offset(mc.player.getHorizontalFacing().getOpposite()), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
      mc.player.inventory.currentItem = this.shulkerSlot;
      if (!this.grabItem.getValue()) {
        disable();
        return;
      } 
      this.stage = 3;
      return;
    } 
    if (this.stage != 3)
      return; 
    if (!(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer))
      return; 
    mc.playerController.updateController();
    mc.playerController.windowClick(mc.player.openContainer.windowId, 0, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
    mc.playerController.windowClick(mc.player.openContainer.windowId, this.shulkerSlot + 4, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
    if (this.autoEnableHitAura.getValue()) {
      ModuleManager.getModuleByName("Bruce Aura").enable();
      ModuleManager.getModuleByName("YakgodAura").enable();
    } 
    disable();
  }
  
  private void placeBlock(BlockPos pos, EnumFacing side) {
    BlockPos neighbour = pos.offset(side);
    EnumFacing opposite = side.getOpposite();
    if (!this.isSneaking) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      this.isSneaking = true;
    } 
    Vec3d hitVec = (new Vec3d((Vec3i)neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
    if (this.rotate.getValue())
      BlockUtils.faceVectorPacketInstant(hitVec); 
    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    mc.player.swingArm(EnumHand.MAIN_HAND);
  }
}
