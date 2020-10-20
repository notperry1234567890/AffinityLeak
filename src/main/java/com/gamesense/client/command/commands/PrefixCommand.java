package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;

public class PrefixCommand extends Command {
  public String[] getAlias() {
    return new String[] { "prefix", "setprefix" };
  }
  
  public String getSyntax() {
    return "prefix <character>";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    Command.setPrefix(args[0]);
    Command.sendClientMessage("Command prefix set to " + Command.getPrefix());
  }
}
