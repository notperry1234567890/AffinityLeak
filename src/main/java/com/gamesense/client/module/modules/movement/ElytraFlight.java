//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import java.util.Arrays;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.lwjgl.input.Keyboard;

@EventBusSubscriber
public class ElytraFlight extends Module {
  private final Setting.Double speed = registerDouble("Speed", "Speed", 50.0D, 1.0D, 150.0D);
  
  private final Setting.Mode mode = registerMode("Mode", "Mode", Arrays.asList(new String[] { "Control", "Highway", "Superman" }, ), "Control");
  
  private final Setting.Boolean takeoffTimer = registerBoolean("Takeoff Timer", "TakeoffTimer", true);
  
  private final Setting.Boolean autoTakeoff = registerBoolean("Auto Takeoff", "AutoTakeoff", true);
  
  private final Setting.Mode yawMode = registerMode("Yaw Mode", "YawMode", Arrays.asList(new String[] { "East", "North", "West", "South", "PlayerYaw" }, ), "PlayerYaw");
  
  private boolean wasAllowedFlying = false;
  
  private boolean wasFlying = false;
  
  private float wantedPitch = 90.0F;
  
  public ElytraFlight() {
    super("ElytraFlight", Module.Category.Movement);
  }
  
  public void onEnable() {
    MinecraftForge.EVENT_BUS.register(this);
    if (mc.getConnection() == null || mc.world == null || mc.player == null)
      return; 
    this.wasAllowedFlying = mc.player.capabilities.allowFlying;
    mc.player.capabilities.allowFlying = true;
    this.wasFlying = mc.player.capabilities.isFlying;
    mc.player.capabilities.isFlying = true;
  }
  
  public void onDisable() {
    MinecraftForge.EVENT_BUS.unregister(this);
    if (mc.getConnection() == null || mc.world == null || mc.player == null)
      return; 
    mc.player.capabilities.allowFlying = this.wasAllowedFlying;
    mc.player.capabilities.isFlying = this.wasFlying;
  }
  
  public void onUpdate() {
    if (mc.getConnection() == null || mc.world == null || mc.player == null)
      return; 
    if (!mc.player.onGround) {
      CPacketPlayer packet = new CPacketPlayer(false);
      setWantedPitch(Keyboard.isKeyDown(57));
      packet.pitch = this.wantedPitch;
      packet.yaw = getWantedYaw();
      packet.moving = true;
      packet.x = this.speed.getValue();
      packet.z = this.speed.getValue();
      mc.getConnection().sendPacket((Packet)packet);
    } 
  }
  
  private void setWantedPitch(boolean isSpacePressed) {
    if (isSpacePressed) {
      switch (this.mode.getValue()) {
        case "Control":
          this.wantedPitch = 180.0F;
          break;
        case "Highway":
          this.wantedPitch = 145.0F;
          break;
        case "Superman":
          this.wantedPitch = mc.player.rotationPitch + 40.0F;
          break;
      } 
    } else {
      switch (this.mode.getValue()) {
        case "Control":
          this.wantedPitch = 90.0F;
          break;
        case "Highway":
          this.wantedPitch = 80.0F;
          break;
        case "Superman":
          this.wantedPitch = mc.player.rotationPitch;
          break;
      } 
    } 
  }
  
  private float getWantedYaw() {
    switch (this.yawMode.getValue()) {
      case "East":
        return 0.0F;
      case "North":
        return 0.0F;
      case "West":
        return 0.0F;
      case "South":
        return 0.0F;
    } 
    return mc.player.rotationYaw;
  }
}
