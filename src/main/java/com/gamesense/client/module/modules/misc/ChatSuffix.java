//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatSuffix extends Module {
  Setting.Mode Separator;
  
  @EventHandler
  private final Listener<PacketEvent.Send> listener;
  
  public ChatSuffix() {
    super("ChatSuffix", Module.Category.Misc);
    this.listener = new Listener(event -> {
          if (event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage)event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage)event.getPacket()).getMessage().startsWith(Command.getPrefix()))
              return; 
            String Separator2 = null;
            if (this.Separator.getValue().equalsIgnoreCase(">>"))
              Separator2 = " 》"; 
            if (this.Separator.getValue().equalsIgnoreCase("<<")) {
              Separator2 = " 《";
            } else if (this.Separator.getValue().equalsIgnoreCase("|")) {
              Separator2 = " �?? ";
            } 
            String old = ((CPacketChatMessage)event.getPacket()).getMessage();
            String suffix = Separator2 + toUnicode("Affinity+");
            String s = old + suffix;
            if (s.length() > 255)
              return; 
            ((CPacketChatMessage)event.getPacket()).message = s;
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    ArrayList<String> Separators = new ArrayList<>();
    Separators.add(">>");
    Separators.add("<<");
    Separators.add("|");
    this.Separator = registerMode("Separator", "Separator", Separators, "|");
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
  
  public String toUnicode(String s) {
    return s.toLowerCase()
      .replace("a", "ᴀ")
      .replace("b", "ʙ")
      .replace("c", "ᴄ")
      .replace("d", "ᴅ")
      .replace("e", "ᴇ")
      .replace("f", "ꜰ")
      .replace("g", "ɢ")
      .replace("h", "ʜ")
      .replace("i", "ɪ")
      .replace("j", "ᴊ")
      .replace("k", "ᴋ")
      .replace("l", "ʟ")
      .replace("m", "�?")
      .replace("n", "ɴ")
      .replace("o", "�?")
      .replace("p", "ᴘ")
      .replace("q", "ǫ")
      .replace("r", "ʀ")
      .replace("s", "ꜱ")
      .replace("t", "ᴛ")
      .replace("u", "ᴜ")
      .replace("v", "ᴠ")
      .replace("w", "ᴡ")
      .replace("x", "ˣ")
      .replace("y", "�?")
      .replace("z", "ᴢ");
  }
}
