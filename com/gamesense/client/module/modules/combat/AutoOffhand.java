//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class AutoOffhand extends Module {
  public final Setting.Double health = registerDouble("Health", "HP", 16.0D, 0.0D, 36.0D);
  
  public final Setting.Mode Mode = registerMode("Mode", "Mode", Arrays.asList(new String[] { "Totem", "Gap", "Crystal", "Pearl", "Chorus", "Strength" }, ), "Totem");
  
  public final Setting.Mode FallbackMode = registerMode("Fallback", "FallbackMode", Arrays.asList(new String[] { "Totem", "Gap", "Crystal", "Pearl", "Chorus", "Strength" }, ), "Crystal");
  
  public final Setting.Double FallDistance = registerDouble("FallDistance", "Fall", 15.0D, 0.0D, 100.0D);
  
  public final Setting.Boolean TotemOnElytra = registerBoolean("TotemOnElytra", "Elytra", true);
  
  public final Setting.Boolean OffhandGapOnSword = registerBoolean("SwordGap", "SwordGap", false);
  
  public final Setting.Boolean OffhandStrNoStrSword = registerBoolean("StrGap", "Strength", false);
  
  public final Setting.Boolean HotbarFirst = registerBoolean("HotbarFirst", "Recursive", false);
  
  public final Setting.Boolean stopInGUI = registerBoolean("StopInContainers", "stopInGUI", false);
  
  public AutoOffhand() {
    super("AutoOffhand", Module.Category.Combat);
  }
  
  public static float GetHealthWithAbsorption() {
    return mc.player.getHealth() + mc.player.getAbsorptionAmount();
  }
  
  public static int GetItemSlot(Item input) {
    if (mc.player == null)
      return 0; 
    for (int i = 0; i < mc.player.inventoryContainer.getInventory().size(); i++) {
      if (i != 0 && i != 5 && i != 6 && i != 7 && i != 8) {
        ItemStack s = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
        if (!s.isEmpty())
          if (s.getItem() == input)
            return i;  
      } 
    } 
    return -1;
  }
  
  public static int GetRecursiveItemSlot(Item input) {
    if (mc.player == null)
      return 0; 
    for (int i = mc.player.inventoryContainer.getInventory().size() - 1; i > 0; i--) {
      if (i != 0 && i != 5 && i != 6 && i != 7 && i != 8) {
        ItemStack s = (ItemStack)mc.player.inventoryContainer.getInventory().get(i);
        if (!s.isEmpty())
          if (s.getItem() == input)
            return i;  
      } 
    } 
    return -1;
  }
  
  private void SwitchOffHandIfNeed(String p_Val) {
    Item l_Item = GetItemFromModeVal(p_Val);
    if (mc.player.getHeldItemOffhand().getItem() != l_Item) {
      int l_Slot = this.HotbarFirst.getValue() ? GetRecursiveItemSlot(l_Item) : GetItemSlot(l_Item);
      Item l_Fallback = GetItemFromModeVal(this.FallbackMode.getValue());
      String l_Display = GetItemNameFromModeVal(p_Val);
      if (l_Slot == -1 && l_Item != l_Fallback && mc.player.getHeldItemOffhand().getItem() != l_Fallback) {
        l_Slot = GetRecursiveItemSlot(l_Fallback);
        l_Display = GetItemNameFromModeVal(this.FallbackMode.getValue());
        if (l_Slot == -1 && l_Fallback != Items.TOTEM_OF_UNDYING) {
          l_Fallback = Items.TOTEM_OF_UNDYING;
          if (l_Item != l_Fallback && mc.player.getHeldItemOffhand().getItem() != l_Fallback) {
            l_Slot = GetRecursiveItemSlot(l_Fallback);
            l_Display = "Emergency Totem";
          } 
        } 
      } 
      if (l_Slot != -1)
        try {
          mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
          mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
          mc.playerController.windowClick(mc.player.inventoryContainer.windowId, l_Slot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
          mc.playerController.updateController();
        } catch (Exception exception) {} 
    } 
  }
  
  public void onUpdate() {
    if (this.stopInGUI.getValue() && 
      mc.currentScreen != null && !(mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory))
      return; 
    if (!mc.player.getHeldItemMainhand().isEmpty()) {
      if (this.health.getValue() <= GetHealthWithAbsorption() && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword && this.OffhandStrNoStrSword.getValue() && !mc.player.isPotionActive(MobEffects.STRENGTH)) {
        SwitchOffHandIfNeed("Strength");
        return;
      } 
      if (this.health.getValue() <= GetHealthWithAbsorption() && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword && this.OffhandGapOnSword.getValue()) {
        SwitchOffHandIfNeed("Gap");
        return;
      } 
    } 
    if (this.health.getValue() > GetHealthWithAbsorption() || this.Mode.getValue() == "Totem" || (this.TotemOnElytra.getValue() && mc.player.isElytraFlying()) || (mc.player.fallDistance >= this.FallDistance.getValue() && !mc.player.isElytraFlying())) {
      SwitchOffHandIfNeed("Totem");
      return;
    } 
    SwitchOffHandIfNeed(this.Mode.getValue());
  }
  
  public void onEnable() {
    super.onEnable();
  }
  
  public Item GetItemFromModeVal(String p_Val) {
    switch (p_Val) {
      case "Crystal":
        return Items.END_CRYSTAL;
      case "Gap":
        return Items.GOLDEN_APPLE;
      case "Pearl":
        return Items.ENDER_PEARL;
      case "Chorus":
        return Items.CHORUS_FRUIT;
      case "Strength":
        return (Item)Items.POTIONITEM;
    } 
    return Items.TOTEM_OF_UNDYING;
  }
  
  private String GetItemNameFromModeVal(String p_Val) {
    switch (p_Val) {
      case "Crystal":
        return "End Crystal";
      case "Gap":
        return "Gap";
      case "Pearl":
        return "Pearl";
      case "Chorus":
        return "Chorus";
      case "Strength":
        return "Strength";
    } 
    return "Totem";
  }
}
