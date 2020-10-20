//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import net.minecraft.client.renderer.ItemRenderer;

public class RenderTweaks extends Module {
  public Setting.Boolean viewClip;
  
  Setting.Boolean nekoAnimation;
  
  Setting.Boolean lowOffhand;
  
  Setting.Boolean fovChanger;
  
  Setting.Double lowOffhandSlider;
  
  Setting.Integer fovChangerSlider;
  
  ItemRenderer itemRenderer = mc.entityRenderer.itemRenderer;
  
  private float oldFOV;
  
  public RenderTweaks() {
    super("RenderTweaks", Module.Category.Render);
  }
  
  public void setup() {
    this.viewClip = registerBoolean("View Clip", "ViewClip", false);
    this.nekoAnimation = registerBoolean("Neko Animation", "NekoAnimation", false);
    this.lowOffhand = registerBoolean("Low Offhand", "LowOffhand", false);
    this.lowOffhandSlider = registerDouble("Offhand Height", "OffhandHeight", 1.0D, 0.1D, 1.0D);
    this.fovChanger = registerBoolean("FOV", "FOV", false);
    this.fovChangerSlider = registerInteger("FOV Slider", "FOVSlider", 90, 70, 200);
  }
  
  public void onUpdate() {
    if (this.nekoAnimation.getValue() && 
      mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword && mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9D) {
      mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0F;
      mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
    } 
    if (this.lowOffhand.getValue())
      this.itemRenderer.equippedProgressOffHand = (float)this.lowOffhandSlider.getValue(); 
    if (this.fovChanger.getValue())
      mc.gameSettings.fovSetting = this.fovChangerSlider.getValue(); 
    if (!this.fovChanger.getValue())
      mc.gameSettings.fovSetting = this.oldFOV; 
  }
  
  public void onEnable() {
    this.oldFOV = mc.gameSettings.fovSetting;
  }
  
  public void onDisable() {
    mc.gameSettings.fovSetting = this.oldFOV;
  }
}
