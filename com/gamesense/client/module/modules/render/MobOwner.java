//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityTameable;

public class MobOwner extends Module {
  private static long startTime1 = 0L;
  
  private static long startTime2 = 0L;
  
  private final Map<String, String> cachedUUIDs = new HashMap<String, String>() {
    
    };
  
  private final String invalidText = "Servers offline!";
  
  private Setting.Integer requestTime;
  
  private Setting.Boolean debug;
  
  private int apiRequests = 0;
  
  public MobOwner() {
    super("MobOwner", Module.Category.Render);
  }
  
  public void setup() {
    this.requestTime = registerInteger("Reset Ticks", "ResetTicks", 10, 0, 20);
    this.debug = registerBoolean("Debug", "Debug", false);
  }
  
  public void onUpdate() {
    resetRequests();
    resetCache();
    for (Entity entity : mc.world.loadedEntityList) {
      if (entity instanceof EntityTameable) {
        EntityTameable entityTameable = (EntityTameable)entity;
        if (entityTameable.isTamed() && entityTameable.getOwner() != null) {
          entityTameable.setAlwaysRenderNameTag(true);
          entityTameable.setCustomNameTag("Owner: " + entityTameable.getOwner().getDisplayName().getFormattedText());
        } 
      } 
      if (entity instanceof AbstractHorse) {
        AbstractHorse abstractHorse = (AbstractHorse)entity;
        if (!abstractHorse.isTame() || abstractHorse.getOwnerUniqueId() == null)
          continue; 
        abstractHorse.setAlwaysRenderNameTag(true);
        abstractHorse.setCustomNameTag("Owner: " + getUsername(abstractHorse.getOwnerUniqueId().toString()));
      } 
    } 
  }
  
  private String getUsername(String uuid) {
    for (Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
      if (((String)entries.getKey()).equalsIgnoreCase(uuid))
        return entries.getValue(); 
    } 
    try {
      if (this.apiRequests > 10)
        return "Too many API requests!"; 
      this.cachedUUIDs.put(uuid, ((String)Objects.<String>requireNonNull(EntityUtil.getNameFromUUID(uuid))).replace("\"", ""));
      this.apiRequests++;
    } catch (IllegalStateException illegal) {
      this.cachedUUIDs.put(uuid, "Servers offline!");
    } 
    for (Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
      if (((String)entries.getKey()).equalsIgnoreCase(uuid))
        return entries.getValue(); 
    } 
    return "Servers offline!";
  }
  
  public void onDisable() {
    this.cachedUUIDs.clear();
    for (Entity entity : mc.world.loadedEntityList) {
      if (!(entity instanceof EntityTameable) && 
        !(entity instanceof AbstractHorse))
        continue; 
      try {
        entity.setAlwaysRenderNameTag(false);
      } catch (Exception exception) {}
    } 
  }
  
  private void resetRequests() {
    if (startTime1 == 0L)
      startTime1 = System.currentTimeMillis(); 
    if (startTime1 + 10000L <= System.currentTimeMillis()) {
      startTime1 = System.currentTimeMillis();
      if (this.apiRequests >= 2) {
        this.apiRequests = 0;
        if (this.debug.getValue())
          Command.sendClientMessage("Reset API requests counter!"); 
      } 
    } 
  }
  
  private void resetCache() {
    if (startTime2 == 0L)
      startTime2 = System.currentTimeMillis(); 
    if (startTime2 + (this.requestTime.getValue() * 1000) <= System.currentTimeMillis()) {
      startTime2 = System.currentTimeMillis();
      for (Map.Entry<String, String> entries : this.cachedUUIDs.entrySet()) {
        if (((String)entries.getKey()).equalsIgnoreCase("Servers offline!")) {
          this.cachedUUIDs.clear();
          if (this.debug.getValue())
            Command.sendClientMessage("Reset cached UUIDs list!"); 
          return;
        } 
      } 
    } 
  }
}
