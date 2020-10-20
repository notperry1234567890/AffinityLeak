//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.event.events.JumpEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.MotionUtils;
import com.gamesense.client.module.Module;
import java.util.ArrayList;

public class Sprint extends Module {
  Setting.Mode Mode;
  
  public Sprint() {
    super("Sprint", Module.Category.Movement);
  }
  
  public void setup() {
    ArrayList<String> sprintModes = new ArrayList<>();
    sprintModes.add("Legit");
    sprintModes.add("Rage");
    this.Mode = registerMode("Mode", "Mode", sprintModes, "Legit");
  }
  
  public void onUpdate() {
    if (mc.gameSettings.keyBindSneak.isKeyDown()) {
      mc.player.setSprinting(false);
      return;
    } 
    if ((mc.player.getFoodStats().getFoodLevel() > 6) ? (this.Mode.getValue().equalsIgnoreCase("Rage") ? (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) : (mc.player.moveForward > 0.0F)) : (mc.player.moveForward > 0.0F))
      mc.player.setSprinting(true); 
  }
  
  public void onJump(JumpEvent event) {
    if (this.Mode.getValue().equalsIgnoreCase("Rage")) {
      double[] dir = MotionUtils.forward(0.01745329238474369D);
      event.getLocation().setX(dir[0] * 0.20000000298023224D);
      event.getLocation().setZ(dir[1] * 0.20000000298023224D);
    } 
  }
}
