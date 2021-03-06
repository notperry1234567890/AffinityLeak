//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.mixin.mixins;

import com.gamesense.api.event.events.PlayerMoveEvent;
import com.gamesense.client.AffinityPlus;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
  public MixinEntityPlayerSP() {
    super(null, null);
  }
  
  @Redirect(method = {"move"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
  public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
    PlayerMoveEvent moveEvent = new PlayerMoveEvent(type, x, y, z);
    AffinityPlus.EVENT_BUS.post(moveEvent);
    if (moveEvent.isCancelled());
    move(type, moveEvent.x, moveEvent.y, moveEvent.z);
  }
}
