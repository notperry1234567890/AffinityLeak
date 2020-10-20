//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.client.module.Module;
import java.util.Objects;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class FootXp extends Module {
  @EventHandler
  final Listener<PacketEvent.Send> packetListener;
  
  public FootXp() {
    super("FootXp", Module.Category.Combat);
    this.packetListener = new Listener(event -> {
          if (event.getPacket() instanceof CPacketPlayer && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemExpBottle) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            event.cancel();
            packet.pitch = 90.0F;
            packet.moving = false;
            packet.onGround = true;
            ((NetHandlerPlayClient)Objects.<NetHandlerPlayClient>requireNonNull(mc.getConnection())).sendPacket((Packet)packet);
          } 
        }new java.util.function.Predicate[0]);
  }
}
