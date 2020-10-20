//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import net.minecraft.entity.Entity;

public class EntityESP extends Module {
  Setting.Mode renderMode;
  
  Setting.Boolean exp;
  
  Setting.Boolean epearls;
  
  Setting.Boolean items;
  
  Setting.Boolean orbs;
  
  Setting.Boolean crystals;
  
  Setting.Integer width;
  
  Setting.ColorSetting color;
  
  public EntityESP() {
    super("EntityESP", Module.Category.Render);
  }
  
  public void setup() {
    ArrayList<String> Modes = new ArrayList<>();
    Modes.add("Box");
    Modes.add("Outline");
    Modes.add("Glow");
    this.exp = registerBoolean("Exp Bottles", "ExpBottles", false);
    this.epearls = registerBoolean("Ender Pearls", "EnderPearls", false);
    this.crystals = registerBoolean("Crystals", "Crystals", false);
    this.items = registerBoolean("Items", "Items", false);
    this.orbs = registerBoolean("Exp Orbs", "ExpOrbs", false);
    this.renderMode = registerMode("Mode", "Mode", Modes, "Box");
    this.width = registerInteger("Width", "Width", 1, 1, 10);
    this.color = registerColor("Color", "Color");
  }
  
  public void onWorldRender(RenderEvent event) {
    APColor rgbColor = this.color.getValue();
    APColor c = new APColor(rgbColor, 50);
    APColor c2 = new APColor(rgbColor, 255);
    boolean drawBox = this.renderMode.getValue().equalsIgnoreCase("Box");
    boolean drawOutline = (this.renderMode.getValue().equalsIgnoreCase("Box") || this.renderMode.getValue().equalsIgnoreCase("Outline"));
    boolean drawGlow = this.renderMode.getValue().equalsIgnoreCase("Glow");
    mc.world.loadedEntityList.stream()
      .filter(entity -> (entity != mc.player))
      .forEach(e -> {
          boolean drawThisThing = false;
          if (this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle) {
            drawThisThing = true;
          } else if (this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl) {
            drawThisThing = true;
          } else if (this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal) {
            drawThisThing = true;
          } else if (this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem) {
            drawThisThing = true;
          } else if (this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb) {
            drawThisThing = true;
          } 
          if (drawThisThing) {
            if (drawBox) {
              GameSenseTessellator.prepare(7);
              GameSenseTessellator.drawBox(e.getRenderBoundingBox(), c, 63);
              GameSenseTessellator.release();
            } 
            if (drawOutline) {
              GameSenseTessellator.prepareGL();
              GameSenseTessellator.drawBoundingBox(e.getRenderBoundingBox(), this.width.getValue(), c2);
              GameSenseTessellator.releaseGL();
            } 
            if (drawGlow)
              e.setGlowing(true); 
          } 
          GameSenseTessellator.releaseGL();
        });
  }
  
  public void onUpdate() {
    mc.world.loadedEntityList.stream()
      .filter(e -> (e != mc.player))
      .forEach(e -> {
          if (!this.renderMode.getValue().equalsIgnoreCase("Glow")) {
            if (e instanceof net.minecraft.entity.item.EntityExpBottle)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderPearl)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityItem)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityXPOrb)
              e.setGlowing(false); 
          } 
          if (!this.exp.getValue() && e instanceof net.minecraft.entity.item.EntityExpBottle)
            e.setGlowing(false); 
          if (!this.epearls.getValue() && e instanceof net.minecraft.entity.item.EntityEnderPearl)
            e.setGlowing(false); 
          if (!this.crystals.getValue() && e instanceof net.minecraft.entity.item.EntityEnderCrystal)
            e.setGlowing(false); 
          if (!this.items.getValue() && e instanceof net.minecraft.entity.item.EntityItem)
            e.setGlowing(false); 
          if (!this.orbs.getValue() && e instanceof net.minecraft.entity.item.EntityXPOrb)
            e.setGlowing(false); 
        });
  }
  
  public void onDisable() {
    if (this.renderMode.getValue().equalsIgnoreCase("Glow"))
      mc.world.loadedEntityList.stream()
        .filter(e -> (e != mc.player))
        .forEach(e -> {
            if (e instanceof net.minecraft.entity.item.EntityExpBottle)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderPearl)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityEnderCrystal)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityItem)
              e.setGlowing(false); 
            if (e instanceof net.minecraft.entity.item.EntityXPOrb)
              e.setGlowing(false); 
          }); 
  }
  
  public String getHudInfo() {
    String t = "";
    if (this.renderMode.getValue().equalsIgnoreCase("Box"))
      t = "[" + ChatFormatting.WHITE + "Box" + ChatFormatting.GRAY + "]"; 
    if (this.renderMode.getValue().equalsIgnoreCase("Outline"))
      t = "[" + ChatFormatting.WHITE + "Outline" + ChatFormatting.GRAY + "]"; 
    if (this.renderMode.getValue().equalsIgnoreCase("Glow"))
      t = "[" + ChatFormatting.WHITE + "Glow" + ChatFormatting.GRAY + "]"; 
    return t;
  }
}
