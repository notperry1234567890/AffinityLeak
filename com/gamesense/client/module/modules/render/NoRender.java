//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.BossbarEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.material.Material;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class NoRender extends Module {
  @EventHandler
  private final Listener<RenderBlockOverlayEvent> renderBlockOverlayEventListener;
  
  public Setting.Boolean armor;
  
  public Setting.Boolean hurtCam;
  
  public Setting.Boolean noOverlay;
  
  @EventHandler
  private final Listener<EntityViewRenderEvent.FogDensity> fogDensityListener;
  
  @EventHandler
  private final Listener<RenderGameOverlayEvent> renderGameOverlayEventListener;
  
  public Setting.Boolean noSkylight;
  
  Setting.Boolean fire;
  
  @EventHandler
  public Listener<RenderBlockOverlayEvent> blockOverlayEventListener;
  
  Setting.Boolean blind;
  
  Setting.Boolean nausea;
  
  Setting.Boolean noBossBar;
  
  @EventHandler
  private final Listener<BossbarEvent> bossbarEventListener;
  
  public NoRender() {
    super("NoRender", Module.Category.Render);
    this.renderBlockOverlayEventListener = new Listener(event -> event.setCanceled(true), new java.util.function.Predicate[0]);
    this.fogDensityListener = new Listener(event -> {
          if (this.noOverlay.getValue() && (event.getState().getMaterial().equals(Material.WATER) || event.getState().getMaterial().equals(Material.LAVA))) {
            event.setDensity(0.0F);
            event.setCanceled(true);
          } 
        }new java.util.function.Predicate[0]);
    this.renderGameOverlayEventListener = new Listener(event -> {
          if (this.noOverlay.getValue()) {
            if (event.getType().equals(RenderGameOverlayEvent.ElementType.HELMET))
              event.setCanceled(true); 
            if (event.getType().equals(RenderGameOverlayEvent.ElementType.PORTAL))
              event.setCanceled(true); 
          } 
        }new java.util.function.Predicate[0]);
    this.blockOverlayEventListener = new Listener(event -> {
          if (this.fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)
            event.setCanceled(true); 
          if (this.noOverlay.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER)
            event.setCanceled(true); 
          if (this.noOverlay.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK)
            event.setCanceled(true); 
        }new java.util.function.Predicate[0]);
    this.bossbarEventListener = new Listener(event -> {
          if (this.noBossBar.getValue())
            event.cancel(); 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.armor = registerBoolean("Armor", "Armor", false);
    this.fire = registerBoolean("Fire", "Fire", false);
    this.blind = registerBoolean("Blind", "Blind", false);
    this.nausea = registerBoolean("Nausea", "Nausea", false);
    this.hurtCam = registerBoolean("HurtCam", "HurtCam", false);
    this.noSkylight = registerBoolean("Skylight", "Skylight", false);
    this.noOverlay = registerBoolean("No Overlay", "NoOverlay", false);
    this.noBossBar = registerBoolean("No Boss Bar", "NoBossBar", false);
  }
  
  public void onUpdate() {
    if (this.blind.getValue() && mc.player.isPotionActive(MobEffects.BLINDNESS))
      mc.player.removePotionEffect(MobEffects.BLINDNESS); 
    if (this.nausea.getValue() && mc.player.isPotionActive(MobEffects.NAUSEA))
      mc.player.removePotionEffect(MobEffects.NAUSEA); 
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
