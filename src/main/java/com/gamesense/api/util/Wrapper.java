//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {
  public static Minecraft mc = Minecraft.getMinecraft();
  
  private static FontRenderer fontRenderer;
  
  public static Minecraft getMinecraft() {
    return Minecraft.getMinecraft();
  }
  
  public static EntityPlayerSP getPlayer() {
    return (getMinecraft()).player;
  }
  
  public static World getWorld() {
    return (World)(getMinecraft()).world;
  }
  
  public static int getKey(String keyname) {
    return Keyboard.getKeyIndex(keyname.toUpperCase());
  }
  
  public static FontRenderer getFontRenderer() {
    return fontRenderer;
  }
}
