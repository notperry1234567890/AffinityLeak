//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;

public class NoEntityTrace extends Module {
  Setting.Boolean pickaxeOnly;
  
  boolean isHoldingPickaxe = false;
  
  public NoEntityTrace() {
    super("NoEntityTrace", Module.Category.Misc);
  }
  
  public void setup() {
    this.pickaxeOnly = registerBoolean("Pickaxe Only", "PickaxeOnly", true);
  }
  
  public void onUpdate() {
    this.isHoldingPickaxe = mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemPickaxe;
  }
  
  public boolean noTrace() {
    if (this.pickaxeOnly.getValue())
      return (isEnabled() && this.isHoldingPickaxe); 
    return isEnabled();
  }
}
