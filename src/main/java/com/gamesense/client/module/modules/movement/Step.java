//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.api.util.world.MotionUtils;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class Step extends Module {
  Setting.Double height;
  
  Setting.Boolean timer;
  
  Setting.Boolean reverse;
  
  Setting.Mode mode;
  
  private int ticks = 0;
  
  public Step() {
    super("Step", Module.Category.Movement);
  }
  
  public void setup() {
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Normal");
    modes.add("Vanilla");
    this.height = registerDouble("Height", "Height", 2.5D, 0.5D, 2.5D);
    this.timer = registerBoolean("Timer", "Timer", false);
    this.reverse = registerBoolean("Reverse", "Reverse", false);
    this.mode = registerMode("Modes", "Modes", modes, "Normal");
  }
  
  public void onUpdate() {
    if (mc.world == null || mc.player == null || mc.player.isInWater() || mc.player.isInLava() || mc.player.isOnLadder() || mc.gameSettings.keyBindJump
      .isKeyDown())
      return; 
    if (ModuleManager.isModuleEnabled("Speed"))
      return; 
    if (this.mode.getValue().equalsIgnoreCase("Normal")) {
      if (this.timer.getValue())
        if (this.ticks == 0) {
          EntityUtil.resetTimer();
        } else {
          this.ticks--;
        }  
      if (mc.player != null && mc.player.onGround && !mc.player.isInWater() && !mc.player.isOnLadder() && this.reverse.getValue())
        for (double y = 0.0D; y < this.height.getValue() + 0.5D; y += 0.01D) {
          if (!mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(0.0D, -y, 0.0D)).isEmpty()) {
            mc.player.motionY = -10.0D;
            break;
          } 
        }  
      double[] dir = MotionUtils.forward(0.1D);
      boolean twofive = false;
      boolean two = false;
      boolean onefive = false;
      boolean one = false;
      if (mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.6D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.4D, dir[1])).isEmpty())
        twofive = true; 
      if (mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 2.1D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.9D, dir[1])).isEmpty())
        two = true; 
      if (mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.6D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.4D, dir[1])).isEmpty())
        onefive = true; 
      if (mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 1.0D, dir[1])).isEmpty() && !mc.world.getCollisionBoxes((Entity)mc.player, mc.player.getEntityBoundingBox().offset(dir[0], 0.6D, dir[1])).isEmpty())
        one = true; 
      if (mc.player.collidedHorizontally && (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) && mc.player.onGround) {
        if (one && this.height.getValue() >= 1.0D) {
          double[] oneOffset = { 0.42D, 0.753D };
          for (int i = 0; i < oneOffset.length; i++)
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneOffset[i], mc.player.posZ, mc.player.onGround)); 
          if (this.timer.getValue())
            EntityUtil.setTimer(0.6F); 
          mc.player.setPosition(mc.player.posX, mc.player.posY + 1.0D, mc.player.posZ);
          this.ticks = 1;
        } 
        if (onefive && this.height.getValue() >= 1.5D) {
          double[] oneFiveOffset = { 0.42D, 0.75D, 1.0D, 1.16D, 1.23D, 1.2D };
          for (int i = 0; i < oneFiveOffset.length; i++)
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + oneFiveOffset[i], mc.player.posZ, mc.player.onGround)); 
          if (this.timer.getValue())
            EntityUtil.setTimer(0.35F); 
          mc.player.setPosition(mc.player.posX, mc.player.posY + 1.5D, mc.player.posZ);
          this.ticks = 1;
        } 
        if (two && this.height.getValue() >= 2.0D) {
          double[] twoOffset = { 0.42D, 0.78D, 0.63D, 0.51D, 0.9D, 1.21D, 1.45D, 1.43D };
          for (int i = 0; i < twoOffset.length; i++)
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoOffset[i], mc.player.posZ, mc.player.onGround)); 
          if (this.timer.getValue())
            EntityUtil.setTimer(0.25F); 
          mc.player.setPosition(mc.player.posX, mc.player.posY + 2.0D, mc.player.posZ);
          this.ticks = 2;
        } 
        if (twofive && this.height.getValue() >= 2.5D) {
          double[] twoFiveOffset = { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D, 1.907D };
          for (int i = 0; i < twoFiveOffset.length; i++)
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + twoFiveOffset[i], mc.player.posZ, mc.player.onGround)); 
          if (this.timer.getValue())
            EntityUtil.setTimer(0.15F); 
          mc.player.setPosition(mc.player.posX, mc.player.posY + 2.5D, mc.player.posZ);
          this.ticks = 2;
        } 
      } 
    } 
    if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
      DecimalFormat df = new DecimalFormat("#");
      mc.player.stepHeight = Float.parseFloat(df.format(this.height.getValue()));
    } 
  }
  
  public void onDisable() {
    mc.player.stepHeight = 0.5F;
  }
  
  public String getHudInfo() {
    String t = "";
    if (this.mode.getValue().equalsIgnoreCase("Normal"))
      t = "[" + ChatFormatting.WHITE + "Normal" + ChatFormatting.GRAY + "]"; 
    if (this.mode.getValue().equalsIgnoreCase("Vanilla"))
      t = "[" + ChatFormatting.WHITE + "Vanilla" + ChatFormatting.GRAY + "]"; 
    return t;
  }
}
