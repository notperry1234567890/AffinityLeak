//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class VanishCommand extends Command {
  private static Entity vehicle;
  
  Minecraft mc = Minecraft.getMinecraft();
  
  public String[] getAlias() {
    return new String[] { "vanish", "v" };
  }
  
  public String getSyntax() {
    return "vanish to do the epic gamer dupe";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    if (this.mc.player.getRidingEntity() != null && vehicle == null) {
      vehicle = this.mc.player.getRidingEntity();
      this.mc.player.dismountRidingEntity();
      this.mc.world.removeEntityFromWorld(vehicle.getEntityId());
      Command.sendClientMessage("Vehicle " + vehicle.getName() + " removed.");
    } else if (vehicle != null) {
      vehicle.isDead = false;
      this.mc.world.addEntityToWorld(vehicle.getEntityId(), vehicle);
      this.mc.player.startRiding(vehicle, true);
      Command.sendClientMessage("Vehicle " + vehicle.getName() + " created.");
      vehicle = null;
    } else {
      Command.sendClientMessage("No Vehicle.");
    } 
  }
}
