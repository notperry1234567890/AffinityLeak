//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;

public class DiscordRPCModule extends Module {
  public final Setting.Integer updateDelay = registerInteger("Update Delay", "UpdateDelay", 30, 1, 1000);
  
  private boolean connected = false;
  
  private final DiscordRPC rpc = DiscordRPC.INSTANCE;
  
  private final DiscordRichPresence presence = new DiscordRichPresence();
  
  public DiscordRPCModule() {
    super("DiscordRPC", Module.Category.Misc);
  }
  
  protected void onEnable() {
    if (this.connected)
      return; 
    this.connected = true;
    this.rpc.Discord_Initialize("756374885139218473", new DiscordEventHandlers(), true, null);
    this.presence.startTimestamp = System.currentTimeMillis() / 1000L;
    new Thread(() -> rpcUpdate(), "DiscordRPCHandler");
  }
  
  protected void onDisable() {
    this.rpc.Discord_Shutdown();
  }
  
  private void rpcUpdate() {
    while (this.connected) {
      try {
        this.presence.details = ("Vibing on " + mc.getConnection() != null) ? mc.currentServerData.serverIP : "the Main Menu";
        this.presence.state = getPresenceState() + " | Affinity+ On Top!";
        this.rpc.Discord_UpdatePresence(this.presence);
        Thread.sleep(this.updateDelay.getValue() * 1000L);
      } catch (Exception exception) {}
    } 
  }
  
  private String getPresenceState() {
    return (mc.player == null) ? "Player not Found." : mc.player.getName();
  }
}
