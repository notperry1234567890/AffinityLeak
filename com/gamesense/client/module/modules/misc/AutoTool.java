//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.event.events.DamageBlockEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

public class AutoTool extends Module {
  @EventHandler
  private final Listener<DamageBlockEvent> leftClickListener;
  
  Setting.Boolean switchBack;
  
  boolean shouldMoveBack;
  
  int lastSlot;
  
  long lastChange;
  
  public AutoTool() {
    super("AutoTool", Module.Category.Misc);
    this.leftClickListener = new Listener(event -> equipBestTool(mc.world.getBlockState(event.getPos())), new java.util.function.Predicate[0]);
    this.shouldMoveBack = false;
    this.lastSlot = 0;
    this.lastChange = 0L;
  }
  
  private static void equip(int slot) {
    mc.player.inventory.currentItem = slot;
    mc.playerController.syncCurrentPlayItem();
  }
  
  public void setup() {
    this.switchBack = registerBoolean("Switch Back", "SwitchBack", false);
  }
  
  public void onUpdate() {
    if (!this.switchBack.getValue())
      this.shouldMoveBack = false; 
    if (mc.currentScreen != null || !this.switchBack.getValue())
      return; 
    boolean mouse = Mouse.isButtonDown(0);
    if (mouse && !this.shouldMoveBack) {
      this.lastChange = System.currentTimeMillis();
      this.shouldMoveBack = true;
      this.lastSlot = mc.player.inventory.currentItem;
      mc.playerController.syncCurrentPlayItem();
    } else if (!mouse && this.shouldMoveBack) {
      this.shouldMoveBack = false;
      mc.player.inventory.currentItem = this.lastSlot;
      mc.playerController.syncCurrentPlayItem();
    } 
  }
  
  private void equipBestTool(IBlockState blockState) {
    int bestSlot = -1;
    double max = 0.0D;
    for (int i = 0; i < 9; i++) {
      ItemStack stack = mc.player.inventory.getStackInSlot(i);
      if (!stack.isEmpty()) {
        float speed = stack.getDestroySpeed(blockState);
        if (speed > 1.0F) {
          int eff;
          speed = (float)(speed + (((eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)) > 0) ? (Math.pow(eff, 2.0D) + 1.0D) : 0.0D));
          if (speed > max) {
            max = speed;
            bestSlot = i;
          } 
        } 
      } 
    } 
    if (bestSlot != -1)
      equip(bestSlot); 
  }
  
  public void onEnable() {
    AffinityPlus.EVENT_BUS.subscribe(this);
  }
  
  public void onDisable() {
    AffinityPlus.EVENT_BUS.unsubscribe(this);
  }
}
