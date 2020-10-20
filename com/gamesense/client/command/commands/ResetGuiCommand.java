package com.gamesense.client.command.commands;

import com.gamesense.client.AffinityPlus;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.command.Command;

public class ResetGuiCommand extends Command {
  public String[] getAlias() {
    return new String[] { "resetgui", "guireset" };
  }
  
  public String getSyntax() {
    return "resetgui";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    (AffinityPlus.getInstance()).clickGUI = new ClickGUI();
  }
}
