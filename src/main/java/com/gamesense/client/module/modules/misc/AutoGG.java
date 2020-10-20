//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class AutoGG extends Module {
  public static AutoGG INSTANCE;
  
  static List<String> AutoGgMessages = new ArrayList<>();
  
  public final Setting.Mode customMessage = registerMode("Custom Message", "customMessage", Arrays.asList(new String[] { "{name} ez'd by Affinity+!", "Hey, {name}! you just got fucked in the ass by Affinity+!", "A no name, also known as {name} just got shit on by Affinity+!", "You just got cucked by the one and only Affinity+, {name}!", "EZZZZZZZZZZZZZZZZZZZZZZZZZZ", "RANDOM" }, ), "RANDOM");
  
  public final Setting.Boolean greenText = registerBoolean("Green Text", "greenText", true);
  
  int index = -1;
  
  private ConcurrentHashMap targetedPlayers = null;
  
  @EventHandler
  private final Listener<PacketEvent.Send> sendListener;
  
  @EventHandler
  private final Listener<LivingDeathEvent> livingDeathEventListener;
  
  public AutoGG() {
    super("AutoGG", Module.Category.Misc);
    this.sendListener = new Listener(event -> {
          if (mc.player != null) {
            if (this.targetedPlayers == null)
              this.targetedPlayers = new ConcurrentHashMap<>(); 
            if (event.getPacket() instanceof CPacketUseEntity) {
              CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
              if (cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK)) {
                Entity targetEntity = cPacketUseEntity.getEntityFromWorld((World)mc.world);
                if (targetEntity instanceof EntityPlayer)
                  addTargetedPlayer(targetEntity.getName()); 
              } 
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.livingDeathEventListener = new Listener(event -> {
          if (mc.player != null) {
            if (this.targetedPlayers == null)
              this.targetedPlayers = new ConcurrentHashMap<>(); 
            EntityLivingBase entity = event.getEntityLiving();
            if (entity != null && entity instanceof EntityPlayer) {
              EntityPlayer player = (EntityPlayer)entity;
              if (player.getHealth() <= 0.0F) {
                String name = player.getName();
                if (shouldAnnounce(name))
                  doAnnounce(name); 
              } 
            } 
          } 
        }new java.util.function.Predicate[0]);
    INSTANCE = this;
  }
  
  public static void addAutoGgMessage(String s) {
    AutoGgMessages.add(s);
  }
  
  public static List<String> getAutoGgMessages() {
    return AutoGgMessages;
  }
  
  public void onEnable() {
    this.targetedPlayers = new ConcurrentHashMap<>();
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    this.targetedPlayers = null;
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
  
  public void onUpdate() {
    if (this.targetedPlayers == null)
      this.targetedPlayers = new ConcurrentHashMap<>(); 
    Iterator<Entity> var1 = mc.world.getLoadedEntityList().iterator();
    while (var1.hasNext()) {
      Entity entity = var1.next();
      if (entity instanceof EntityPlayer) {
        EntityPlayer player = (EntityPlayer)entity;
        if (player.getHealth() <= 0.0F) {
          String name = player.getName();
          if (shouldAnnounce(name)) {
            doAnnounce(name);
            break;
          } 
        } 
      } 
    } 
    this.targetedPlayers.forEach((namex, timeout) -> {
          if (((Integer)timeout).intValue() <= 0) {
            this.targetedPlayers.remove(namex);
          } else {
            this.targetedPlayers.put(namex, Integer.valueOf(((Integer)timeout).intValue() - 1));
          } 
        });
  }
  
  private boolean shouldAnnounce(String name) {
    return this.targetedPlayers.containsKey(name);
  }
  
  private void doAnnounce(String name) {
    this.targetedPlayers.remove(name);
    if (this.index >= AutoGgMessages.size() - 1)
      this.index = -1; 
    this.index++;
    String message = this.customMessage.getValue().equals("RANDOM") ? "" : this.customMessage.getValue();
    String messageSanitized = message.replaceAll("à¸¢à¸‡", "").replace("{name}", name);
    if (messageSanitized.length() > 255)
      messageSanitized = messageSanitized.substring(0, 255); 
    mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
  }
  
  public void addTargetedPlayer(String name) {
    if (!Objects.equals(name, mc.player.getName())) {
      if (this.targetedPlayers == null)
        this.targetedPlayers = new ConcurrentHashMap<>(); 
      this.targetedPlayers.put(name, Integer.valueOf(20));
    } 
  }
}
