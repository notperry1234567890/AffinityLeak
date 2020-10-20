//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.world.EntityUtil;
import com.gamesense.client.module.Module;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class YakgodAura extends Module {
  private Setting.Boolean attackPlayers;
  
  private Setting.Boolean attackMobs;
  
  private Setting.Boolean attackAnimals;
  
  private Setting.Boolean ignoreWalls;
  
  private Setting.Boolean switchTo32k;
  
  private Setting.Boolean onlyUse32k;
  
  private Setting.Double hitRange;
  
  private Setting.Integer waitTick;
  
  private int waitCounter;
  
  public YakgodAura() {
    super("YakgodAura", Module.Category.Combat);
    this.attackPlayers = registerBoolean("Players", "Players", true);
    this.attackMobs = registerBoolean("Mobs", "Mobs", false);
    this.attackAnimals = registerBoolean("Animals", "Animals", false);
    this.ignoreWalls = registerBoolean("Ignore Walls", "IgnoreWalls", true);
    this.switchTo32k = registerBoolean("32k Switch", "32kSwitch", true);
    this.onlyUse32k = registerBoolean("32k Only", "32kOnly", true);
    this.hitRange = registerDouble("Hit Range", "HitRange", 5.5D, 0.0D, 25.0D);
    this.waitTick = registerInteger("Tick Delay", "TickDelay", 3, 0, 20);
    this.waitCounter = 0;
  }
  
  public void onUpdate() {
    if (mc.player.isDead)
      return; 
    if (this.waitCounter < this.waitTick.getValue()) {
      this.waitCounter++;
      return;
    } 
    this.waitCounter = 0;
    Iterator<Entity> entityIterator = mc.world.loadedEntityList.iterator();
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
  
  private boolean checkSharpness(ItemStack stack) {
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
  
  private void attack(Entity e) {
    boolean holding32k = false;
    if (checkSharpness(mc.player.getHeldItemMainhand()))
      holding32k = true; 
    if (this.switchTo32k.getValue() && !holding32k) {
      int newSlot = -1;
      for (int i = 0; i < 9; i++) {
        ItemStack stack = mc.player.inventory.getStackInSlot(i);
        if (stack != ItemStack.EMPTY)
          if (checkSharpness(stack)) {
            newSlot = i;
            break;
          }  
      } 
      if (newSlot != -1) {
        mc.player.inventory.currentItem = newSlot;
        holding32k = true;
      } 
    } 
    if (this.onlyUse32k.getValue() && !holding32k)
      return; 
    mc.playerController.attackEntity((EntityPlayer)mc.player, e);
    mc.player.swingArm(EnumHand.MAIN_HAND);
  }
  
  private boolean canEntityFeetBeSeen(Entity entityIn) {
    return (mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(entityIn.posX, entityIn.posY, entityIn.posZ), false, true, false) == null);
  }
}
