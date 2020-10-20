//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.mixin.mixins;

import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.movement.PlayerTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = {MovementInputFromOptions.class}, priority = 10000)
public abstract class MixinMovementInputFromOptions extends MovementInput {
  @Redirect(method = {"updatePlayerMoveState"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
  public boolean isKeyPressed(KeyBinding keyBinding) {
    if (ModuleManager.isModuleEnabled("PlayerTweaks") && ((PlayerTweaks)ModuleManager.getModuleByName("PlayerTweaks")).guiMove.getValue() && 
      (Minecraft.getMinecraft()).currentScreen != null && 
      !((Minecraft.getMinecraft()).currentScreen instanceof net.minecraft.client.gui.GuiChat) && 
      (Minecraft.getMinecraft()).player != null)
      return Keyboard.isKeyDown(keyBinding.getKeyCode()); 
    return keyBinding.isKeyDown();
  }
}
