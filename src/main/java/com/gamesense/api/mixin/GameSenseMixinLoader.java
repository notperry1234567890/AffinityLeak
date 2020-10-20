package com.gamesense.api.mixin;

import com.gamesense.client.AffinityPlus;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

public class GameSenseMixinLoader implements IFMLLoadingPlugin {
  private static boolean isObfuscatedEnvironment = false;
  
  public GameSenseMixinLoader() {
    AffinityPlus.log.info("GameSense mixins initialized");
    MixinBootstrap.init();
    Mixins.addConfiguration("mixins.gamesense.json");
    MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    AffinityPlus.log.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
  }
  
  public String[] getASMTransformerClass() {
    return new String[0];
  }
  
  public String getModContainerClass() {
    return null;
  }
  
  @Nullable
  public String getSetupClass() {
    return null;
  }
  
  public void injectData(Map<String, Object> data) {
    isObfuscatedEnvironment = ((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
  }
  
  public String getAccessTransformerClass() {
    return null;
  }
}
