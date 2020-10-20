package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ToggleCommand extends Command {
  boolean found;
  
  public String[] getAlias() {
    return new String[] { "toggle", "t" };
  }
  
  public String getSyntax() {
    return "toggle <Module>";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    this.found = false;
    ModuleManager.getModules().forEach(m -> {
          if (m.getName().equalsIgnoreCase(args[0]))
            if (m.isEnabled()) {
              m.disable();
              this.found = true;
              Command.sendClientMessage(m.getName() + ChatFormatting.RED + " disabled!");
            } else if (!m.isEnabled()) {
              m.enable();
              this.found = true;
              Command.sendClientMessage(m.getName() + ChatFormatting.GREEN + " enabled!");
            }  
        });
    if (!this.found && args.length == 1)
      Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!"); 
  }
}
