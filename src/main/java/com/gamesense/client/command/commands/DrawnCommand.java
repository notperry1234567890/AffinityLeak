package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;

public class DrawnCommand extends Command {
  boolean found;
  
  public String[] getAlias() {
    return new String[] { "drawn", "visible", "d" };
  }
  
  public String getSyntax() {
    return "drawn <Module>";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    this.found = false;
    ModuleManager.getModules().forEach(m -> {
          if (m.getName().equalsIgnoreCase(args[0]))
            if (m.isDrawn()) {
              m.setDrawn(false);
              this.found = true;
              Command.sendClientMessage(m.getName() + ChatFormatting.RED + " drawn = false");
            } else if (!m.isDrawn()) {
              m.setDrawn(true);
              this.found = true;
              Command.sendClientMessage(m.getName() + ChatFormatting.GREEN + " drawn = true");
            }  
        });
    if (!this.found && args.length == 1)
      Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!"); 
  }
}
