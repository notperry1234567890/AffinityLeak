//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ChatModifier extends Module {
  public Setting.Boolean clearBkg;
  
  Setting.Boolean chattimestamps;
  
  Setting.Mode format;
  
  Setting.Mode color;
  
  Setting.Mode decoration;
  
  Setting.Boolean space;
  
  @EventHandler
  private final Listener<ClientChatReceivedEvent> chatReceivedEventListener;
  
  Setting.Boolean greentext;
  
  @EventHandler
  private final Listener<PacketEvent.Send> listener;
  
  public ChatModifier() {
    super("ChatModifier", Module.Category.Misc);
    this.chatReceivedEventListener = new Listener(event -> {
          if (this.chattimestamps.getValue()) {
            String decoLeft = this.decoration.getValue().equalsIgnoreCase(" ") ? "" : this.decoration.getValue().split(" ")[0];
            String decoRight = this.decoration.getValue().equalsIgnoreCase(" ") ? "" : this.decoration.getValue().split(" ")[1];
            if (this.space.getValue())
              decoRight = decoRight + " "; 
            String dateFormat = this.format.getValue().replace("H24", "k").replace("H12", "h");
            String date = (new SimpleDateFormat(dateFormat)).format(new Date());
            TextComponentString time = new TextComponentString(ChatFormatting.getByName(this.color.getValue()) + decoLeft + date + decoRight + ChatFormatting.RESET);
            event.setMessage(time.appendSibling(event.getMessage()));
          } 
        }new java.util.function.Predicate[0]);
    this.listener = new Listener(event -> {
          if (this.greentext.getValue() && event.getPacket() instanceof CPacketChatMessage) {
            if (((CPacketChatMessage)event.getPacket()).getMessage().startsWith("/") || ((CPacketChatMessage)event.getPacket()).getMessage().startsWith(Command.getPrefix()))
              return; 
            String message = ((CPacketChatMessage)event.getPacket()).getMessage();
            String prefix = "";
            prefix = ">";
            String s = prefix + message;
            if (s.length() > 255)
              return; 
            ((CPacketChatMessage)event.getPacket()).message = s;
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    ArrayList<String> formats = new ArrayList<>();
    formats.add("H24:mm");
    formats.add("H12:mm");
    formats.add("H12:mm a");
    formats.add("H24:mm:ss");
    formats.add("H12:mm:ss");
    formats.add("H12:mm:ss a");
    ArrayList<String> deco = new ArrayList<>();
    deco.add("< >");
    deco.add("[ ]");
    deco.add("{}");
    deco.add(" ");
    ArrayList<String> colors = new ArrayList<>();
    for (ChatFormatting cf : ChatFormatting.values())
      colors.add(cf.getName()); 
    this.clearBkg = registerBoolean("Clear Chat", "ClearChat", false);
    this.greentext = registerBoolean("Green Text", "GreenText", false);
    this.chattimestamps = registerBoolean("Chat Time Stamps", "ChatTimeStamps", false);
    this.format = registerMode("Format", "Format", formats, "H24:mm");
    this.decoration = registerMode("Deco", "Deco", deco, "[ ]");
    this.color = registerMode("Color", "Colors", colors, ChatFormatting.GRAY.getName());
    this.space = registerBoolean("Space", "Space", false);
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
