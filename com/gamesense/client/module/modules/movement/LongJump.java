//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.event.events.PlayerJumpEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class LongJump extends Module {
  private Setting.Integer xzMultiplier = registerInteger("Speed Multiplier", "SpeedMultiplier", 5, 1, 20);
  
  private Setting.Double fallSpeed = registerDouble("Fall Speed", "FallSpeed", 1.0D, 0.1D, 4.0D);
  
  @EventHandler
  public Listener<PlayerJumpEvent> jumpEventListener;
  
  public LongJump() {
    super("LongJump", Module.Category.Movement);
    this.jumpEventListener = new Listener(e -> {
          if (mc.world == null || mc.getConnection() == null || mc.player == null)
            return; 
          sendMovePacket();
        }new java.util.function.Predicate[0]);
  }
  
  private void sendMovePacket() {
    if (mc.getConnection() == null)
      return; 
    CPacketPlayer packet = new CPacketPlayer(false);
    packet.x = mc.player.motionX * this.xzMultiplier.getValue();
    packet.y = this.fallSpeed.getValue();
    packet.z = mc.player.motionZ * this.xzMultiplier.getValue();
    mc.getConnection().sendPacket((Packet)packet);
  }
}
