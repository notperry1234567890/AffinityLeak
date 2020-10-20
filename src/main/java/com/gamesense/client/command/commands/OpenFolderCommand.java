package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;
import java.awt.Desktop;
import java.io.File;

public class OpenFolderCommand extends Command {
  public String[] getAlias() {
    return new String[] { "openfolder", "folder" };
  }
  
  public String getSyntax() {
    return "openfolder";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    try {
      Desktop.getDesktop().open(new File("GameSense"));
    } catch (Exception e) {
      sendClientMessage("Error: " + e.getMessage());
    } 
  }
}
