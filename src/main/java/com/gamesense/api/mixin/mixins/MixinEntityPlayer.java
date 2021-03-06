//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.mixin.mixins;

import com.gamesense.api.event.events.PlayerJumpEvent;
import com.gamesense.api.event.events.WaterPushEvent;
import com.gamesense.client.AffinityPlus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({EntityPlayer.class})
public abstract class MixinEntityPlayer {
  @Shadow
  public abstract String getName();
  
  @Inject(method = {"jump"}, at = {@At("HEAD")}, cancellable = true)
  public void onJump(CallbackInfo ci) {
    if ((Minecraft.getMinecraft()).player.getName() == getName())
      AffinityPlus.EVENT_BUS.post(new PlayerJumpEvent()); 
  }
  
  @Inject(method = {"isPushedByWater"}, at = {@At("HEAD")}, cancellable = true)
  private void onPushedByWater(CallbackInfoReturnable<Boolean> cir) {
    WaterPushEvent event = new WaterPushEvent();
    AffinityPlus.EVENT_BUS.post(event);
    if (event.isCancelled())
      cir.setReturnValue(Boolean.valueOf(false)); 
  }
}
