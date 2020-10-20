//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.TransformSideFirstPersonEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {
  public Setting.Boolean cancelEating;
  
  Setting.Double xRight;
  
  Setting.Double yRight;
  
  Setting.Double zRight;
  
  Setting.Double xLeft;
  
  Setting.Double yLeft;
  
  Setting.Double zLeft;
  
  @EventHandler
  private final Listener<TransformSideFirstPersonEvent> eventListener;
  
  public ViewModel() {
    super("ViewModel", Module.Category.Render);
    this.eventListener = new Listener(event -> {
          if (event.getHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(this.xRight.getValue(), this.yRight.getValue(), this.zRight.getValue());
          } else if (event.getHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(this.xLeft.getValue(), this.yLeft.getValue(), this.zLeft.getValue());
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.cancelEating = registerBoolean("No Eat", "NoEat", false);
    this.xLeft = registerDouble("Left X", "LeftX", 0.0D, -2.0D, 2.0D);
    this.yLeft = registerDouble("Left Y", "LeftY", 0.2D, -2.0D, 2.0D);
    this.zLeft = registerDouble("Left Z", "LeftZ", -1.2D, -2.0D, 2.0D);
    this.xRight = registerDouble("Right X", "RightX", 0.0D, -2.0D, 2.0D);
    this.yRight = registerDouble("Right Y", "RightY", 0.2D, -2.0D, 2.0D);
    this.zRight = registerDouble("Right Z", "RightZ", -1.2D, -2.0D, 2.0D);
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
