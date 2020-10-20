package com.gamesense.api.event.events;

import com.gamesense.api.event.GameSenseEvent;
import net.minecraft.util.math.BlockPos;

public class DestroyBlockEvent extends GameSenseEvent {
  BlockPos pos;
  
  public DestroyBlockEvent(BlockPos blockPos) {
    this.pos = blockPos;
  }
  
  public BlockPos getBlockPos() {
    return this.pos;
  }
  
  public void setPos(BlockPos pos) {
    this.pos = pos;
  }
}
