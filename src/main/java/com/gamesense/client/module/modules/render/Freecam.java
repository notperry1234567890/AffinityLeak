//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.PlayerMoveEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;

public class Freecam extends Module {
  @EventHandler
  private final Listener<PlayerMoveEvent> moveListener = new Listener(event -> mc.player.noClip = true, new java.util.function.Predicate[0]);
  
  @EventHandler
  private final Listener<PlayerSPPushOutOfBlocksEvent> pushListener;
  
  Setting.Boolean cancelPackets;
  
  @EventHandler
  private final Listener<PacketEvent.Send> sendListener;
  
  Setting.Double speed;
  
  private double posX;
  
  private double posY;
  
  private double posZ;
  
  private float pitch;
  
  private float yaw;
  
  private EntityOtherPlayerMP clonedPlayer;
  
  private boolean isRidingEntity;
  
  private Entity ridingEntity;
  
  public Freecam() {
    super("Freecam", Module.Category.Render);
    this.pushListener = new Listener(event -> event.setCanceled(true), new java.util.function.Predicate[0]);
    this.sendListener = new Listener(event -> {
          if ((event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayer || event.getPacket() instanceof net.minecraft.network.play.client.CPacketInput) && this.cancelPackets.getValue())
            event.cancel(); 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.cancelPackets = registerBoolean("Cancel Packets", "CancelPackets", true);
    this.speed = registerDouble("Speed", "Speed", 5.0D, 0.0D, 10.0D);
  }
  
  protected void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
    if (mc.player != null) {
      this.isRidingEntity = (mc.player.getRidingEntity() != null);
      if (mc.player.getRidingEntity() == null) {
        this.posX = mc.player.posX;
        this.posY = mc.player.posY;
        this.posZ = mc.player.posZ;
      } else {
        this.ridingEntity = mc.player.getRidingEntity();
        mc.player.dismountRidingEntity();
      } 
      this.pitch = mc.player.rotationPitch;
      this.yaw = mc.player.rotationYaw;
      this.clonedPlayer = new EntityOtherPlayerMP((World)mc.world, mc.getSession().getProfile());
      this.clonedPlayer.copyLocationAndAnglesFrom((Entity)mc.player);
      this.clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
      mc.world.addEntityToWorld(-100, (Entity)this.clonedPlayer);
      mc.player.capabilities.isFlying = true;
      mc.player.capabilities.setFlySpeed((float)(this.speed.getValue() / 100.0D));
      mc.player.noClip = true;
    } 
  }
  
  protected void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
    EntityPlayerSP entityPlayerSP = mc.player;
    mc.player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.yaw, this.pitch);
    mc.world.removeEntityFromWorld(-100);
    this.clonedPlayer = null;
    this.posX = this.posY = this.posZ = 0.0D;
    this.pitch = this.yaw = 0.0F;
    mc.player.capabilities.isFlying = false;
    mc.player.capabilities.setFlySpeed(0.05F);
    mc.player.noClip = false;
    mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0.0D;
    if (entityPlayerSP != null && this.isRidingEntity)
      mc.player.startRiding(this.ridingEntity, true); 
  }
  
  public void onUpdate() {
    mc.player.capabilities.isFlying = true;
    mc.player.capabilities.setFlySpeed((float)(this.speed.getValue() / 100.0D));
    mc.player.noClip = true;
    mc.player.onGround = false;
    mc.player.fallDistance = 0.0F;
  }
}
