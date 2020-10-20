//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.event.events;

import com.gamesense.api.event.GameSenseEvent;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;

public class RenderEvent extends GameSenseEvent {
  private final Tessellator tessellator;
  
  private final Vec3d renderPos;
  
  private final float partialTicks;
  
  public RenderEvent(Tessellator tessellator, Vec3d renderPos, float ticks) {
    this.tessellator = tessellator;
    this.renderPos = renderPos;
    this.partialTicks = ticks;
  }
  
  public Tessellator getTessellator() {
    return this.tessellator;
  }
  
  public BufferBuilder getBuffer() {
    return this.tessellator.getBuffer();
  }
  
  public Vec3d getRenderPos() {
    return this.renderPos;
  }
  
  public void setTranslation(Vec3d translation) {
    getBuffer().setTranslation(-translation.x, -translation.y, -translation.z);
  }
  
  public void resetTranslation() {
    setTranslation(this.renderPos);
  }
  
  public float getPartialTicks() {
    return this.partialTicks;
  }
}
