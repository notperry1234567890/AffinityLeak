//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.combat;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.command.Command;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class HoleFill extends Module {
  private final List<Block> obbyonly = Arrays.asList(new Block[] { Blocks.OBSIDIAN });
  
  private final List<Block> bothonly = Arrays.asList(new Block[] { Blocks.OBSIDIAN, Blocks.ENDER_CHEST });
  
  private final List<Block> echestonly = Arrays.asList(new Block[] { Blocks.ENDER_CHEST });
  
  private final List<Block> webonly = Arrays.asList(new Block[] { Blocks.WEB });
  
  Setting.Double range;
  
  Setting.Integer yRange;
  
  Setting.Integer waitTick;
  
  Setting.Boolean chat;
  
  Setting.Boolean rotate;
  
  Setting.Mode type;
  
  BlockPos pos;
  
  private ArrayList<BlockPos> holes = new ArrayList<>();
  
  private List<Block> list = Arrays.asList(new Block[0]);
  
  private int waitCounter;
  
  public HoleFill() {
    super("HoleFill", Module.Category.Combat);
  }
  
  public static boolean placeBlockScaffold(BlockPos pos, boolean rotate) {
    Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    EnumFacing[] arrayOfEnumFacing;
    int i;
    byte b;
    for (arrayOfEnumFacing = EnumFacing.values(), i = arrayOfEnumFacing.length, b = 0; b < i; ) {
      EnumFacing side = arrayOfEnumFacing[b];
      BlockPos neighbor = pos.offset(side);
      EnumFacing side2 = side.getOpposite();
      if (!canBeClicked(neighbor)) {
        b++;
        continue;
      } 
      Vec3d hitVec = (new Vec3d((Vec3i)neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
      if (rotate)
        faceVectorPacketInstant(hitVec); 
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.START_SNEAKING));
      processRightClickBlock(neighbor, side2, hitVec);
      mc.player.swingArm(EnumHand.MAIN_HAND);
      mc.rightClickDelayTimer = 0;
      mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
      return true;
    } 
    return false;
  }
  
  public static boolean canBeClicked(BlockPos pos) {
    return getBlock(pos).canCollideCheck(getState(pos), false);
  }
  
  public static IBlockState getState(BlockPos pos) {
    return mc.world.getBlockState(pos);
  }
  
  public static Block getBlock(BlockPos pos) {
    return getState(pos).getBlock();
  }
  
  public static void faceVectorPacketInstant(Vec3d vec) {
    float[] rotations = getNeededRotations2(vec);
    mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], mc.player.onGround));
  }
  
  private static float[] getNeededRotations2(Vec3d vec) {
    Vec3d eyesPos = getEyesPos();
    double diffX = vec.x - eyesPos.x;
    double diffY = vec.y - eyesPos.y;
    double diffZ = vec.z - eyesPos.z;
    double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
    float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
    float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));
    return new float[] { mc.player.rotationYaw + 
        
        MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + 
        
        MathHelper.wrapDegrees(pitch - mc.player.rotationPitch) };
  }
  
  public static Vec3d getEyesPos() {
    return new Vec3d(mc.player.posX, mc.player.posY + mc.player
        .getEyeHeight(), mc.player.posZ);
  }
  
  public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
    getPlayerController().processRightClickBlock(mc.player, mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
  }
  
  private static PlayerControllerMP getPlayerController() {
    return mc.playerController;
  }
  
  public void setup() {
    ArrayList<String> blockmode = new ArrayList<>();
    blockmode.add("Obby");
    blockmode.add("EChest");
    blockmode.add("Both");
    blockmode.add("Web");
    this.type = registerMode("Block", "BlockMode", blockmode, "Obby");
    this.range = registerDouble("Place Range", "PlaceRange", 5.0D, 0.0D, 10.0D);
    this.yRange = registerInteger("Y Range", "YRange", 2, 0, 10);
    this.waitTick = registerInteger("Tick Delay", "TickDelay", 1, 0, 20);
    this.rotate = registerBoolean("Rotate", "Rotate", false);
    this.chat = registerBoolean("Toggle Msg", "ToggleMsg", false);
  }
  
  public void onUpdate() {
    // Byte code:
    //   0: aload_0
    //   1: new java/util/ArrayList
    //   4: dup
    //   5: invokespecial <init> : ()V
    //   8: putfield holes : Ljava/util/ArrayList;
    //   11: aload_0
    //   12: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   15: invokevirtual getValue : ()Ljava/lang/String;
    //   18: ldc_w 'Obby'
    //   21: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   24: ifeq -> 35
    //   27: aload_0
    //   28: aload_0
    //   29: getfield obbyonly : Ljava/util/List;
    //   32: putfield list : Ljava/util/List;
    //   35: aload_0
    //   36: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   39: invokevirtual getValue : ()Ljava/lang/String;
    //   42: ldc_w 'EChest'
    //   45: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   48: ifeq -> 59
    //   51: aload_0
    //   52: aload_0
    //   53: getfield echestonly : Ljava/util/List;
    //   56: putfield list : Ljava/util/List;
    //   59: aload_0
    //   60: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   63: invokevirtual getValue : ()Ljava/lang/String;
    //   66: ldc_w 'Both'
    //   69: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   72: ifeq -> 83
    //   75: aload_0
    //   76: aload_0
    //   77: getfield bothonly : Ljava/util/List;
    //   80: putfield list : Ljava/util/List;
    //   83: aload_0
    //   84: getfield type : Lcom/gamesense/api/settings/Setting$Mode;
    //   87: invokevirtual getValue : ()Ljava/lang/String;
    //   90: ldc_w 'Web'
    //   93: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   96: ifeq -> 107
    //   99: aload_0
    //   100: aload_0
    //   101: getfield webonly : Ljava/util/List;
    //   104: putfield list : Ljava/util/List;
    //   107: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   110: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   113: invokevirtual getPosition : ()Lnet/minecraft/util/math/BlockPos;
    //   116: aload_0
    //   117: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   120: invokevirtual getValue : ()D
    //   123: dneg
    //   124: aload_0
    //   125: getfield yRange : Lcom/gamesense/api/settings/Setting$Integer;
    //   128: invokevirtual getValue : ()I
    //   131: ineg
    //   132: i2d
    //   133: aload_0
    //   134: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   137: invokevirtual getValue : ()D
    //   140: dneg
    //   141: invokevirtual add : (DDD)Lnet/minecraft/util/math/BlockPos;
    //   144: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   147: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   150: invokevirtual getPosition : ()Lnet/minecraft/util/math/BlockPos;
    //   153: aload_0
    //   154: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   157: invokevirtual getValue : ()D
    //   160: aload_0
    //   161: getfield yRange : Lcom/gamesense/api/settings/Setting$Integer;
    //   164: invokevirtual getValue : ()I
    //   167: i2d
    //   168: aload_0
    //   169: getfield range : Lcom/gamesense/api/settings/Setting$Double;
    //   172: invokevirtual getValue : ()D
    //   175: invokevirtual add : (DDD)Lnet/minecraft/util/math/BlockPos;
    //   178: invokestatic getAllInBox : (Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Ljava/lang/Iterable;
    //   181: astore_1
    //   182: aload_1
    //   183: invokeinterface iterator : ()Ljava/util/Iterator;
    //   188: astore_2
    //   189: aload_2
    //   190: invokeinterface hasNext : ()Z
    //   195: ifeq -> 633
    //   198: aload_2
    //   199: invokeinterface next : ()Ljava/lang/Object;
    //   204: checkcast net/minecraft/util/math/BlockPos
    //   207: astore_3
    //   208: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   211: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   214: aload_3
    //   215: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   218: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   223: invokevirtual blocksMovement : ()Z
    //   226: ifne -> 630
    //   229: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   232: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   235: aload_3
    //   236: iconst_0
    //   237: iconst_1
    //   238: iconst_0
    //   239: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   242: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   245: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   250: invokevirtual blocksMovement : ()Z
    //   253: ifne -> 630
    //   256: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   259: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   262: aload_3
    //   263: iconst_1
    //   264: iconst_0
    //   265: iconst_0
    //   266: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   269: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   272: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   277: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   280: if_acmpne -> 287
    //   283: iconst_1
    //   284: goto -> 288
    //   287: iconst_0
    //   288: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   291: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   294: aload_3
    //   295: iconst_1
    //   296: iconst_0
    //   297: iconst_0
    //   298: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   301: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   304: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   309: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   312: if_acmpne -> 319
    //   315: iconst_1
    //   316: goto -> 320
    //   319: iconst_0
    //   320: ior
    //   321: ifeq -> 613
    //   324: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   327: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   330: aload_3
    //   331: iconst_0
    //   332: iconst_0
    //   333: iconst_1
    //   334: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   337: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   340: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   345: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   348: if_acmpne -> 355
    //   351: iconst_1
    //   352: goto -> 356
    //   355: iconst_0
    //   356: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   359: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   362: aload_3
    //   363: iconst_0
    //   364: iconst_0
    //   365: iconst_1
    //   366: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   369: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   372: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   377: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   380: if_acmpne -> 387
    //   383: iconst_1
    //   384: goto -> 388
    //   387: iconst_0
    //   388: ior
    //   389: ifeq -> 613
    //   392: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   395: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   398: aload_3
    //   399: iconst_m1
    //   400: iconst_0
    //   401: iconst_0
    //   402: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   405: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   408: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   413: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   416: if_acmpne -> 423
    //   419: iconst_1
    //   420: goto -> 424
    //   423: iconst_0
    //   424: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   427: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   430: aload_3
    //   431: iconst_m1
    //   432: iconst_0
    //   433: iconst_0
    //   434: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   437: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   440: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   445: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   448: if_acmpne -> 455
    //   451: iconst_1
    //   452: goto -> 456
    //   455: iconst_0
    //   456: ior
    //   457: ifeq -> 613
    //   460: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   463: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   466: aload_3
    //   467: iconst_0
    //   468: iconst_0
    //   469: iconst_m1
    //   470: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   473: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   476: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   481: getstatic net/minecraft/init/Blocks.BEDROCK : Lnet/minecraft/block/Block;
    //   484: if_acmpne -> 491
    //   487: iconst_1
    //   488: goto -> 492
    //   491: iconst_0
    //   492: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   495: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   498: aload_3
    //   499: iconst_0
    //   500: iconst_0
    //   501: iconst_m1
    //   502: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   505: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   508: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   513: getstatic net/minecraft/init/Blocks.OBSIDIAN : Lnet/minecraft/block/Block;
    //   516: if_acmpne -> 523
    //   519: iconst_1
    //   520: goto -> 524
    //   523: iconst_0
    //   524: ior
    //   525: ifeq -> 613
    //   528: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   531: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   534: aload_3
    //   535: iconst_0
    //   536: iconst_0
    //   537: iconst_0
    //   538: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   541: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   544: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   549: getstatic net/minecraft/block/material/Material.AIR : Lnet/minecraft/block/material/Material;
    //   552: if_acmpne -> 613
    //   555: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   558: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   561: aload_3
    //   562: iconst_0
    //   563: iconst_1
    //   564: iconst_0
    //   565: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   568: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   571: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   576: getstatic net/minecraft/block/material/Material.AIR : Lnet/minecraft/block/material/Material;
    //   579: if_acmpne -> 613
    //   582: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   585: getfield world : Lnet/minecraft/client/multiplayer/WorldClient;
    //   588: aload_3
    //   589: iconst_0
    //   590: iconst_2
    //   591: iconst_0
    //   592: invokevirtual add : (III)Lnet/minecraft/util/math/BlockPos;
    //   595: invokevirtual getBlockState : (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   598: invokeinterface getMaterial : ()Lnet/minecraft/block/material/Material;
    //   603: getstatic net/minecraft/block/material/Material.AIR : Lnet/minecraft/block/material/Material;
    //   606: if_acmpne -> 613
    //   609: iconst_1
    //   610: goto -> 614
    //   613: iconst_0
    //   614: istore #4
    //   616: iload #4
    //   618: ifeq -> 630
    //   621: aload_0
    //   622: getfield holes : Ljava/util/ArrayList;
    //   625: aload_3
    //   626: invokevirtual add : (Ljava/lang/Object;)Z
    //   629: pop
    //   630: goto -> 189
    //   633: iconst_m1
    //   634: istore_2
    //   635: iconst_0
    //   636: istore_3
    //   637: iload_3
    //   638: bipush #9
    //   640: if_icmpge -> 721
    //   643: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   646: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   649: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   652: iload_3
    //   653: invokevirtual getStackInSlot : (I)Lnet/minecraft/item/ItemStack;
    //   656: astore #4
    //   658: aload #4
    //   660: getstatic net/minecraft/item/ItemStack.EMPTY : Lnet/minecraft/item/ItemStack;
    //   663: if_acmpeq -> 715
    //   666: aload #4
    //   668: invokevirtual getItem : ()Lnet/minecraft/item/Item;
    //   671: instanceof net/minecraft/item/ItemBlock
    //   674: ifne -> 680
    //   677: goto -> 715
    //   680: aload #4
    //   682: invokevirtual getItem : ()Lnet/minecraft/item/Item;
    //   685: checkcast net/minecraft/item/ItemBlock
    //   688: invokevirtual getBlock : ()Lnet/minecraft/block/Block;
    //   691: astore #5
    //   693: aload_0
    //   694: getfield list : Ljava/util/List;
    //   697: aload #5
    //   699: invokeinterface contains : (Ljava/lang/Object;)Z
    //   704: ifne -> 710
    //   707: goto -> 715
    //   710: iload_3
    //   711: istore_2
    //   712: goto -> 721
    //   715: iinc #3, 1
    //   718: goto -> 637
    //   721: iload_2
    //   722: iconst_m1
    //   723: if_icmpne -> 727
    //   726: return
    //   727: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   730: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   733: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   736: getfield currentItem : I
    //   739: istore_3
    //   740: aload_0
    //   741: getfield waitTick : Lcom/gamesense/api/settings/Setting$Integer;
    //   744: invokevirtual getValue : ()I
    //   747: ifle -> 809
    //   750: aload_0
    //   751: getfield waitCounter : I
    //   754: aload_0
    //   755: getfield waitTick : Lcom/gamesense/api/settings/Setting$Integer;
    //   758: invokevirtual getValue : ()I
    //   761: if_icmpge -> 804
    //   764: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   767: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   770: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   773: iload_2
    //   774: putfield currentItem : I
    //   777: aload_0
    //   778: getfield holes : Ljava/util/ArrayList;
    //   781: aload_0
    //   782: <illegal opcode> accept : (Lcom/gamesense/client/module/modules/combat/HoleFill;)Ljava/util/function/Consumer;
    //   787: invokevirtual forEach : (Ljava/util/function/Consumer;)V
    //   790: getstatic com/gamesense/client/module/modules/combat/HoleFill.mc : Lnet/minecraft/client/Minecraft;
    //   793: getfield player : Lnet/minecraft/client/entity/EntityPlayerSP;
    //   796: getfield inventory : Lnet/minecraft/entity/player/InventoryPlayer;
    //   799: iload_3
    //   800: putfield currentItem : I
    //   803: return
    //   804: aload_0
    //   805: iconst_0
    //   806: putfield waitCounter : I
    //   809: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #146	-> 0
    //   #147	-> 11
    //   #148	-> 27
    //   #150	-> 35
    //   #151	-> 51
    //   #153	-> 59
    //   #154	-> 75
    //   #156	-> 83
    //   #157	-> 99
    //   #159	-> 107
    //   #160	-> 182
    //   #161	-> 208
    //   #162	-> 256
    //   #163	-> 266
    //   #164	-> 334
    //   #165	-> 402
    //   #166	-> 470
    //   #167	-> 538
    //   #168	-> 565
    //   #169	-> 592
    //   #170	-> 616
    //   #171	-> 621
    //   #174	-> 630
    //   #177	-> 633
    //   #178	-> 635
    //   #180	-> 643
    //   #181	-> 653
    //   #183	-> 658
    //   #184	-> 677
    //   #187	-> 680
    //   #188	-> 693
    //   #189	-> 707
    //   #192	-> 710
    //   #193	-> 712
    //   #178	-> 715
    //   #197	-> 721
    //   #198	-> 726
    //   #201	-> 727
    //   #204	-> 740
    //   #205	-> 750
    //   #207	-> 764
    //   #208	-> 777
    //   #209	-> 790
    //   #210	-> 803
    //   #212	-> 804
    //   #215	-> 809
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   616	14	4	solidNeighbours	Z
    //   208	422	3	pos	Lnet/minecraft/util/math/BlockPos;
    //   658	57	4	stack	Lnet/minecraft/item/ItemStack;
    //   693	22	5	block	Lnet/minecraft/block/Block;
    //   637	84	3	i	I
    //   0	810	0	this	Lcom/gamesense/client/module/modules/combat/HoleFill;
    //   182	628	1	blocks	Ljava/lang/Iterable;
    //   635	175	2	newSlot	I
    //   740	70	3	oldSlot	I
    // Local variable type table:
    //   start	length	slot	name	signature
    //   182	628	1	blocks	Ljava/lang/Iterable<Lnet/minecraft/util/math/BlockPos;>;
  }
  
  public void onEnable() {
    if (mc.player != null && this.chat.getValue())
      Command.sendRawMessage("§aHolefill turned ON!"); 
  }
  
  public void onDisable() {
    if (mc.player != null && this.chat.getValue())
      Command.sendRawMessage("§cHolefill turned OFF!"); 
  }
  
  private void place(BlockPos blockPos) {
    for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos))) {
      if (entity instanceof net.minecraft.entity.EntityLivingBase)
        return; 
    } 
    placeBlockScaffold(blockPos, this.rotate.getValue());
    this.waitCounter++;
  }
}
