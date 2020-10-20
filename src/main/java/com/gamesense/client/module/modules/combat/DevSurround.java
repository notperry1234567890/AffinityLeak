//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.BlockUtils;
import com.gamesense.client.module.Module;
import java.util.Arrays;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DevSurround extends Module {
  public final Setting.Integer BPT = registerInteger("Blocks Per Tick", "BPT", 4, 1, 10);
  
  public final Setting.Boolean cancelOnAir = registerBoolean("Cancel On Air", "CancelOnAir", true);
  
  public final Setting.Boolean autoCenter = registerBoolean("Auto Center", "AutoCenter", true);
  
  int blocksPerTickCount;
  
  public DevSurround() {
    super("Dev Surround", Module.Category.Combat);
    this.blocksPerTickCount = 0;
  }
  
  public void onUpdate() {
    this.blocksPerTickCount = 0;
    BlockPos current = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    List<BlockPos> posList = Arrays.asList(new BlockPos[] { current
          .west(), current
          .west().north(), current
          .north(), current
          .north().east(), current
          .east(), current
          .east().south(), current
          .south(), current
          .south().west() });
    List<EnumFacing> wantedFacing = Arrays.asList(new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH });
    for (BlockPos b : posList) {
      for (EnumFacing f : wantedFacing) {
        placeBlock(b, f);
        if (this.cancelOnAir.getValue() && mc.player.isAirBorne)
          return; 
        if (this.BPT.getValue() >= this.blocksPerTickCount)
          return; 
      } 
      if (this.cancelOnAir.getValue() && mc.player.isAirBorne)
        return; 
      if (this.BPT.getValue() >= this.blocksPerTickCount)
        return; 
    } 
  }
  
  private void placeBlock(BlockPos b, EnumFacing facing) {
    if (BlockUtils.getBlock(b) != Blocks.AIR) {
      this.blocksPerTickCount++;
      return;
    } 
    mc.playerController.clickBlock(b, facing);
    mc.playerController.updateController();
  }
  
  private void equipObsidian() {
    int obiPos = -1;
  }
}
