//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class SmartOffhand extends Module {
  Setting.Mode switch_mode = registerMode("Mode", "OffhandOffhand", Arrays.asList(new String[] { "Totem", "Crystal", "Gapple" }, ), "Totem");
  
  Setting.Integer totem_switch = registerInteger("Min Offhand HP", "OffhandTotemHP", 16, 0, 36);
  
  Setting.Boolean gapple_in_hole = registerBoolean("Gapple In Hole", "OffhandGapple", true);
  
  Setting.Integer gapple_hole_hp = registerInteger("Gapple Hole HP", "OffhandGappleHP", 8, 0, 36);
  
  Setting.Boolean delay = registerBoolean("Delay", "OffhandDelay", false);
  
  private boolean switching = false;
  
  private int last_slot;
  
  public SmartOffhand() {
    super("Smart Offhand", Module.Category.Combat);
  }
  
  public void onUpdate() {
    if (mc.currentScreen == null || mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory) {
      if (this.switching) {
        swap_items(this.last_slot, 2);
        return;
      } 
      float hp = mc.player.getHealth() + mc.player.getAbsorptionAmount();
      if (hp > this.totem_switch.getValue()) {
        if (this.switch_mode.getValue().equals("Crystal") && ModuleManager.isModuleEnabled("AutoCrystal")) {
          swap_items(get_item_slot(Items.END_CRYSTAL), 0);
          return;
        } 
        if (this.gapple_in_hole.getValue() && hp > this.gapple_hole_hp.getValue() && is_in_hole()) {
          swap_items(get_item_slot(Items.GOLDEN_APPLE), this.delay.getValue() ? 1 : 0);
          return;
        } 
        if (this.switch_mode.getValue().equals("Totem")) {
          swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), this.delay.getValue() ? 1 : 0);
          return;
        } 
        if (this.switch_mode.getValue().equals("Gapple")) {
          swap_items(get_item_slot(Items.GOLDEN_APPLE), this.delay.getValue() ? 1 : 0);
          return;
        } 
        if (this.switch_mode.getValue().equals("Crystal") && !ModuleManager.isModuleEnabled("AutoCrystal")) {
          swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), 0);
          return;
        } 
      } else {
        swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), this.delay.getValue() ? 1 : 0);
        return;
      } 
      if (mc.player.getHeldItemOffhand().getItem() == Items.AIR)
        swap_items(get_item_slot(Items.TOTEM_OF_UNDYING), this.delay.getValue() ? 1 : 0); 
    } 
  }
  
  public void swap_items(int slot, int step) {
    if (slot == -1)
      return; 
    if (step == 0) {
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
    } 
    if (step == 1) {
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      this.switching = true;
      this.last_slot = slot;
    } 
    if (step == 2) {
      mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      this.switching = false;
    } 
    mc.playerController.updateController();
  }
  
  private boolean is_in_hole() {
    BlockPos player_block = EntityUtil.GetLocalPlayerPosFloored();
    return (mc.world.getBlockState(player_block.east()).getBlock() != Blocks.AIR && mc.world
      .getBlockState(player_block.west()).getBlock() != Blocks.AIR && mc.world
      .getBlockState(player_block.north()).getBlock() != Blocks.AIR && mc.world
      .getBlockState(player_block.south()).getBlock() != Blocks.AIR);
  }
  
  private int get_item_slot(Item input) {
    if (input == mc.player.getHeldItemOffhand().getItem())
      return -1; 
    for (int i = 36; i >= 0; i--) {
      Item item = mc.player.inventory.getStackInSlot(i).getItem();
      if (item == input) {
        if (i < 9) {
          if (input == Items.GOLDEN_APPLE)
            return -1; 
          i += 36;
        } 
        return i;
      } 
    } 
    return -1;
  }
}
