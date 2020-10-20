package com.gamesense.client.command;

import com.gamesense.client.command.commands.AutoGgCommand;
import com.gamesense.client.command.commands.AutoReplyCommand;
import com.gamesense.client.command.commands.BindCommand;
import com.gamesense.client.command.commands.CmdsCommand;
import com.gamesense.client.command.commands.DrawnCommand;
import com.gamesense.client.command.commands.EnemyCommand;
import com.gamesense.client.command.commands.FontCommand;
import com.gamesense.client.command.commands.FriendCommand;
import com.gamesense.client.command.commands.MacroCommand;
import com.gamesense.client.command.commands.ModsCommand;
import com.gamesense.client.command.commands.OpenFolderCommand;
import com.gamesense.client.command.commands.PrefixCommand;
import com.gamesense.client.command.commands.ResetGuiCommand;
import com.gamesense.client.command.commands.SaveConfigCommand;
import com.gamesense.client.command.commands.SetSettingCommand;
import com.gamesense.client.command.commands.ToggleCommand;
import com.gamesense.client.command.commands.VanishCommand;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;

public class CommandManager {
  private static ArrayList<Command> commands;
  
  boolean b;
  
  public static void initCommands() {
    commands = new ArrayList<>();
    addCommand((Command)new AutoGgCommand());
    addCommand((Command)new AutoReplyCommand());
    addCommand((Command)new BindCommand());
    addCommand((Command)new CmdsCommand());
    addCommand((Command)new DrawnCommand());
    addCommand((Command)new EnemyCommand());
    addCommand((Command)new FontCommand());
    addCommand((Command)new FriendCommand());
    addCommand((Command)new MacroCommand());
    addCommand((Command)new ModsCommand());
    addCommand((Command)new OpenFolderCommand());
    addCommand((Command)new PrefixCommand());
    addCommand((Command)new ResetGuiCommand());
    addCommand((Command)new SaveConfigCommand());
    addCommand((Command)new SetSettingCommand());
    addCommand((Command)new ToggleCommand());
    addCommand((Command)new VanishCommand());
  }
  
  public static void addCommand(Command c) {
    commands.add(c);
  }
  
  public static ArrayList<Command> getCommands() {
    return commands;
  }
  
  public void callCommand(String input) {
    String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    String command = split[0];
    String args = input.substring(command.length()).trim();
    this.b = false;
    commands.forEach(c -> {
          for (String s : c.getAlias()) {
            if (s.equalsIgnoreCase(command)) {
              this.b = true;
              try {
                c.onCommand(args, args.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
              } catch (Exception e) {
                Command.sendClientMessage(ChatFormatting.GRAY + c.getSyntax());
              } 
            } 
          } 
        });
    if (!this.b)
      Command.sendClientMessage(ChatFormatting.GRAY + "Unknown command!"); 
  }
}
