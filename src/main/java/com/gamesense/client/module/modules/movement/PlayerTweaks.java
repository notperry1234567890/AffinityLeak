//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.WaterPushEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraftforge.client.event.InputUpdateEvent;
import org.lwjgl.input.Keyboard;

public class PlayerTweaks extends Module {
  public Setting.Boolean guiMove;
  
  public Setting.Boolean noPush;
  
  @EventHandler
  private final Listener<WaterPushEvent> waterPushEventListener;
  
  public Setting.Boolean noSlow;
  
  @EventHandler
  private final Listener<InputUpdateEvent> eventListener;
  
  Setting.Boolean antiKnockBack;
  
  @EventHandler
  private final Listener<PacketEvent.Receive> receiveListener;
  
  public PlayerTweaks() {
    super("PlayerTweaks", Module.Category.Movement);
    this.waterPushEventListener = new Listener(event -> {
          if (this.noPush.getValue())
            event.cancel(); 
        }new java.util.function.Predicate[0]);
    this.eventListener = new Listener(event -> {
          if (this.noSlow.getValue() && mc.player.isHandActive() && !mc.player.isRiding()) {
            (event.getMovementInput()).moveStrafe *= 5.0F;
            (event.getMovementInput()).moveForward *= 5.0F;
          } 
        }new java.util.function.Predicate[0]);
    this.receiveListener = new Listener(event -> {
          if (this.antiKnockBack.getValue()) {
            if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.getEntityId())
              event.cancel(); 
            if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketExplosion)
              event.cancel(); 
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.guiMove = registerBoolean("Gui Move", "GuiMove", false);
    this.noPush = registerBoolean("No Push", "NoPush", false);
    this.noSlow = registerBoolean("No Slow", "NoSlow", false);
    this.antiKnockBack = registerBoolean("Velocity", "Velocity", false);
  }
  
  public void onUpdate() {
    if (this.guiMove.getValue() && 
      mc.currentScreen != null && !(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)) {
      if (Keyboard.isKeyDown(200))
        mc.player.rotationPitch -= 5.0F; 
      if (Keyboard.isKeyDown(208))
        mc.player.rotationPitch += 5.0F; 
      if (Keyboard.isKeyDown(205))
        mc.player.rotationYaw += 5.0F; 
      if (Keyboard.isKeyDown(203))
        mc.player.rotationYaw -= 5.0F; 
      if (mc.player.rotationPitch > 90.0F)
        mc.player.rotationPitch = 90.0F; 
      if (mc.player.rotationPitch < -90.0F)
        mc.player.rotationPitch = -90.0F; 
    } 
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
