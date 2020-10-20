//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class AutoReply extends Module {
  private static String reply = "I don't speak to newfags!";
  
  @EventHandler
  private final Listener<ClientChatReceivedEvent> listener;
  
  public AutoReply() {
    super("AutoReply", Module.Category.Misc);
    this.listener = new Listener(event -> {
          if (event.getMessage().getUnformattedText().contains("whispers: ") && !event.getMessage().getUnformattedText().startsWith(mc.player.getName())) {
            mc.player.sendChatMessage("/r " + reply);
          } else if (event.getMessage().getUnformattedText().contains("whispers: I don't speak to newfags!") && !event.getMessage().getUnformattedText().startsWith(mc.player.getName())) {
            return;
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public static String getReply() {
    return reply;
  }
  
  public static void setReply(String r) {
    reply = r;
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
