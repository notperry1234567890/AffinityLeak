//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.util.Wrapper;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.Module;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class HitSpheres extends Module {
  public HitSpheres() {
    super("HitSpheres", Module.Category.Render);
  }
  
  public void onWorldRender(RenderEvent event) {
    if (isEnabled())
      for (Entity ep : (Wrapper.getWorld()).loadedEntityList) {
        if (!(ep instanceof net.minecraft.client.entity.EntityPlayerSP) && 
          ep instanceof net.minecraft.entity.player.EntityPlayer) {
          double d = ep.lastTickPosX + (ep.posX - ep.lastTickPosX) * (Wrapper.getMinecraft()).timer.renderPartialTicks;
          double d1 = ep.lastTickPosY + (ep.posY - ep.lastTickPosY) * (Wrapper.getMinecraft()).timer.renderPartialTicks;
          double d2 = ep.lastTickPosZ + (ep.posZ - ep.lastTickPosZ) * (Wrapper.getMinecraft()).timer.renderPartialTicks;
          if (Friends.isFriend(ep.getName())) {
            GL11.glColor4f(0.15F, 0.15F, 1.0F, 1.0F);
          } else if (Wrapper.getPlayer().getDistanceSq(ep) >= 64.0D) {
            GL11.glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
          } else {
            GL11.glColor4f(1.0F, Wrapper.getPlayer().getDistance(ep) / 150.0F, 0.0F, 1.0F);
          } 
          GameSenseTessellator.drawSphere(d, d1, d2, 6.0F, 20, 15);
        } 
      }  
  }
}
