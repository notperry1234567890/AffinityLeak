//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockHighlight extends Module {
  Setting.Integer w;
  
  Setting.Boolean shade;
  
  Setting.ColorSetting color;
  
  public BlockHighlight() {
    super("BlockHighlight", Module.Category.Render);
  }
  
  public void setup() {
    this.shade = registerBoolean("Fill", "Fill", false);
    this.w = registerInteger("Width", "Width", 2, 1, 10);
    this.color = registerColor("Color", "Color");
  }
  
  public void onWorldRender(RenderEvent event) {
    RayTraceResult ray = mc.objectMouseOver;
    APColor c2 = new APColor(this.color.getValue(), 50);
    if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
      BlockPos pos = ray.getBlockPos();
      AxisAlignedBB bb = mc.world.getBlockState(pos).getSelectedBoundingBox((World)mc.world, pos);
      if (bb != null && pos != null && mc.world.getBlockState(pos).getMaterial() != Material.AIR) {
        GameSenseTessellator.prepareGL();
        GameSenseTessellator.drawBoundingBox(bb, this.w.getValue(), this.color.getValue());
        GameSenseTessellator.releaseGL();
        if (this.shade.getValue()) {
          GameSenseTessellator.prepare(7);
          GameSenseTessellator.drawBox(bb, c2, 63);
          GameSenseTessellator.release();
        } 
      } 
    } 
  }
}
