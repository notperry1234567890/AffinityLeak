//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.client.module.Module;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class FakePlayer extends Module {
  EntityOtherPlayerMP fakePlayer;
  
  public FakePlayer() {
    super("FakePlayer", Module.Category.Misc);
    MinecraftForge.EVENT_BUS.register(this);
  }
  
  protected void onEnable() {
    this.fakePlayer = new EntityOtherPlayerMP((World)mc.world, new GameProfile(UUID.fromString("e4ea5edb-e317-433f-ac90-ef304841d8c8"), "skidmaster"));
    mc.world.addEntityToWorld(this.fakePlayer.entityId, (Entity)this.fakePlayer);
    this.fakePlayer.attemptTeleport(mc.player.posX, mc.player.posY, mc.player.posZ);
  }
  
  @SubscribeEvent
  public void onChat(ClientChatEvent event) {
    if (event.getMessage().equalsIgnoreCase("fakeplayer jump")) {
      event.setCanceled(true);
      this.fakePlayer.jump();
    } 
  }
  
  protected void onDisable() {
    try {
      mc.world.removeEntity((Entity)this.fakePlayer);
    } catch (Exception exception) {}
  }
}
