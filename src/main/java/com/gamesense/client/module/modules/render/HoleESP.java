//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class HoleESP extends Module {
  public static Setting.Integer rangeS;
  
  private final BlockPos[] surroundOffset = new BlockPos[] { new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
  
  Setting.Boolean hideOwn;
  
  Setting.Boolean flatOwn;
  
  Setting.Mode mode;
  
  Setting.Mode type;
  
  Setting.Integer width;
  
  Setting.ColorSetting bedrockColor;
  
  Setting.ColorSetting otherColor;
  
  private ConcurrentHashMap<BlockPos, Boolean> safeHoles;
  
  public HoleESP() {
    super("HoleESP", Module.Category.Render);
  }
  
  public static BlockPos getPlayerPos() {
    return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
  }
  
  public void setup() {
    rangeS = registerInteger("Range", "Range", 5, 1, 20);
    this.hideOwn = registerBoolean("Hide Own", "HideOwn", false);
    this.flatOwn = registerBoolean("Flat Own", "FlatOwn", false);
    ArrayList<String> render = new ArrayList<>();
    render.add("Outline");
    render.add("Fill");
    render.add("Both");
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Air");
    modes.add("Ground");
    modes.add("Flat");
    this.type = registerMode("Render", "Render", render, "Both");
    this.mode = registerMode("Mode", "Mode", modes, "Air");
    this.width = registerInteger("Width", "Width", 1, 1, 10);
    this.bedrockColor = registerColor("Bedrock Color", "BedrockColor", new APColor(0, 255, 0));
    this.otherColor = registerColor("Obsidian Color", "ObsidianColor", new APColor(255, 0, 0));
  }
  
  public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
    List<BlockPos> circleblocks = new ArrayList<>();
    int cx = loc.getX();
    int cy = loc.getY();
    int cz = loc.getZ();
    for (int x = cx - (int)r; x <= cx + r; x++) {
      for (int z = cz - (int)r; z <= cz + r; ) {
        int y = sphere ? (cy - (int)r) : cy;
        for (;; z++) {
          if (y < (sphere ? (cy + r) : (cy + h))) {
            double dist = ((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0));
            if (dist < (r * r) && (!hollow || dist >= ((r - 1.0F) * (r - 1.0F)))) {
              BlockPos l = new BlockPos(x, y + plus_y, z);
              circleblocks.add(l);
            } 
            y++;
            continue;
          } 
        } 
      } 
    } 
    return circleblocks;
  }
  
  public void onUpdate() {
    if (this.safeHoles == null) {
      this.safeHoles = new ConcurrentHashMap<>();
    } else {
      this.safeHoles.clear();
    } 
    int range = (int)Math.ceil(rangeS.getValue());
    List<BlockPos> blockPosList = getSphere(getPlayerPos(), range, range, false, true, 0);
    for (BlockPos pos : blockPosList) {
      if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR))
        continue; 
      if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR))
        continue; 
      if (!mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR))
        continue; 
      if (this.hideOwn.getValue() && pos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)))
        continue; 
      boolean isSafe = true;
      boolean isBedrock = true;
      for (BlockPos offset : this.surroundOffset) {
        Block block = mc.world.getBlockState(pos.add((Vec3i)offset)).getBlock();
        if (block != Blocks.BEDROCK)
          isBedrock = false; 
        if (block != Blocks.BEDROCK && block != Blocks.OBSIDIAN && block != Blocks.ENDER_CHEST && block != Blocks.ANVIL) {
          isSafe = false;
          break;
        } 
      } 
      if (isSafe)
        this.safeHoles.put(pos, Boolean.valueOf(isBedrock)); 
    } 
  }
  
  public void onWorldRender(RenderEvent event) {
    if (mc.player == null || this.safeHoles == null)
      return; 
    if (this.safeHoles.isEmpty())
      return; 
    GameSenseTessellator.prepare(7);
    this.safeHoles.forEach((blockPos, isBedrock) -> {
          if (this.mode.getValue().equalsIgnoreCase("Air")) {
            drawBox(blockPos, isBedrock.booleanValue());
          } else if (this.mode.getValue().equalsIgnoreCase("Ground")) {
            drawDownBox(blockPos, isBedrock.booleanValue());
          } else if (this.mode.getValue().equalsIgnoreCase("Flat")) {
            drawFlat(blockPos, isBedrock.booleanValue());
          } 
        });
    GameSenseTessellator.release();
    GameSenseTessellator.prepare(7);
    this.safeHoles.forEach((blockPos, isBedrock) -> drawOutline(blockPos, this.width.getValue(), isBedrock.booleanValue()));
    GameSenseTessellator.release();
  }
  
  private APColor getColor(boolean isBedrock, int alpha) {
    APColor c;
    if (isBedrock) {
      c = this.bedrockColor.getValue();
    } else {
      c = this.otherColor.getValue();
    } 
    return new APColor(c, alpha);
  }
  
  private void drawBox(BlockPos blockPos, boolean isBedrock) {
    if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
      APColor color = getColor(isBedrock, 50);
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.mode.getValue().equalsIgnoreCase("Air"))
        if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
          GameSenseTessellator.drawBox(blockPos, color, 1);
        } else {
          GameSenseTessellator.drawBox(blockPos, color, 63);
        }  
    } 
  }
  
  private void drawDownBox(BlockPos blockPos, boolean isBedrock) {
    if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
      APColor color = getColor(isBedrock, 50);
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.mode.getValue().equalsIgnoreCase("Ground"))
        GameSenseTessellator.drawDownBox(blockPos, color, 63); 
    } 
  }
  
  private void drawFlat(BlockPos blockPos, boolean isBedrock) {
    if (this.type.getValue().equalsIgnoreCase("Fill") || this.type.getValue().equalsIgnoreCase("Both")) {
      APColor color = getColor(isBedrock, 50);
      AxisAlignedBB bb = mc.world.getBlockState(blockPos).getSelectedBoundingBox((World)mc.world, blockPos);
      if (this.mode.getValue().equalsIgnoreCase("Flat"))
        GameSenseTessellator.drawBox(blockPos, color, 1); 
    } 
  }
  
  private void drawOutline(BlockPos blockPos, int width, boolean isBedrock) {
    APColor color = getColor(isBedrock, 255);
    if (this.type.getValue().equalsIgnoreCase("Outline") || this.type.getValue().equalsIgnoreCase("Both")) {
      if (this.mode.getValue().equalsIgnoreCase("Air"))
        if (this.flatOwn.getValue() && blockPos.equals(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ))) {
          GameSenseTessellator.drawBoundingBoxBottom2(blockPos, width, color);
        } else {
          GameSenseTessellator.drawBoundingBoxBlockPos(blockPos, width, color);
        }  
      if (this.mode.getValue().equalsIgnoreCase("Flat"))
        GameSenseTessellator.drawBoundingBoxBottom2(blockPos, width, color); 
      if (this.mode.getValue().equalsIgnoreCase("Ground"))
        GameSenseTessellator.drawBoundingBoxBlockPos2(blockPos, width, color); 
    } 
  }
}
