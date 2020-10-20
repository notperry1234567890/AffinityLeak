//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.TotemPopEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.world.World;

public class PvPInfo extends Module {
  public Set strengthedPlayers;
  
  public Set renderPlayers;
  
  @EventHandler
  public Listener<PacketEvent.Receive> totemPopListener;
  
  List<Entity> knownPlayers;
  
  List<Entity> antipearlspamplz;
  
  List<Entity> players;
  
  List<Entity> pearls;
  
  Setting.Boolean visualrange;
  
  Setting.Boolean pearlalert;
  
  Setting.Boolean popcounter;
  
  Setting.Boolean strengthdetect;
  
  Setting.Mode ChatColor;
  
  private HashMap<String, Integer> popList;
  
  @EventHandler
  public Listener<TotemPopEvent> totemPopEvent;
  
  public PvPInfo() {
    super("PvPInfo", Module.Category.Misc);
    this.totemPopListener = new Listener(event -> {
          if (mc.world == null || mc.player == null)
            return; 
          if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus)event.getPacket();
            if (packet.getOpCode() == 35) {
              Entity entity = packet.getEntity((World)mc.world);
              AffinityPlus.EVENT_BUS.post(new TotemPopEvent(entity));
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.knownPlayers = new ArrayList<>();
    this.antipearlspamplz = new ArrayList<>();
    this.popList = new HashMap<>();
    this.totemPopEvent = new Listener(event -> {
          if (this.popcounter.getValue()) {
            if (this.popList == null)
              this.popList = new HashMap<>(); 
            if (this.popList.get(event.getEntity().getName()) == null) {
              this.popList.put(event.getEntity().getName(), Integer.valueOf(1));
              Command.sendClientMessage(getTextColor() + event.getEntity().getName() + " popped " + ChatFormatting.RED + '\001' + getTextColor() + " totem!");
            } else if (this.popList.get(event.getEntity().getName()) != null) {
              int popCounter = ((Integer)this.popList.get(event.getEntity().getName())).intValue();
              int newPopCounter = ++popCounter;
              this.popList.put(event.getEntity().getName(), Integer.valueOf(newPopCounter));
              Command.sendClientMessage(getTextColor() + event.getEntity().getName() + " popped " + ChatFormatting.RED + newPopCounter + getTextColor() + " totems!");
            } 
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    ArrayList<String> colors = new ArrayList<>();
    colors.add("Black");
    colors.add("Dark Green");
    colors.add("Dark Red");
    colors.add("Gold");
    colors.add("Dark Gray");
    colors.add("Green");
    colors.add("Red");
    colors.add("Yellow");
    colors.add("Dark Blue");
    colors.add("Dark Aqua");
    colors.add("Dark Purple");
    colors.add("Gray");
    colors.add("Blue");
    colors.add("Aqua");
    colors.add("Light Purple");
    colors.add("White");
    this.visualrange = registerBoolean("Visual Range", "VisualRange", false);
    this.pearlalert = registerBoolean("Pearl Alert", "PearlAlert", false);
    this.popcounter = registerBoolean("Pop Counter", "PopCounter", false);
    this.strengthdetect = registerBoolean("Strength Detect", "StrengthDetect", false);
    this.ChatColor = registerMode("Color", "Color", colors, "Light Purple");
  }
  
  public void onUpdate() {
    if (this.visualrange.getValue()) {
      if (mc.player == null)
        return; 
      this.players = (List<Entity>)mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityPlayer).collect(Collectors.toList());
      try {
        for (Entity e : this.players) {
          if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName()) && 
            !this.knownPlayers.contains(e)) {
            this.knownPlayers.add(e);
            Command.sendClientMessage(getTextColor() + e.getName() + " has been spotted thanks to GameSense!");
          } 
        } 
      } catch (Exception exception) {}
      try {
        for (Entity e : this.knownPlayers) {
          if (e instanceof EntityPlayer && !e.getName().equalsIgnoreCase(mc.player.getName()) && 
            !this.players.contains(e))
            this.knownPlayers.remove(e); 
        } 
      } catch (Exception exception) {}
    } 
    if (this.pearlalert.getValue()) {
      this.pearls = (List<Entity>)mc.world.loadedEntityList.stream().filter(e -> e instanceof net.minecraft.entity.item.EntityEnderPearl).collect(Collectors.toList());
      try {
        for (Entity e : this.pearls) {
          if (e instanceof net.minecraft.entity.item.EntityEnderPearl && 
            !this.antipearlspamplz.contains(e)) {
            this.antipearlspamplz.add(e);
            Command.sendClientMessage(getTextColor() + e.getEntityWorld().getClosestPlayerToEntity(e, 3.0D).getName() + " has just thrown a pearl!");
          } 
        } 
      } catch (Exception exception) {}
    } 
    if (this.popcounter.getValue())
      for (EntityPlayer player : mc.world.playerEntities) {
        if (player.getHealth() <= 0.0F && 
          this.popList.containsKey(player.getName())) {
          Command.sendClientMessage(getTextColor() + player.getName() + " died after popping " + ChatFormatting.GREEN + this.popList.get(player.getName()) + getTextColor() + " totems!");
          this.popList.remove(player.getName(), this.popList.get(player.getName()));
        } 
      }  
    if (this.strengthdetect.getValue() && 
      isEnabled() && mc.player != null) {
      Iterator<EntityPlayer> var1 = mc.world.playerEntities.iterator();
      while (var1.hasNext()) {
        EntityPlayer ent = var1.next();
        if (EntityUtil.isLiving((Entity)ent) && ent.getHealth() > 0.0F) {
          if (ent.isPotionActive(MobEffects.STRENGTH) && !this.strengthedPlayers.contains(ent)) {
            Command.sendClientMessage(getTextColor() + ent.getDisplayNameString() + " has (drank) strength!");
            this.strengthedPlayers.add(ent);
          } 
          if (this.strengthedPlayers.contains(ent) && !ent.isPotionActive(MobEffects.STRENGTH)) {
            Command.sendClientMessage(getTextColor() + ent.getDisplayNameString() + " no longer has strength!");
            this.strengthedPlayers.remove(ent);
          } 
          checkRender();
        } 
      } 
    } 
  }
  
  public void checkRender() {
    try {
      this.renderPlayers.clear();
      Iterator<EntityPlayer> var1 = mc.world.playerEntities.iterator();
      while (var1.hasNext()) {
        EntityPlayer ent = var1.next();
        if (EntityUtil.isLiving((Entity)ent) && ent.getHealth() > 0.0F)
          this.renderPlayers.add(ent); 
      } 
      var1 = this.strengthedPlayers.iterator();
      while (var1.hasNext()) {
        EntityPlayer ent = var1.next();
        if (!this.renderPlayers.contains(ent))
          this.strengthedPlayers.remove(ent); 
      } 
    } catch (Exception exception) {}
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
    this.popList = new HashMap<>();
    this.strengthedPlayers = new HashSet();
    this.renderPlayers = new HashSet();
  }
  
  public ChatFormatting getTextColor() {
    if (this.ChatColor.getValue().equalsIgnoreCase("Black"))
      return ChatFormatting.BLACK; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Dark Green"))
      return ChatFormatting.DARK_GREEN; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Dark Red"))
      return ChatFormatting.DARK_RED; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Gold"))
      return ChatFormatting.GOLD; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Dark Gray"))
      return ChatFormatting.DARK_GRAY; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Green"))
      return ChatFormatting.GREEN; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Red"))
      return ChatFormatting.RED; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Yellow"))
      return ChatFormatting.YELLOW; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Dark Blue"))
      return ChatFormatting.DARK_BLUE; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Dark Aqua"))
      return ChatFormatting.DARK_AQUA; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Dark Purple"))
      return ChatFormatting.DARK_PURPLE; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Gray"))
      return ChatFormatting.GRAY; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Blue"))
      return ChatFormatting.BLUE; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Light Purple"))
      return ChatFormatting.LIGHT_PURPLE; 
    if (this.ChatColor.getValue().equalsIgnoreCase("White"))
      return ChatFormatting.WHITE; 
    if (this.ChatColor.getValue().equalsIgnoreCase("Aqua"))
      return ChatFormatting.AQUA; 
    return null;
  }
  
  public void onDisable() {
    this.knownPlayers.clear();
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
