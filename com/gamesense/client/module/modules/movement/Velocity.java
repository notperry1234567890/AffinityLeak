//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;

public class Velocity extends Module {
  Setting.Double xMultiplier = registerDouble("X Multiplier", "xMultiplier", 0.0D, 0.0D, 1.0D);
  
  Setting.Double yMultiplier = registerDouble("Y Multiplier", "yMultiplier", 0.0D, 0.0D, 1.0D);
  
  Setting.Double zMultiplier = registerDouble("Z Multiplier", "zMultiplier", 0.0D, 0.0D, 1.0D);
  
  @EventHandler
  Listener<PacketEvent.Receive> listener;
  
  public Velocity() {
    super("Velocity", Module.Category.Movement);
    this.listener = new Listener(event -> {
          if (!ModuleManager.isModuleEnabled(this))
            return; 
          if (!(event.getPacket() instanceof SPacketEntityVelocity))
            return; 
          SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
          if (packet.entityID != mc.player.entityId)
            return; 
          packet.motionX = (int)(packet.motionX * this.xMultiplier.getValue());
          packet.motionY = (int)(packet.motionY * this.yMultiplier.getValue());
          packet.motionZ = (int)(packet.motionZ * this.zMultiplier.getValue());
        }new java.util.function.Predicate[0]);
  }
}
