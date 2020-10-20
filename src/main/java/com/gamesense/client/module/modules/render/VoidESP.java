//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.module.Module;
import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class VoidESP extends Module {
  Setting.Integer renderDistance;
  
  Setting.Integer activeYValue;
  
  Setting.Mode renderType;
  
  Setting.Mode renderMode;
  
  Setting.Integer width;
  
  Setting.ColorSetting color;
  
  private ConcurrentSet<BlockPos> voidHoles;
  
  public VoidESP() {
    super("VoidESP", Module.Category.Render);
  }
  
  public static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  public void setup() {
    ArrayList<String> render = new ArrayList<>();
    render.add("Outline");
    render.add("Fill");
    render.add("Both");
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Box");
    modes.add("Flat");
    this.renderDistance = registerInteger("Distance", "Distance", 10, 1, 40);
    this.activeYValue = registerInteger("Activate Y", "ActivateY", 20, 0, 256);
    this.renderType = registerMode("Render", "Render", render, "Both");
    this.renderMode = registerMode("Mode", "Mode", modes, "Flat");
    this.width = registerInteger("Width", "Width", 1, 1, 10);
    this.color = registerColor("Color", "Color", new APColor(255, 255, 0));
  }
  
  public void onUpdate() {
    if (mc.player.dimension == 1)
      return; 
    if (mc.player.getPosition().getY() > this.activeYValue.getValue())
      return; 
    if (this.voidHoles == null) {
      this.voidHoles = new ConcurrentSet();
    } else {
      this.voidHoles.clear();
    } 
    List<BlockPos> blockPosList = BlockUtils.getCircle(getPlayerPos(), 0, this.renderDistance.getValue(), false);
    for (BlockPos blockPos : blockPosList) {
      if (mc.world.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK))
        continue; 
      if (isAnyBedrock(blockPos, Offsets.center))
        continue; 
      this.voidHoles.add(blockPos);
    } 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (mc.player == null || this.voidHoles == null)
      return; 
    if (mc.player.getPosition().getY() > this.activeYValue.getValue())
      return; 
    if (this.voidHoles.isEmpty())
      return; 
    this.voidHoles.forEach(blockPos -> {
          GameSenseTessellator.prepare(7);
          if (this.renderMode.getValue().equalsIgnoreCase("Box")) {
            drawBox(blockPos);
          } else {
            drawFlat(blockPos);
          } 
          GameSenseTessellator.release();
          GameSenseTessellator.prepare(7);
          drawOutline(blockPos, this.width.getValue());
          GameSenseTessellator.release();
        });
  }
  
  private boolean isAnyBedrock(BlockPos origin, BlockPos[] offset) {
    for (BlockPos pos : offset) {
      if (mc.world.getBlockState(origin.add((Vec3i)pos)).getBlock().equals(Blocks.BEDROCK))
        return true; 
    } 
    return false;
  }
  
  private void drawFlat(BlockPos blockPos) {
    if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
      APColor c = new APColor(this.color.getValue(), 50);
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.renderMode.getValue().equalsIgnoreCase("Flat"))
        GameSenseTessellator.drawBox(blockPos, c, 1); 
    } 
  }
  
  private void drawBox(BlockPos blockPos) {
    if (this.renderType.getValue().equalsIgnoreCase("Fill") || this.renderType.getValue().equalsIgnoreCase("Both")) {
      APColor c = new APColor(this.color.getValue(), 50);
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      GameSenseTessellator.drawBox(blockPos, c, 63);
    } 
  }
  
  private void drawOutline(BlockPos blockPos, int width) {
    if (this.renderType.getValue().equalsIgnoreCase("Outline") || this.renderType.getValue().equalsIgnoreCase("Both")) {
      if (this.renderMode.getValue().equalsIgnoreCase("Box"))
        GameSenseTessellator.drawBoundingBoxBlockPos(blockPos, width, this.color.getValue()); 
      if (this.renderMode.getValue().equalsIgnoreCase("Flat"))
        GameSenseTessellator.drawBoundingBoxBottom2(blockPos, width, this.color.getValue()); 
    } 
  }
  
  private static class Offsets {
    static final BlockPos[] center = new BlockPos[] { new BlockPos(0, 0, 0), new BlockPos(0, 1, 0), new BlockPos(0, 2, 0) };
  }
}
