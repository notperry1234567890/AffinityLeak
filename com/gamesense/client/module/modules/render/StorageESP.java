//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class StorageESP extends Module {
  Setting.Integer w;
  
  Setting.ColorSetting c1;
  
  Setting.ColorSetting c2;
  
  Setting.ColorSetting c3;
  
  Setting.ColorSetting c4;
  
  ConcurrentHashMap<TileEntity, String> chests = new ConcurrentHashMap<>();
  
  public StorageESP() {
    super("StorageESP", Module.Category.Render);
  }
  
  public void setup() {
    this.w = registerInteger("Width", "Width", 2, 1, 10);
    this.c1 = registerColor("Chest Color", "ChestColor", new APColor(255, 255, 0));
    this.c2 = registerColor("EnderChest Color", "EnderChestColor", new APColor(180, 70, 200));
    this.c3 = registerColor("Shulker Color", "ShulkerBoxColor", new APColor(150, 150, 150));
    this.c4 = registerColor("Other Color", "OtherColor", new APColor(255, 0, 0));
  }
  
  public void onUpdate() {
    mc.world.loadedTileEntityList.forEach(e -> (String)this.chests.put(e, ""));
  }
  
  public void onWorldRender(RenderEvent event) {
    if (this.chests != null && this.chests.size() > 0) {
      GameSenseTessellator.prepareGL();
      this.chests.forEach((c, t) -> {
            if (mc.world.loadedTileEntityList.contains(c)) {
              if (c instanceof net.minecraft.tileentity.TileEntityChest)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), this.c1.getValue()); 
              if (c instanceof net.minecraft.tileentity.TileEntityEnderChest)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), this.c2.getValue()); 
              if (c instanceof net.minecraft.tileentity.TileEntityShulkerBox)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), this.c3.getValue()); 
              if (c instanceof net.minecraft.tileentity.TileEntityDispenser || c instanceof net.minecraft.tileentity.TileEntityFurnace || c instanceof net.minecraft.tileentity.TileEntityHopper)
                GameSenseTessellator.drawBoundingBox(mc.world.getBlockState(c.getPos()).getSelectedBoundingBox((World)mc.world, c.getPos()), this.w.getValue(), this.c4.getValue()); 
            } 
          });
      GameSenseTessellator.releaseGL();
    } 
  }
}
