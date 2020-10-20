//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.api.util.world.TpsUtils;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.combat.AutoCrystal;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class HUD extends Module {
  private static final RenderItem itemRender = Minecraft.getMinecraft()
    .getRenderItem();
  
  public static Setting.Boolean customFont;
  
  private static APColor col;
  
  Setting.Boolean PotionEffects;
  
  Setting.Boolean Watermark;
  
  Setting.Boolean Welcomer;
  
  Setting.Boolean Inventory;
  
  Setting.Integer inventoryX;
  
  Setting.Integer inventoryY;
  
  Setting.Boolean GameSenseInfo;
  
  Setting.Mode Type;
  
  Setting.Boolean ArrayList;
  
  Setting.Boolean ArmorHud;
  
  Setting.Integer potionx;
  
  Setting.Integer potiony;
  
  Setting.Integer welcomex;
  
  Setting.Integer welcomey;
  
  Setting.Integer infox;
  
  Setting.Integer infoy;
  
  Setting.Boolean sortUp;
  
  Setting.Boolean right;
  
  Setting.Boolean psortUp;
  
  Setting.Boolean pright;
  
  Setting.Integer arrayx;
  
  Setting.Integer arrayy;
  
  Setting.ColorSetting color;
  
  ResourceLocation resource;
  
  int sort;
  
  int modCount;
  
  int count;
  
  DecimalFormat format1 = new DecimalFormat("0");
  
  DecimalFormat format2 = new DecimalFormat("00");
  
  int totems;
  
  private BlockPos[] surroundOffset;
  
  public HUD() {
    super("HUD", Module.Category.HUD);
    setDrawn(false);
    this.resource = new ResourceLocation("minecraft:inventory_viewer.png");
  }
  
  public void setup() {
    ArrayList<String> Modes = new ArrayList<>();
    Modes.add("PvP");
    Modes.add("Combat");
    this.Type = registerMode("Info Type", "InfoType", Modes, "PvP");
    this.infox = registerInteger("Information X", "InformationX", 0, 0, 1000);
    this.infoy = registerInteger("Information Y", "InformationY", 0, 0, 1000);
    this.GameSenseInfo = registerBoolean("Information", "Information", false);
    this.ArmorHud = registerBoolean("Armor Hud", "ArmorHud", false);
    this.ArrayList = registerBoolean("ArrayList", "ArrayList", false);
    this.sortUp = registerBoolean("Array Sort Up", "ArraySortUp", false);
    this.right = registerBoolean("Array Right", "ArrayRight", false);
    this.arrayx = registerInteger("Array X", "ArrayX", 0, 0, 1000);
    this.arrayy = registerInteger("Array Y", "ArrayY", 0, 0, 1000);
    this.Inventory = registerBoolean("Inventory", "Inventory", false);
    this.inventoryX = registerInteger("Inventory X", "InventoryX", 0, 0, 1000);
    this.inventoryY = registerInteger("Inventory Y", "InventoryY", 12, 0, 1000);
    this.PotionEffects = registerBoolean("Potion Effects", "PotionEffects", false);
    this.potionx = registerInteger("Potion X", "PotionX", 0, 0, 1000);
    this.potiony = registerInteger("Potion Y", "PotionY", 0, 0, 1000);
    this.psortUp = registerBoolean("Potion Sort Up", "PotionSortUp", false);
    this.pright = registerBoolean("Potion Right", "PotionRight", false);
    this.Watermark = registerBoolean("Watermark", "Watermark", false);
    this.Welcomer = registerBoolean("Welcomer", "Welcomer", false);
    this.welcomex = registerInteger("Welcomer X", "WelcomerX", 0, 0, 1000);
    this.welcomey = registerInteger("Welcomer Y", "WelcomerY", 0, 0, 1000);
    customFont = registerBoolean("Custom Font", "CustomFont", false);
    this.color = registerColor("Color", "Color");
  }
  
  public void onRender() {
    APColor c = this.color.getValue();
    if (this.PotionEffects.getValue()) {
      this.count = 0;
      try {
        mc.player.getActivePotionEffects().forEach(effect -> {
              String name = I18n.format(effect.getPotion().getName(), new Object[0]);
              double duration = (effect.getDuration() / TpsUtils.getTickRate());
              int amplifier = effect.getAmplifier() + 1;
              double p1 = duration % 60.0D;
              double p2 = duration / 60.0D;
              double p3 = p2 % 60.0D;
              String minutes = this.format1.format(p3);
              String seconds = this.format2.format(p1);
              String s = name + " " + amplifier + ChatFormatting.GRAY + " " + minutes + ":" + seconds;
              if (this.psortUp.getValue()) {
                if (this.pright.getValue()) {
                  FontUtils.drawStringWithShadow(customFont.getValue(), s, this.potionx.getValue() - FontUtils.getStringWidth(customFont.getValue(), s), this.potiony.getValue() + this.count * 10, c);
                } else {
                  FontUtils.drawStringWithShadow(customFont.getValue(), s, this.potionx.getValue(), this.potiony.getValue() + this.count * 10, c);
                } 
                this.count++;
              } else {
                if (this.pright.getValue()) {
                  FontUtils.drawStringWithShadow(customFont.getValue(), s, this.potionx.getValue() - FontUtils.getStringWidth(customFont.getValue(), s), this.potiony.getValue() + this.count * -10, c);
                } else {
                  FontUtils.drawStringWithShadow(customFont.getValue(), s, this.potionx.getValue(), this.potiony.getValue() + this.count * -10, c);
                } 
                this.count++;
              } 
            });
      } catch (NullPointerException e) {
        e.printStackTrace();
      } 
    } 
    if (this.Watermark.getValue())
      FontUtils.drawStringWithShadow(customFont.getValue(), "Affinity+ v0.0.1", 0, 0, c); 
    if (this.Welcomer.getValue())
      FontUtils.drawStringWithShadow(customFont.getValue(), "Hello " + mc.player.getName() + " :^)", this.welcomex.getValue(), this.welcomey.getValue(), c); 
    if (this.Inventory.getValue())
      drawInventory(this.inventoryX.getValue(), this.inventoryY.getValue()); 
    APColor on = new APColor(0, 255, 0);
    APColor off = new APColor(255, 0, 0);
    if (this.GameSenseInfo.getValue())
      if (this.Type.getValue().equalsIgnoreCase("PvP")) {
        this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
          this.totems++; 
        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).filter(e -> (mc.player.getDistance(e) <= AutoCrystal.range.getValue())).map(entity -> (EntityEnderCrystal)entity).min(Comparator.comparing(cl -> Float.valueOf(mc.player.getDistance((Entity)cl)))).orElse(null);
        EntityOtherPlayerMP players = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityOtherPlayerMP).filter(entity -> !Friends.isFriend(entity.getName())).filter(e -> (mc.player.getDistance(e) <= AutoCrystal.placeRange.getValue())).map(entity -> (EntityOtherPlayerMP)entity).min(Comparator.comparing(cl -> Float.valueOf(mc.player.getDistance((Entity)cl)))).orElse(null);
        AutoCrystal a = (AutoCrystal)ModuleManager.getModuleByName("Autocrystal");
        this.surroundOffset = new BlockPos[] { new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0) };
        List<EntityPlayer> entities = new ArrayList<>((Collection<? extends EntityPlayer>)mc.world.playerEntities.stream().filter(entityPlayer -> !Friends.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
        if (this.Type.getValue().equalsIgnoreCase("PvP")) {
          FontUtils.drawStringWithShadow(customFont.getValue(), "Affinity+", this.infox.getValue(), this.infoy.getValue(), c);
          if (players != null && mc.player.getDistance((Entity)players) <= AutoCrystal.range.getValue()) {
            FontUtils.drawStringWithShadow(customFont.getValue(), "HTR", this.infox.getValue(), this.infoy.getValue() + 10, on);
          } else {
            FontUtils.drawStringWithShadow(customFont.getValue(), "HTR", this.infox.getValue(), this.infoy.getValue() + 10, off);
          } 
          if (players != null && mc.player.getDistance((Entity)players) <= AutoCrystal.placeRange.getValue()) {
            FontUtils.drawStringWithShadow(customFont.getValue(), "PLR", this.infox.getValue(), this.infoy.getValue() + 20, on);
          } else {
            FontUtils.drawStringWithShadow(customFont.getValue(), "PLR", this.infox.getValue(), this.infoy.getValue() + 20, off);
          } 
          if (this.totems > 0 && ModuleManager.isModuleEnabled("AutoTotem")) {
            FontUtils.drawStringWithShadow(customFont.getValue(), this.totems + "", this.infox.getValue(), this.infoy.getValue() + 30, on);
          } else {
            FontUtils.drawStringWithShadow(customFont.getValue(), this.totems + "", this.infox.getValue(), this.infoy.getValue() + 30, off);
          } 
          if (getPing() > 100) {
            FontUtils.drawStringWithShadow(customFont.getValue(), "PING " + getPing(), this.infox.getValue(), this.infoy.getValue() + 40, off);
          } else {
            FontUtils.drawStringWithShadow(customFont.getValue(), "PING " + getPing(), this.infox.getValue(), this.infoy.getValue() + 40, on);
          } 
          for (EntityPlayer e : entities) {
            int i = 0;
            for (BlockPos add : this.surroundOffset) {
              i++;
              BlockPos o = (new BlockPos((e.getPositionVector()).x, (e.getPositionVector()).y, (e.getPositionVector()).z)).add(add.getX(), add.getY(), add.getZ());
              if (mc.world.getBlockState(o).getBlock() == Blocks.OBSIDIAN) {
                if (i == 1 && a.canPlaceCrystal(o.north(1).down()))
                  FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", this.infox.getValue(), this.infoy.getValue() + 50, on); 
                if (i == 2 && a.canPlaceCrystal(o.east(1).down()))
                  FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", this.infox.getValue(), this.infoy.getValue() + 50, on); 
                if (i == 3 && a.canPlaceCrystal(o.south(1).down()))
                  FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", this.infox.getValue(), this.infoy.getValue() + 50, on); 
                if (i == 4 && a.canPlaceCrystal(o.west(1).down()))
                  FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", this.infox.getValue(), this.infoy.getValue() + 50, on); 
              } else {
                FontUtils.drawStringWithShadow(customFont.getValue(), "LBY", this.infox.getValue(), this.infoy.getValue() + 50, off);
              } 
            } 
          } 
        } 
      } else if (this.Type.getValue().equalsIgnoreCase("Combat")) {
        FontUtils.drawStringWithShadow(customFont.getValue(), " ", this.infox.getValue(), this.infoy.getValue(), c);
        if (ModuleManager.isModuleEnabled("AutoCrystal")) {
          FontUtils.drawStringWithShadow(customFont.getValue(), "AC: ENBL", this.infox.getValue(), this.infoy.getValue(), on);
        } else {
          FontUtils.drawStringWithShadow(customFont.getValue(), "AC: DSBL", this.infox.getValue(), this.infoy.getValue(), off);
        } 
        if (ModuleManager.isModuleEnabled("KillAura")) {
          FontUtils.drawStringWithShadow(customFont.getValue(), "KA: ENBL", this.infox.getValue(), this.infoy.getValue() + 10, on);
        } else {
          FontUtils.drawStringWithShadow(customFont.getValue(), "KA: DSBL", this.infox.getValue(), this.infoy.getValue() + 10, off);
        } 
        if (ModuleManager.isModuleEnabled("Surround")) {
          FontUtils.drawStringWithShadow(customFont.getValue(), "FP: ENBL", this.infox.getValue(), this.infoy.getValue() + 20, on);
        } else {
          FontUtils.drawStringWithShadow(customFont.getValue(), "FP: DSBL", this.infox.getValue(), this.infoy.getValue() + 20, off);
        } 
        if (ModuleManager.isModuleEnabled("AutoTrap")) {
          FontUtils.drawStringWithShadow(customFont.getValue(), "AT: ENBL", this.infox.getValue(), this.infoy.getValue() + 30, on);
        } else {
          FontUtils.drawStringWithShadow(customFont.getValue(), "AT: DSBL", this.infox.getValue(), this.infoy.getValue() + 30, off);
        } 
        if (ModuleManager.isModuleEnabled("SelfTrap")) {
          FontUtils.drawStringWithShadow(customFont.getValue(), "ST: ENBL", this.infox.getValue(), this.infoy.getValue() + 40, on);
        } else {
          FontUtils.drawStringWithShadow(customFont.getValue(), "ST: DSBL", this.infox.getValue(), this.infoy.getValue() + 40, off);
        } 
      }  
    if (this.ArrayList.getValue()) {
      if (this.sortUp.getValue()) {
        this.sort = -1;
      } else {
        this.sort = 1;
      } 
      this.modCount = 0;
      col = c;
      ModuleManager.getModules()
        .stream()
        .filter(Module::isEnabled)
        .filter(Module::isDrawn)
        .sorted(Comparator.comparing(module -> Integer.valueOf(FontUtils.getStringWidth(customFont.getValue(), module.getName() + ChatFormatting.GRAY + " " + module.getHudInfo()) * -1)))
        .forEach(m -> {
            if (this.sortUp.getValue()) {
              if (this.right.getValue()) {
                FontUtils.drawStringWithShadow(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo(), this.arrayx.getValue() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo()), this.arrayy.getValue() + this.modCount * 10, col);
              } else {
                FontUtils.drawStringWithShadow(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo(), this.arrayx.getValue(), this.arrayy.getValue() + this.modCount * 10, col);
              } 
              this.modCount++;
            } else {
              if (this.right.getValue()) {
                FontUtils.drawStringWithShadow(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo(), this.arrayx.getValue() - FontUtils.getStringWidth(customFont.getValue(), m.getName() + ChatFormatting.GRAY + " " + m.getHudInfo()), this.arrayy.getValue() + this.modCount * -10, col);
              } else {
                FontUtils.drawStringWithShadow(customFont.getValue(), m.getName() + ChatFormatting.GRAY + m.getHudInfo(), this.arrayx.getValue(), this.arrayy.getValue() + this.modCount * -10, col);
              } 
              this.modCount++;
            } 
            if (this.color.getRainbow())
              col = APColor.fromHSB(col.getHue() + 0.02F, col.getSaturation(), col.getBrightness()); 
          });
    } 
    if (this.ArmorHud.getValue()) {
      GlStateManager.enableTexture2D();
      ScaledResolution resolution = new ScaledResolution(mc);
      int i = resolution.getScaledWidth() / 2;
      int iteration = 0;
      int y = resolution.getScaledHeight() - 55 - (mc.player.isInWater() ? 10 : 0);
      for (ItemStack is : mc.player.inventory.armorInventory) {
        iteration++;
        if (is.isEmpty())
          continue; 
        int x = i - 90 + (9 - iteration) * 20 + 2;
        GlStateManager.enableDepth();
        itemRender.zLevel = 200.0F;
        itemRender.renderItemAndEffectIntoGUI(is, x, y);
        itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
        itemRender.zLevel = 0.0F;
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        String s = (is.getCount() > 1) ? (is.getCount() + "") : "";
        mc.fontRenderer.drawStringWithShadow(s, (x + 19 - 2 - mc.fontRenderer.getStringWidth(s)), (y + 9), (new APColor(255, 255, 255)).getRGB());
        float green = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage();
        float red = 1.0F - green;
        int dmg = 100 - (int)(red * 100.0F);
        FontUtils.drawStringWithShadow(customFont.getValue(), dmg + "", x + 8 - mc.fontRenderer.getStringWidth(dmg + "") / 2, y - 11, new APColor((int)(red * 255.0F), (int)(green * 255.0F), 0));
      } 
      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
    } 
  }
  
  public void drawInventory(int x, int y) {
    if (this.Inventory.getValue()) {
      GlStateManager.enableAlpha();
      mc.renderEngine.bindTexture(this.resource);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      mc.ingameGUI.drawTexturedModalRect(x, y, 7, 17, 162, 54);
      GlStateManager.disableAlpha();
      GlStateManager.clear(256);
      NonNullList<ItemStack> items = (Minecraft.getMinecraft()).player.inventory.mainInventory;
      for (int size = items.size(), item = 9; item < size; item++) {
        int slotX = x + 1 + item % 9 * 18;
        int slotY = y + 1 + (item / 9 - 1) * 18;
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI((ItemStack)items.get(item), slotX, slotY);
        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, (ItemStack)items.get(item), slotX, slotY);
        RenderHelper.disableStandardItemLighting();
      } 
    } 
  }
  
  public int getPing() {
    int p = -1;
    if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
      p = -1;
    } else {
      p = mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime();
    } 
    return p;
  }
}
