//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class NoKick extends Module {
  public Setting.Boolean noPacketKick;
  
  Setting.Boolean noSlimeCrash;
  
  Setting.Boolean noOffhandCrash;
  
  @EventHandler
  private final Listener<PacketEvent.Receive> receiveListener;
  
  public NoKick() {
    super("NoKick", Module.Category.Misc);
    this.receiveListener = new Listener(event -> {
          if (this.noOffhandCrash.getValue() && event.getPacket() instanceof SPacketSoundEffect && ((SPacketSoundEffect)event.getPacket()).getSound() == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC)
            event.cancel(); 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.noPacketKick = registerBoolean("Packet", "Packet", true);
    this.noSlimeCrash = registerBoolean("Slime", "Slime", false);
    this.noOffhandCrash = registerBoolean("Offhand", "Offhand", false);
  }
  
  public void onUpdate() {
    if (mc.world != null && this.noSlimeCrash.getValue())
      mc.world.loadedEntityList
        .forEach(entity -> {
            if (entity instanceof EntitySlime) {
              EntitySlime slime = (EntitySlime)entity;
              if (slime.getSlimeSize() > 4)
                mc.world.removeEntity(entity); 
            } 
          }); 
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
