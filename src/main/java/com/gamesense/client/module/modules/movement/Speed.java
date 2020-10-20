//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.api.event.events.PlayerMoveEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.api.util.world.MotionUtils;
import com.gamesense.api.util.world.Timer;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class Speed extends Module {
  public static boolean doSlow;
  
  public Timer waitTimer = new Timer();
  
  int waitCounter;
  
  int forward = 1;
  
  Setting.Boolean ice;
  
  Setting.Mode Mode;
  
  Setting.Double speed;
  
  private double moveSpeed;
  
  @EventHandler
  private final Listener<PlayerMoveEvent> listener;
  
  public Speed() {
    super("Speed", Module.Category.Movement);
    this.listener = new Listener(event -> {
          boolean icee = (this.ice.getValue() && (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockPackedIce));
          if (icee)
            return; 
          if (mc.player.isInLava() || mc.player.isInWater() || mc.player.isOnLadder())
            return; 
          if (this.Mode.getValue().equalsIgnoreCase("Strafe")) {
            double motionY = 0.41999998688697815D;
            if (mc.player.onGround && MotionUtils.isMoving((EntityLivingBase)mc.player) && this.waitTimer.hasReached(300L)) {
              if (mc.player.isPotionActive(MobEffects.JUMP_BOOST))
                motionY += ((mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F); 
              event.setY(mc.player.motionY = motionY);
              this.moveSpeed = MotionUtils.getBaseMoveSpeed() * ((EntityUtil.isColliding(0.0D, -0.5D, 0.0D) instanceof net.minecraft.block.BlockLiquid && !EntityUtil.isInLiquid()) ? 0.9D : 1.901D);
              doSlow = true;
              this.waitTimer.reset();
            } else if (doSlow || mc.player.collidedHorizontally) {
              this.moveSpeed -= (EntityUtil.isColliding(0.0D, -0.8D, 0.0D) instanceof net.minecraft.block.BlockLiquid && !EntityUtil.isInLiquid()) ? 0.4D : (0.7D * (this.moveSpeed = MotionUtils.getBaseMoveSpeed()));
              doSlow = false;
            } else {
              this.moveSpeed -= this.moveSpeed / 159.0D;
            } 
            this.moveSpeed = Math.max(this.moveSpeed, MotionUtils.getBaseMoveSpeed());
            double[] dir = MotionUtils.forward(this.moveSpeed);
            event.setX(dir[0]);
            event.setZ(dir[1]);
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.ice = registerBoolean("Ice", "Ice", true);
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Strafe");
    modes.add("YPort");
    modes.add("Packet");
    modes.add("Packet2");
    modes.add("FakeStrafe");
    this.speed = registerDouble("Speed", "Speed", 8.0D, 0.0D, 10.0D);
    this.Mode = registerMode("Modes", "Modes", modes, "Strafe");
  }
  
  public void onEnable() {
    this.moveSpeed = MotionUtils.getBaseMoveSpeed();
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onUpdate() {
    boolean icee = (this.ice.getValue() && (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockPackedIce));
    if (icee)
      MotionUtils.setSpeed((EntityLivingBase)mc.player, MotionUtils.getBaseMoveSpeed() + (mc.player.isPotionActive(MobEffects.SPEED) ? ((mc.player.ticksExisted % 2 == 0) ? 0.7D : 0.1D) : 0.4D)); 
    if (!icee) {
      if ((this.Mode.getValue().equalsIgnoreCase("Packet") || this.Mode.getValue().equalsIgnoreCase("Packet2")) && 
        MotionUtils.isMoving((EntityLivingBase)mc.player) && mc.player.onGround) {
        boolean step = ModuleManager.isModuleEnabled("Step");
        double posX = mc.player.posX;
        double posY = mc.player.posY;
        double posZ = mc.player.posZ;
        boolean ground = mc.player.onGround;
        double[] dir1 = MotionUtils.forward(0.5D);
        BlockPos pos = new BlockPos(posX + dir1[0], posY, posZ + dir1[1]);
        Block block = mc.world.getBlockState(pos).getBlock();
        if (step && !(block instanceof net.minecraft.block.BlockAir)) {
          MotionUtils.setSpeed((EntityLivingBase)mc.player, 0.0D);
          return;
        } 
        if (mc.world.getBlockState(new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ())).getBlock() instanceof net.minecraft.block.BlockAir)
          return; 
        double x;
        for (x = 0.0625D; x < this.speed.getValue(); x += 0.262D) {
          double[] dir = MotionUtils.forward(x);
          mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX + dir[0], posY, posZ + dir[1], ground));
        } 
        if (this.Mode.getValue().equalsIgnoreCase("Packet2"))
          MotionUtils.setSpeed((EntityLivingBase)mc.player, 2.0D); 
        mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(posX + mc.player.motionX, (mc.player.posY <= 10.0D) ? 255.0D : 1.0D, posZ + mc.player.motionZ, ground));
      } 
      if (this.Mode.getValue().equalsIgnoreCase("YPort")) {
        if (!MotionUtils.isMoving((EntityLivingBase)mc.player) || (mc.player.isInWater() && mc.player.isInLava()) || mc.player.collidedHorizontally)
          return; 
        if (mc.player.onGround) {
          EntityUtil.setTimer(1.15F);
          mc.player.jump();
          boolean ice = (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockIce || mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockPackedIce);
          MotionUtils.setSpeed((EntityLivingBase)mc.player, MotionUtils.getBaseMoveSpeed() + (ice ? 0.3D : 0.06D));
        } else {
          mc.player.motionY = -1.0D;
          EntityUtil.resetTimer();
        } 
      } 
    } 
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
    EntityUtil.resetTimer();
  }
  
  public String getHudInfo() {
    String t = "";
    if (this.Mode.getValue().equalsIgnoreCase("Strafe"))
      t = "[" + ChatFormatting.WHITE + "Strafe" + ChatFormatting.GRAY + "]"; 
    if (this.Mode.getValue().equalsIgnoreCase("YPort"))
      t = "[" + ChatFormatting.WHITE + "YPort" + ChatFormatting.GRAY + "]"; 
    if (this.Mode.getValue().equalsIgnoreCase("Packet"))
      t = "[" + ChatFormatting.WHITE + "Packet" + ChatFormatting.GRAY + "]"; 
    if (this.Mode.getValue().equalsIgnoreCase("Packet2"))
      t = "[" + ChatFormatting.WHITE + "Packet2" + ChatFormatting.GRAY + "]"; 
    if (this.Mode.getValue().equalsIgnoreCase("FakeStrafe"))
      t = "[" + ChatFormatting.WHITE + "Strafe" + ChatFormatting.GRAY + "]"; 
    return t;
  }
}
