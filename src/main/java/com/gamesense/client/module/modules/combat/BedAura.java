//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.BlockInteractionHelper;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BedAura extends Module {
  private static final DecimalFormat df = new DecimalFormat("#.#");
  
  private final Setting.Boolean rotate = registerBoolean("Rotate", "Rotate", false);
  
  private final Setting.Boolean debugMessages = registerBoolean("Debug Messages", "DebugMessages", false);
  
  private final Setting.Boolean chainBomb = registerBoolean("Chain Bomb", "ChainBomb", true);
  
  private final Setting.Boolean fastMode = registerBoolean("Fast Mode", "FastMode", true);
  
  private final Setting.Integer tickDelay = registerInteger("Tick Delay", "TickDelay", 5, 0, 40);
  
  private BlockPos placeTarget;
  
  private int bedSlot;
  
  private int ticksWaited = 0;
  
  private int normalTickDelay;
  
  private boolean isSneaking;
  
  private boolean firstRun;
  
  public BedAura() {
    super("Bed Aura", Module.Category.Combat);
  }
  
  protected void onEnable() {
    if (mc.player == null || ModuleManager.isModuleEnabled("Freecam")) {
      disable();
      return;
    } 
    this.normalTickDelay = mc.rightClickDelayTimer;
    df.setRoundingMode(RoundingMode.CEILING);
    this.placeTarget = null;
    this.bedSlot = -1;
    for (int i = 0; i < 9; i++) {
      if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED)
        this.bedSlot = i; 
    } 
    this.isSneaking = false;
    if (this.bedSlot == -1) {
      if (this.debugMessages.getValue())
        Command.sendClientMessage("BedAura: Bed(s) missing, disabling."); 
      disable();
      return;
    } 
    if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null || mc.objectMouseOver.getBlockPos().up() == null) {
      if (this.debugMessages.getValue())
        Command.sendClientMessage("BedAura: Not a valid place target, disabling."); 
      disable();
      return;
    } 
    this.placeTarget = mc.objectMouseOver.getBlockPos().up();
    this.firstRun = true;
  }
  
  public void onUpdate() {
    if (mc.player == null || mc.world == null)
      return; 
    if (ModuleManager.isModuleEnabled("Freecam"))
      return; 
    if (!this.firstRun && this.tickDelay.getValue() > this.ticksWaited) {
      this.ticksWaited++;
      return;
    } 
    if (this.firstRun)
      this.firstRun = false; 
    this.ticksWaited = 0;
    mc.player.inventory.currentItem = this.bedSlot;
    if (this.fastMode.getValue()) {
      if (mc.player.getHeldItemMainhand().getItem() == Items.BED)
        mc.rightClickDelayTimer = 0; 
    } else {
      mc.rightClickDelayTimer = this.normalTickDelay;
    } 
    placeBlock(new BlockPos((Vec3i)this.placeTarget), EnumFacing.DOWN);
    mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
    this.isSneaking = false;
    mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.placeTarget.add(0, 0, 0), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
    if (!this.chainBomb.getValue()) {
      disable();
      return;
    } 
    int nextBed = findBedSlot();
    if (nextBed == -1) {
      Command.sendClientMessage("BedAura: Bed(s) missing, disabling.");
      disable();
    } else {
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, nextBed, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, (this.bedSlot < 9) ? (this.bedSlot + 36) : this.bedSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.windowClick(mc.player.inventoryContainer.windowId, nextBed, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.updateController();
    } 
  }
  
  private void placeBlock(BlockPos pos, EnumFacing side) {
    BlockPos neighbour = pos.offset(side);
    EnumFacing opposite = side.getOpposite();
    if (!this.isSneaking) {
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      this.isSneaking = true;
    } 
    Vec3d hitVec = new Vec3d((Vec3i)neighbour.add(0.5D, 0.5D, 0.5D).add(opposite.getDirectionVec()));
    if (this.rotate.getValue())
      BlockInteractionHelper.faceVectorPacketInstant(hitVec); 
    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
    mc.player.swingArm(EnumHand.MAIN_HAND);
  }
  
  private int findBedSlot() {
    for (int i = 9; i < 36; i++) {
      if (mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemBed)
        return i; 
    } 
    return -1;
  }
}
