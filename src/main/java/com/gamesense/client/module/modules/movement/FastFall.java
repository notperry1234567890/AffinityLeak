//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.movement;

import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class FastFall extends Module {
  private final double[] oneblockPositions = new double[] { 0.42D, 0.75D };
  
  private int packets;
  
  private boolean jumped;
  
  public FastFall() {
    super("FastFall", Module.Category.Movement);
  }
  
  public void onUpdate() {
    if (mc.world == null || mc.player == null || ModuleManager.isModuleEnabled("Speed"))
      return; 
    if (!mc.player.onGround) {
      if (mc.gameSettings.keyBindJump.isKeyDown())
        this.jumped = true; 
    } else {
      this.jumped = false;
    } 
    if (!this.jumped && mc.player.fallDistance < 0.5D && isInHole() && mc.player.posY - getNearestBlockBelow() <= 1.125D && mc.player.posY - getNearestBlockBelow() <= 0.95D && !isOnLiquid() && !isInLiquid()) {
      if (!mc.player.onGround)
        this.packets++; 
      if (!mc.player.onGround && !mc.player.isInsideOfMaterial(Material.WATER) && !mc.player.isInsideOfMaterial(Material.LAVA) && !mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isOnLadder() && this.packets > 0) {
        BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        for (double position : this.oneblockPositions)
          mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position((blockPos.getX() + 0.5F), mc.player.posY - position, (blockPos.getZ() + 0.5F), true)); 
        mc.player.setPosition((blockPos.getX() + 0.5F), getNearestBlockBelow() + 0.1D, (blockPos.getZ() + 0.5F));
        this.packets = 0;
      } 
    } 
  }
  
  private boolean isInHole() {
    BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
    IBlockState blockState = mc.world.getBlockState(blockPos);
    return isBlockValid(blockState, blockPos);
  }
  
  private double getNearestBlockBelow() {
    for (double y = mc.player.posY; y > 0.0D; y -= 0.001D) {
      if (!(mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock() instanceof net.minecraft.block.BlockSlab) && mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock().getDefaultState().getCollisionBoundingBox((IBlockAccess)mc.world, new BlockPos(0, 0, 0)) != null)
        return y; 
    } 
    return -1.0D;
  }
  
  private boolean isBlockValid(IBlockState blockState, BlockPos blockPos) {
    return (blockState.getBlock() == Blocks.AIR && mc.player.getDistanceSq(blockPos) >= 1.0D && mc.world.getBlockState(blockPos.up()).getBlock() == Blocks.AIR && mc.world.getBlockState(blockPos.up(2)).getBlock() == Blocks.AIR && (isBedrockHole(blockPos) || isObbyHole(blockPos) || isBothHole(blockPos) || isElseHole(blockPos)));
  }
  
  private boolean isObbyHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos touching : array) {
      IBlockState touchingState = mc.world.getBlockState(touching);
      if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN)
        return false; 
    } 
    return true;
  }
  
  private boolean isBedrockHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos touching : array) {
      IBlockState touchingState = mc.world.getBlockState(touching);
      if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK)
        return false; 
    } 
    return true;
  }
  
  private boolean isBothHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos touching : array) {
      IBlockState touchingState = mc.world.getBlockState(touching);
      if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN))
        return false; 
    } 
    return true;
  }
  
  private boolean isElseHole(BlockPos blockPos) {
    BlockPos[] array = { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }, touchingBlocks = array;
    for (BlockPos touching : array) {
      IBlockState touchingState = mc.world.getBlockState(touching);
      if (touchingState.getBlock() == Blocks.AIR || !touchingState.isFullBlock())
        return false; 
    } 
    return true;
  }
  
  private boolean isOnLiquid() {
    double y = mc.player.posY - 0.03D;
    for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++) {
      for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++) {
        BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
        if (mc.world.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockLiquid)
          return true; 
      } 
    } 
    return false;
  }
  
  private boolean isInLiquid() {
    double y = mc.player.posY + 0.01D;
    for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++) {
      for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++) {
        BlockPos pos = new BlockPos(x, (int)y, z);
        if (mc.world.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockLiquid)
          return true; 
      } 
    } 
    return false;
  }
}
