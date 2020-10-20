//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.mixin.mixins;

import com.gamesense.client.module.ModuleManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockLiquid.class})
public class MixinBlockLiquid {
  @Inject(method = {"canCollideCheck"}, at = {@At("HEAD")}, cancellable = true)
  public void canCollideCheck(IBlockState blockState, boolean b, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
    callbackInfoReturnable.setReturnValue(Boolean.valueOf((ModuleManager.isModuleEnabled("LiquidInteract") || (b && ((Integer)blockState.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0))));
  }
}
