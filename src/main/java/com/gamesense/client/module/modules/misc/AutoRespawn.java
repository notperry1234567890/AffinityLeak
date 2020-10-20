//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.GuiScreenDisplayedEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class AutoRespawn extends Module {
  Setting.Boolean coords;
  
  @EventHandler
  private final Listener<GuiScreenDisplayedEvent> listener;
  
  public AutoRespawn() {
    super("AutoRespawn", Module.Category.Misc);
    this.listener = new Listener(event -> {
          if (event.getScreen() instanceof net.minecraft.client.gui.GuiGameOver) {
            if (this.coords.getValue())
              Command.sendClientMessage(String.format("You died at x%d y%d z%d", new Object[] { Integer.valueOf((int)mc.player.posX), Integer.valueOf((int)mc.player.posY), Integer.valueOf((int)mc.player.posZ) })); 
            if (mc.player != null)
              mc.player.respawnPlayer(); 
            mc.displayGuiScreen(null);
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.coords = registerBoolean("Coords", "Coords", false);
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
