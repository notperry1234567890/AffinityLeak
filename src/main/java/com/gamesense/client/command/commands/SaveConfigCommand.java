package com.gamesense.client.command.commands;

import com.gamesense.api.Stopper;
import com.gamesense.client.command.Command;

public class SaveConfigCommand extends Command {
  public String[] getAlias() {
    return new String[] { "saveconfig", "savecfg" };
  }
  
  public String getSyntax() {
    return "saveconfig";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    Stopper.saveConfig();
    Command.sendClientMessage("Config saved!");
  }
}
