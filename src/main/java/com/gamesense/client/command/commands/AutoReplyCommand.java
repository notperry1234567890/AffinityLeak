package com.gamesense.client.command.commands;

import com.gamesense.client.command.Command;
import com.gamesense.client.module.modules.misc.AutoReply;

public class AutoReplyCommand extends Command {
  public String[] getAlias() {
    return new String[] { "autoreply" };
  }
  
  public String getSyntax() {
    return "autoreply <message (use \"_\" for spaces)>";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    if (args[0] != null && !args[0].equalsIgnoreCase("")) {
      AutoReply.setReply(args[0].replace("_", " "));
      Command.sendClientMessage("AutoReply message set to " + AutoReply.getReply());
    } else {
      Command.sendClientMessage(getSyntax());
    } 
  }
}
