//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.client.module.Module;
import java.util.Arrays;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class BruceAura extends Module {
  private Setting.Boolean attackPlayers = registerBoolean("Players", "Players", true);
  
  private Setting.Boolean attackMobs = registerBoolean("Mobs", "Mobs", false);
  
  private Setting.Boolean attackAnimals = registerBoolean("Animals", "Animals", false);
  
  private Setting.Boolean ignoreWalls = registerBoolean("Ignore Walls", "IgnoreWalls", true);
  
  private Setting.Boolean autoSwapReverted = registerBoolean("Auto Throw Reverted", "AutoThrowReverted", true);
  
  private Setting.Double hitRange = registerDouble("Hit Range", "HitRange", 5.5D, 0.0D, 25.0D);
  
  private Setting.Double waitTick = registerDouble("Tick Delay", "TickDelay", 2.0D, 0.0D, 20.0D);
  
  private Setting.Mode switchMode = registerMode("Auto Switch", "Autoswitch", Arrays.asList(new String[] { "NONE", "ALL", "Only32k" }, ), "Only32k");
  
  private Setting.Mode hitMode = registerMode("Hit Mode", "Tool", Arrays.asList(new String[] { "SWORD", "AXE", "Only32k" }, ), "SWORD");
  
  private int waitCounter;
  
  private int cached32kSlot = -1;
  
  public BruceAura() {
    super("Bruce Aura", Module.Category.Combat);
  }
  
  public void onEnable() {
    if (mc.player == null)
      return; 
  }
  
  public void onUpdate() {
    if (mc.player.isDead || mc.player == null)
      return; 
    boolean shield = (mc.player.getHeldItemOffhand().getItem().equals(Items.SHIELD) && mc.player.getActiveHand() == EnumHand.OFF_HAND);
    if (mc.player.isHandActive() && !shield)
      return; 
    if (this.waitTick.getValue() > 0.0D) {
      if (this.waitCounter < this.waitTick.getValue()) {
        this.waitCounter++;
        return;
      } 
      this.waitCounter = 0;
    } 
    if (this.autoSwapReverted.getValue() && this.switchMode.getValue().equals("Only32k")) {
      if (this.cached32kSlot == -1)
        return; 
      if (!is32kSword(mc.player.inventory.getStackInSlot(this.cached32kSlot)))
        mc.playerController.windowClick(0, this.cached32kSlot, 0, ClickType.THROW, (EntityPlayer)mc.player); 
      if (!(mc.currentScreen instanceof GuiContainer))
        return; 
      if ((((GuiContainer)mc.currentScreen).inventorySlots.getSlot(0).getStack()).isEmpty)
        return; 
      if (!is32kSword(((GuiContainer)mc.currentScreen).inventorySlots.getSlot(0).getStack()))
        return; 
      mc.playerController.windowClick(mc.player.openContainer.windowId, 0, mc.player.inventory.currentItem, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.windowClick(0, this.cached32kSlot, 0, ClickType.PICKUP, (EntityPlayer)mc.player);
      mc.playerController.updateController();
    } 
    Iterator<Entity> entityIterator = (Minecraft.getMinecraft()).world.loadedEntityList.iterator();
    while (entityIterator.hasNext()) {
      Entity target = entityIterator.next();
      if (!EntityUtil.isLiving(target))
        continue; 
      if (target == mc.player)
        continue; 
      if (mc.player.getDistance(target) > this.hitRange.getValue())
        continue; 
      if (((EntityLivingBase)target).getHealth() <= 0.0F)
        continue; 
      if (!this.ignoreWalls.getValue() && !mc.player.canEntityBeSeen(target) && !canEntityFeetBeSeen(target))
        continue; 
      if (this.attackPlayers.getValue() && target instanceof EntityPlayer && !Friends.isFriend(target.getName())) {
        attack(target);
        return;
      } 
      if (EntityUtil.isPassive(target) ? this.attackAnimals.getValue() : (EntityUtil.isMobAggressive(target) && this.attackMobs.getValue())) {
        attack(target);
        return;
      } 
    } 
  }
  
  private void attack(Entity e) {
    boolean holding32k = false;
    if ((this.switchMode.getValue().equals("Only32k") || this.switchMode.getValue().equals("ALL")) && !holding32k) {
      int newSlot = -1;
      for (int i = 0; i < 9; i++) {
        ItemStack stack = mc.player.inventory.getStackInSlot(i);
        if (stack != ItemStack.EMPTY)
          if (is32kSword(stack)) {
            newSlot = i;
            if (this.cached32kSlot == -1)
              this.cached32kSlot = i; 
            break;
          }  
      } 
      if (newSlot != -1) {
        mc.player.inventory.currentItem = newSlot;
        holding32k = true;
      } 
    } 
    if (this.switchMode.getValue().equals("Only32k") && !holding32k)
      return; 
    mc.playerController.attackEntity((EntityPlayer)mc.player, e);
    mc.player.swingArm(EnumHand.MAIN_HAND);
  }
  
  private boolean canEntityFeetBeSeen(Entity entityIn) {
    return (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null);
  }
  
  private boolean is32kSword(ItemStack stack) {
    if (stack.getTagCompound() == null)
      return false; 
    NBTTagList enchants = (NBTTagList)stack.getTagCompound().getTag("ench");
    if (enchants == null)
      return false; 
    for (int i = 0; i < enchants.tagCount(); i++) {
      NBTTagCompound enchant = enchants.getCompoundTagAt(i);
      if (enchant.getInteger("id") == 16) {
        int lvl = enchant.getInteger("lvl");
        if (lvl >= 42)
          return true; 
        break;
      } 
    } 
    return false;
  }
}
