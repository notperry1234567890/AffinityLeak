//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.client.module.Module;
import java.util.Random;

public class SkiddaOnBottomDupe extends Module {
  public SkiddaOnBottomDupe() {
    super("SkiddaOnBottomDupe", Module.Category.Misc);
  }
  
  public void onEnable() {
    if (mc.player != null)
      mc.player.sendChatMessage("I just used the Skidda On Bottom Dupe and got " + ((new Random()).nextInt(31) + 1) + " shulkers thanks to Affinity+!"); 
    disable();
  }
}
