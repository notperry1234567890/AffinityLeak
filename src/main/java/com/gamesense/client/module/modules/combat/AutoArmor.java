//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.client.module.Module;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmor extends Module {
  public AutoArmor() {
    super("AutoArmor", Module.Category.Combat);
  }
  
  public void onUpdate() {
    if (mc.player.ticksExisted % 2 == 0)
      return; 
    if (mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainer && !(mc.currentScreen instanceof net.minecraft.client.renderer.InventoryEffectRenderer))
      return; 
    int[] bestArmorSlots = new int[4];
    int[] bestArmorValues = new int[4];
    for (int i = 0; i < 4; i++) {
      ItemStack oldArmor = mc.player.inventory.armorItemInSlot(i);
      if (oldArmor != null && oldArmor.getItem() instanceof ItemArmor)
        bestArmorValues[i] = ((ItemArmor)oldArmor
          .getItem()).damageReduceAmount; 
      bestArmorSlots[i] = -1;
    } 
    for (int slot = 0; slot < 36; slot++) {
      ItemStack stack = mc.player.inventory.getStackInSlot(slot);
      if (stack.getCount() <= 1)
        if (stack != null && stack.getItem() instanceof ItemArmor) {
          ItemArmor armor = (ItemArmor)stack.getItem();
          int j = armor.armorType.ordinal() - 2;
          if (j != 2 || !mc.player.inventory.armorItemInSlot(j).getItem().equals(Items.ELYTRA)) {
            int armorValue = armor.damageReduceAmount;
            if (armorValue > bestArmorValues[j]) {
              bestArmorSlots[j] = slot;
              bestArmorValues[j] = armorValue;
            } 
          } 
        }  
    } 
    for (int armorType = 0; armorType < 4; armorType++) {
      int j = bestArmorSlots[armorType];
      if (j != -1) {
        ItemStack oldArmor = mc.player.inventory.armorItemInSlot(armorType);
        if (oldArmor == null || oldArmor != ItemStack.EMPTY || mc.player.inventory
          .getFirstEmptyStack() != -1) {
          if (j < 9)
            j += 36; 
          mc.playerController.windowClick(0, 8 - armorType, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
          mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, (EntityPlayer)mc.player);
          break;
        } 
      } 
    } 
  }
}
