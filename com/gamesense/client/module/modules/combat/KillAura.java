//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class KillAura extends Module {
  Setting.Boolean players;
  
  Setting.Boolean hostileMobs;
  
  Setting.Boolean passiveMobs;
  
  Setting.Boolean swordOnly;
  
  Setting.Boolean caCheck;
  
  Setting.Boolean criticals;
  
  Setting.Double range;
  
  private boolean isAttacking = false;
  
  @EventHandler
  private final Listener<PacketEvent.Send> listener;
  
  public KillAura() {
    super("KillAura", Module.Category.Combat);
    this.listener = new Listener(event -> {
          if (event.getPacket() instanceof CPacketUseEntity && this.criticals.getValue() && ((CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround && this.isAttacking) {
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.10000000149011612D, mc.player.posZ, false));
            mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.players = registerBoolean("Players", "Players", true);
    this.passiveMobs = registerBoolean("Animals", "Animals", false);
    this.hostileMobs = registerBoolean("Monsters", "Monsters", false);
    this.range = registerDouble("Range", "Range", 5.0D, 0.0D, 10.0D);
    this.swordOnly = registerBoolean("Sword Only", "SwordOnly", true);
    this.criticals = registerBoolean("Criticals", "Criticals", true);
    this.caCheck = registerBoolean("AC Check", "ACCheck", false);
  }
  
  public void onUpdate() {
    if (mc.player == null || mc.player.isDead)
      return; 
    List<Entity> targets = (List<Entity>)mc.world.loadedEntityList.stream().filter(entity -> (entity != mc.player)).filter(entity -> (mc.player.getDistance(entity) <= this.range.getValue())).filter(entity -> !entity.isDead).filter(entity -> attackCheck(entity)).sorted(Comparator.comparing(e -> Float.valueOf(mc.player.getDistance(e)))).collect(Collectors.toList());
    targets.forEach(target -> {
          if (this.swordOnly.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemSword))
            return; 
          if (this.caCheck.getValue() && ((AutoCrystal)ModuleManager.getModuleByName("AutoCrystalGS")).isActive)
            return; 
          attack(target);
        });
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
  
  public void attack(Entity e) {
    if (mc.player.getCooledAttackStrength(0.0F) >= 1.0F) {
      this.isAttacking = true;
      mc.playerController.attackEntity((EntityPlayer)mc.player, e);
      mc.player.swingArm(EnumHand.MAIN_HAND);
      this.isAttacking = false;
    } 
  }
  
  private boolean attackCheck(Entity entity) {
    if (this.players.getValue() && entity instanceof EntityPlayer && !Friends.isFriend(entity.getName()) && (
      (EntityPlayer)entity).getHealth() > 0.0F)
      return true; 
    if (this.passiveMobs.getValue() && entity instanceof net.minecraft.entity.passive.EntityAnimal)
      return !(entity instanceof net.minecraft.entity.passive.EntityTameable); 
    return (this.hostileMobs.getValue() && entity instanceof net.minecraft.entity.monster.EntityMob);
  }
}
