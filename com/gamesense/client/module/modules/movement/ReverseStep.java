//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import net.minecraft.entity.Entity;

public class ReverseStep extends Module {
  Setting.Double height;
  
  public ReverseStep() {
    super("ReverseStep", Module.Category.Movement);
  }
  
  public void setup() {
    this.height = registerDouble("Height", "Height", 2.5D, 0.5D, 2.5D);
  }
  
  public void onUpdate() {
    if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump
      .isKeyDown())
      return; 
    if (ModuleManager.isModuleEnabled("Speed"))
      return; 
    if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder())
      for (double y = 0.0D; y < this.height.getValue() + 0.5D; y += 0.01D) {
        if (!mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -y, 0.0D)).isEmpty()) {
          mc.player.motionY = -10.0D;
          break;
        } 
      }  
  }
}
