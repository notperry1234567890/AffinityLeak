//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.DestroyBlockEvent;
import com.gamesense.api.event.events.PacketEvent;
import com.gamesense.api.event.events.PlayerJumpEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class Announcer extends Module {
  public static int blockBrokeDelay = 0;
  
  public static String walkMessage = "I just walked{blocks} meters thanks to Affinity+!";
  
  public static String placeMessage = "I just inserted{amount}{name} into the muliverse thanks to Affinity+";
  
  public static String jumpMessage = "I just hovered in the air thanks to Affinity+!";
  
  public static String breakMessage = "I just snapped{amount}{name} out of existance thanks to Affinity+";
  
  public static String attackMessage = "I just disembowed{name} with a{item} thanks to Affinity+!";
  
  public static String eatMessage = "I just gobbled up{amount}{name} thanks to Affinity+!";
  
  public static String guiMessage = "I just opened my advanced hacking console thanks to Affinity+!";
  
  public static String[] walkMessages = new String[] { "I just walked{blocks} meters thanks to Affinity+!" };
  
  public static String[] placeMessages = new String[] { "I just inserted{amount}{name} into the muliverse thanks to Affinity+!" };
  
  public static String[] jumpMessages = new String[] { "I just hovered in the air thanks to Affinity+" };
  
  public static String[] breakMessages = new String[] { "I just snapped{amount}{name} out of existance thanks to Affinity+" };
  
  public static String[] eatMessages = new String[] { "I just ate{amount}{name} thanks to Affinity+" };
  
  static int blockPlacedDelay = 0;
  
  static int jumpDelay = 0;
  
  static int attackDelay = 0;
  
  static int eattingDelay = 0;
  
  static long lastPositionUpdate;
  
  static double lastPositionX;
  
  static double lastPositionY;
  
  static double lastPositionZ;
  
  private static double speed;
  
  public Setting.Boolean clientSide;
  
  public Setting.Boolean clickGui;
  
  String heldItem = "";
  
  int blocksPlaced = 0;
  
  int blocksBroken = 0;
  
  int eaten = 0;
  
  Setting.Boolean walk;
  
  Setting.Boolean place;
  
  Setting.Boolean jump;
  
  Setting.Boolean breaking;
  
  Setting.Boolean attack;
  
  Setting.Boolean eat;
  
  Setting.Integer delay;
  
  @EventHandler
  private final Listener<LivingEntityUseItemEvent.Finish> eatListener;
  
  @EventHandler
  private final Listener<PacketEvent.Send> sendListener;
  
  @EventHandler
  private final Listener<DestroyBlockEvent> destroyListener;
  
  @EventHandler
  private final Listener<AttackEntityEvent> attackListener;
  
  @EventHandler
  private final Listener<PlayerJumpEvent> jumpListener;
  
  public Announcer() {
    super("Announcer", Module.Category.Misc);
    this.eatListener = new Listener(event -> {
          int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
          if (event.getEntity() == mc.player && (event.getItem().getItem() instanceof net.minecraft.item.ItemFood || event.getItem().getItem() instanceof net.minecraft.item.ItemAppleGold)) {
            this.eaten++;
            if (eattingDelay >= 300 * this.delay.getValue() && this.eat.getValue() && this.eaten > randomNum) {
              Random random = new Random();
              if (this.clientSide.getValue()) {
                Command.sendClientMessage(eatMessages[random.nextInt(eatMessages.length)].replace("{amount}", this.eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
              } else {
                mc.player.sendChatMessage(eatMessages[random.nextInt(eatMessages.length)].replace("{amount}", this.eaten + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName()));
              } 
              this.eaten = 0;
              eattingDelay = 0;
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.sendListener = new Listener(event -> {
          if (event.getPacket() instanceof net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof net.minecraft.item.ItemBlock) {
            this.blocksPlaced++;
            int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            if (blockPlacedDelay >= 150 * this.delay.getValue() && this.place.getValue() && this.blocksPlaced > randomNum) {
              Random random = new Random();
              String msg = placeMessages[random.nextInt(placeMessages.length)].replace("{amount}", this.blocksPlaced + "").replace("{name}", mc.player.getHeldItemMainhand().getDisplayName());
              if (this.clientSide.getValue()) {
                Command.sendClientMessage(msg);
              } else {
                mc.player.sendChatMessage(msg);
              } 
              this.blocksPlaced = 0;
              blockPlacedDelay = 0;
            } 
          } 
        }new java.util.function.Predicate[0]);
    this.destroyListener = new Listener(event -> {
          this.blocksBroken++;
          int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
          if (blockBrokeDelay >= 300 * this.delay.getValue() && this.breaking.getValue() && this.blocksBroken > randomNum) {
            Random random = new Random();
            String msg = breakMessages[random.nextInt(breakMessages.length)].replace("{amount}", this.blocksBroken + "").replace("{name}", mc.world.getBlockState(event.getBlockPos()).getBlock().getLocalizedName());
            if (this.clientSide.getValue()) {
              Command.sendClientMessage(msg);
            } else {
              mc.player.sendChatMessage(msg);
            } 
            this.blocksBroken = 0;
            blockBrokeDelay = 0;
          } 
        }new java.util.function.Predicate[0]);
    this.attackListener = new Listener(event -> {
          if (this.attack.getValue() && !(event.getTarget() instanceof net.minecraft.entity.item.EntityEnderCrystal) && attackDelay >= 300 * this.delay.getValue()) {
            String msg = attackMessage.replace("{name}", event.getTarget().getName()).replace("{item}", mc.player.getHeldItemMainhand().getDisplayName());
            if (this.clientSide.getValue()) {
              Command.sendClientMessage(msg);
            } else {
              mc.player.sendChatMessage(msg);
            } 
            attackDelay = 0;
          } 
        }new java.util.function.Predicate[0]);
    this.jumpListener = new Listener(event -> {
          if (this.jump.getValue() && jumpDelay >= 300 * this.delay.getValue()) {
            if (this.clientSide.getValue()) {
              Random random = new Random();
              Command.sendClientMessage(jumpMessages[random.nextInt(jumpMessages.length)]);
            } else {
              Random random = new Random();
              mc.player.sendChatMessage(jumpMessages[random.nextInt(jumpMessages.length)]);
            } 
            jumpDelay = 0;
          } 
        }new java.util.function.Predicate[0]);
  }
  
  public void setup() {
    this.clientSide = registerBoolean("Client Side", "ClientSide", false);
    this.walk = registerBoolean("Walk", "Walk", true);
    this.place = registerBoolean("Place", "Place", true);
    this.jump = registerBoolean("Jump", "Jump", true);
    this.breaking = registerBoolean("Breaking", "Breaking", true);
    this.attack = registerBoolean("Attack", "Attack", true);
    this.eat = registerBoolean("Eat", "Eat", true);
    this.clickGui = registerBoolean("DevGUI", "DevGUI", true);
    this.delay = registerInteger("Delay", "Delay", 1, 1, 20);
  }
  
  public void onUpdate() {
    blockBrokeDelay++;
    blockPlacedDelay++;
    jumpDelay++;
    attackDelay++;
    eattingDelay++;
    this.heldItem = mc.player.getHeldItemMainhand().getDisplayName();
    if (this.walk.getValue() && 
      lastPositionUpdate + 5000L * this.delay.getValue() < System.currentTimeMillis()) {
      double d0 = lastPositionX - mc.player.lastTickPosX;
      double d2 = lastPositionY - mc.player.lastTickPosY;
      double d3 = lastPositionZ - mc.player.lastTickPosZ;
      speed = Math.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
      if (speed > 1.0D && speed <= 5000.0D) {
        String walkAmount = (new DecimalFormat("0.00")).format(speed);
        Random random = new Random();
        if (this.clientSide.getValue()) {
          Command.sendClientMessage(walkMessage.replace("{blocks}", walkAmount));
        } else {
          mc.player.sendChatMessage(walkMessages[random.nextInt(walkMessages.length)].replace("{blocks}", walkAmount));
        } 
        lastPositionUpdate = System.currentTimeMillis();
        lastPositionX = mc.player.lastTickPosX;
        lastPositionY = mc.player.lastTickPosY;
        lastPositionZ = mc.player.lastTickPosZ;
      } 
    } 
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
    this.blocksPlaced = 0;
    this.blocksBroken = 0;
    this.eaten = 0;
    speed = 0.0D;
    blockBrokeDelay = 0;
    blockPlacedDelay = 0;
    jumpDelay = 0;
    attackDelay = 0;
    eattingDelay = 0;
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
