//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

public class MCF extends Module {
  @EventHandler
  private final Listener<InputEvent.MouseInputEvent> listener = new Listener(event -> {
        if (mc.objectMouseOver.typeOfHit.equals(RayTraceResult.Type.ENTITY) && mc.objectMouseOver.entityHit instanceof net.minecraft.entity.player.EntityPlayer && Mouse.getEventButton() == 2)
          if (Friends.isFriend(mc.objectMouseOver.entityHit.getName())) {
            (AffinityPlus.getInstance()).friends.delFriend(mc.objectMouseOver.entityHit.getName());
            Command.sendClientMessage(ChatFormatting.RED + "Removed " + mc.objectMouseOver.entityHit.getName() + " from friends list");
          } else {
            (AffinityPlus.getInstance()).friends.addFriend(mc.objectMouseOver.entityHit.getName());
            Command.sendClientMessage(ChatFormatting.GREEN + "Added " + mc.objectMouseOver.entityHit.getName() + " to friends list");
          }  
      }new java.util.function.Predicate[0]);
  
  public MCF() {
    super("MCF", Module.Category.Misc);
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
