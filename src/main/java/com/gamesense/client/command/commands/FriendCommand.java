package com.gamesense.client.command.commands;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;

public class FriendCommand extends Command {
  public String[] getAlias() {
    return new String[] { "friend", "friends", "f" };
  }
  
  public String getSyntax() {
    return "friend <add | del> <Name>";
  }
  
  public void onCommand(String command, String[] args) throws Exception {
    if (args[0].equalsIgnoreCase("add")) {
      if (Friends.isFriend(args[1])) {
        Command.sendClientMessage(args[1] + ChatFormatting.GRAY + " is already a friend!");
        return;
      } 
      if (!Friends.isFriend(args[1])) {
        (AffinityPlus.getInstance()).friends.addFriend(args[1]);
        Command.sendClientMessage("Added " + args[1] + " to friends list");
      } 
    } 
    if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
      if (!Friends.isFriend(args[1])) {
        Command.sendClientMessage(args[1] + " is not a friend!");
        return;
      } 
      if (Friends.isFriend(args[1])) {
        (AffinityPlus.getInstance()).friends.delFriend(args[1]);
        Command.sendClientMessage("Removed " + args[1] + " from friends list");
      } 
    } 
  }
}
