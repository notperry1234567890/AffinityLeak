//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.players.enemy.Enemies;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.Wrapper;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.HUD;
import java.awt.Rectangle;
import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
  Setting.Boolean durability;
  
  Setting.Boolean armor;
  
  Setting.Boolean enchantnames;
  
  Setting.Boolean itemName;
  
  Setting.Boolean gamemode;
  
  Setting.Boolean health;
  
  Setting.Boolean ping;
  
  Setting.Boolean entityId;
  
  Setting.Boolean customColor;
  
  Setting.ColorSetting borderColor;
  
  public Nametags() {
    super("Nametags", Module.Category.Render);
  }
  
  public static double timerPos(double n, double n2) {
    return n2 + (n - n2) * (Wrapper.getMinecraft()).timer.renderPartialTicks;
  }
  
  public static Vec3d renderPosEntity(Entity entity) {
    return new Vec3d(timerPos(entity.posX, entity.lastTickPosX) - (mc.getRenderManager()).renderPosX, timerPos(entity.posY, entity.lastTickPosY) - (mc.getRenderManager()).renderPosY, timerPos(entity.posZ, entity.lastTickPosZ) - (mc.getRenderManager()).renderPosZ);
  }
  
  public static Vec3d M2(Entity entity, Vec3d vec3d) {
    return location4(entity, vec3d.x, vec3d.y, vec3d.z);
  }
  
  public static Vec3d location1(Entity entity, Vec3d vec3d) {
    return location4(entity, vec3d.x, vec3d.y, vec3d.z);
  }
  
  public static Vec3d location3(Entity entity, double n) {
    return location4(entity, n, n, n);
  }
  
  public static Vec3d location4(Entity entity, double n, double n2, double n3) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * n, (entity.posY - entity.lastTickPosY) * n2, (entity.posZ - entity.lastTickPosZ) * n3);
  }
  
  public static Vec3d location5(Entity entity, float n) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(location3(entity, n));
  }
  
  public static void M(float n) {
    GL11.glDisable(3008);
    GL11.glEnable(3042);
    GL11.glBlendFunc(770, 771);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    GL11.glEnable(2884);
    mc.entityRenderer.enableLightmap();
    GL11.glEnable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
    GL11.glLineWidth(n);
  }
  
  public static void drawBorderedRectReliant(float x, float y, float x1, float y1, float lineWidth, APColor inside, APColor border) {
    enableGL2D();
    drawRect(x, y, x1, y1, inside);
    border.glColor();
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glLineWidth(lineWidth);
    GL11.glBegin(3);
    GL11.glVertex2f(x, y);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    disableGL2D();
  }
  
  public static void enableGL2D() {
    GL11.glDisable(2929);
    GL11.glEnable(3042);
    GL11.glDisable(3553);
    GL11.glBlendFunc(770, 771);
    GL11.glDepthMask(true);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4354);
    GL11.glHint(3155, 4354);
  }
  
  public static void disableGL2D() {
    GL11.glEnable(3553);
    GL11.glDisable(3042);
    GL11.glEnable(2929);
    GL11.glDisable(2848);
    GL11.glHint(3154, 4352);
    GL11.glHint(3155, 4352);
  }
  
  public static void drawRect(Rectangle rectangle, APColor color) {
    drawRect(rectangle.x, rectangle.y, (rectangle.x + rectangle.width), (rectangle.y + rectangle.height), color);
  }
  
  public static void drawRect(float x, float y, float x1, float y1, APColor color) {
    enableGL2D();
    color.glColor();
    drawRect(x, y, x1, y1);
    disableGL2D();
  }
  
  public static void drawRect(float x, float y, float x1, float y1) {
    GL11.glBegin(7);
    GL11.glVertex2f(x, y1);
    GL11.glVertex2f(x1, y1);
    GL11.glVertex2f(x1, y);
    GL11.glVertex2f(x, y);
    GL11.glEnd();
  }
  
  public void setup() {
    this.durability = registerBoolean("Durability", "Durability", true);
    this.armor = registerBoolean("Armor", "Armor", true);
    this.enchantnames = registerBoolean("Enchants", "Enchants", true);
    this.itemName = registerBoolean("Item Name", "ItemName", false);
    this.gamemode = registerBoolean("Gamemode", "Gamemode", false);
    this.health = registerBoolean("Health", "Health", true);
    this.ping = registerBoolean("Ping", "Ping", false);
    this.entityId = registerBoolean("Entity Id", "EntityId", false);
    this.customColor = registerBoolean("Custom Color", "CustomColor", true);
    this.borderColor = registerColor("Border Color", "BorderColor");
  }
  
  public void onWorldRender(RenderEvent event) {
    for (Object o : mc.world.playerEntities) {
      Entity entity = (Entity)o;
      if (entity instanceof EntityPlayer && entity != mc.player && entity.isEntityAlive()) {
        double x = interpolate(entity.lastTickPosX, entity.posX, event.getPartialTicks()) - (mc.getRenderManager()).renderPosX;
        double y = interpolate(entity.lastTickPosY, entity.posY, event.getPartialTicks()) - (mc.getRenderManager()).renderPosY;
        double z = interpolate(entity.lastTickPosZ, entity.posZ, event.getPartialTicks()) - (mc.getRenderManager()).renderPosZ;
        Vec3d m = renderPosEntity(entity);
        renderNameTagsFor((EntityPlayer)entity, m.x, m.y, m.z);
      } 
    } 
  }
  
  public void renderNameTagsFor(EntityPlayer entityPlayer, double n, double n2, double n3) {
    renderNametags(entityPlayer, n, n2, n3);
  }
  
  private double interpolate(double previous, double current, float delta) {
    return previous + (current - previous) * delta;
  }
  
  private void renderItemName(ItemStack itemStack, int x, int y) {
    float n3 = 0.5F;
    float n4 = 0.5F;
    GlStateManager.scale(n4, n3, n4);
    GlStateManager.disableDepth();
    String displayName = itemStack.getDisplayName();
    String s2 = displayName;
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), s2, -FontUtils.getStringWidth(HUD.customFont.getValue(), s2) / 2, y, new APColor(255, 255, 255));
    GlStateManager.enableDepth();
    float n5 = 2.0F;
    int n6 = 2;
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
  }
  
  private void renderEnchants(ItemStack itemStack, int x, int y) {
    y = y;
    Iterator<Enchantment> iterator2 = EnchantmentHelper.getEnchantments(itemStack).keySet().iterator(), iterator = iterator2;
    while (iterator.hasNext()) {
      Enchantment enchantment;
      if ((enchantment = iterator2.next()) == null) {
        iterator = iterator2;
        continue;
      } 
      Enchantment enchantment3 = enchantment;
      if (this.enchantnames.getValue()) {
        FontUtils.drawStringWithShadow(HUD.customFont.getValue(), stringForEnchants(enchantment3, EnchantmentHelper.getEnchantmentLevel(enchantment3, itemStack)), x * 2, y, new APColor(255, 255, 255));
      } else {
        return;
      } 
      y += 8;
      iterator = iterator2;
    } 
    if (itemStack.getItem().equals(Items.GOLDEN_APPLE) && itemStack.hasEffect())
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), "God", x * 2, y, new APColor(195, 77, 65)); 
  }
  
  private String stringForEnchants(Enchantment enchantment, int n) {
    ResourceLocation resourceLocation;
    String substring = ((resourceLocation = (ResourceLocation)Enchantment.REGISTRY.getNameForObject(enchantment)) == null) ? enchantment.getName() : resourceLocation.toString();
    int n2 = (n > 1) ? 12 : 13;
    if (substring.length() > n2)
      substring = substring.substring(10, n2); 
    StringBuilder sb = new StringBuilder();
    String s = substring;
    int n3 = 0;
    String s2 = sb.insert(0, s.substring(0, 1).toUpperCase()).append(substring.substring(1)).toString();
    if (n > 1)
      s2 = (new StringBuilder()).insert(0, s2).append(n).toString(); 
    return s2;
  }
  
  private void renderItemDurability(ItemStack itemStack, int x, int y) {
    int maxDamage = itemStack.getMaxDamage();
    float n3 = (maxDamage - itemStack.getItemDamage()) / maxDamage;
    float green = (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage();
    float red = 1.0F - green;
    int dmg = 100 - (int)(red * 100.0F);
    float n4 = 0.5F;
    float n5 = 0.5F;
    GlStateManager.scale(0.5F, 0.5F, 0.5F);
    GlStateManager.disableDepth();
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), (new StringBuilder()).insert(0, (int)(n3 * 100.0F)).append('%').toString(), x * 2, y, new APColor((int)(red * 255.0F), (int)(green * 255.0F), 0));
    GlStateManager.enableDepth();
    float n6 = 2.0F;
    int n7 = 2;
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
  }
  
  private void renderItems(ItemStack itemStack, int n, int n2, int n3) {
    GlStateManager.pushMatrix();
    GlStateManager.depthMask(true);
    GlStateManager.clear(256);
    RenderHelper.enableStandardItemLighting();
    (mc.getRenderItem()).zLevel = -150.0F;
    GlStateManager.disableAlpha();
    GlStateManager.enableDepth();
    GlStateManager.disableCull();
    int n4 = (n3 > 4) ? ((n3 - 4) * 8 / 2) : 0;
    mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, n, n2 + n4);
    mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, n, n2 + n4);
    (mc.getRenderItem()).zLevel = 0.0F;
    RenderHelper.disableStandardItemLighting();
    GlStateManager.enableCull();
    GlStateManager.enableAlpha();
    float n5 = 0.5F;
    float n6 = 0.5F;
    GlStateManager.scale(0.5F, 0.5F, 0.5F);
    GlStateManager.disableDepth();
    renderEnchants(itemStack, n, n2 - 24);
    GlStateManager.enableDepth();
    float n7 = 2.0F;
    int n8 = 2;
    GlStateManager.scale(2.0F, 2.0F, 2.0F);
    GlStateManager.popMatrix();
  }
  
  private void renderNametags(EntityPlayer entityPlayer, double n, double distance, double n2) {
    APColor color;
    double tempY = distance;
    tempY += entityPlayer.isSneaking() ? 0.5D : 0.7D;
    Entity entity2 = (mc.getRenderViewEntity() == null) ? (Entity)mc.player : mc.getRenderViewEntity(), entity = entity2;
    double posX = entity2.posX;
    double posY = entity2.posY;
    double posZ = entity2.posZ;
    Vec3d m;
    entity2.posX = (m = renderPosEntity(entity2)).x;
    entity2.posY = m.y;
    entity2.posZ = m.z;
    distance = entity.getDistance(n, distance, n2);
    int n4 = FontUtils.getStringWidth(HUD.customFont.getValue(), renderEntityName(entityPlayer)) / 2;
    int n5 = FontUtils.getStringWidth(HUD.customFont.getValue(), renderEntityName(entityPlayer)) / 2;
    double n6 = 0.0018D + 0.003000000026077032D * distance;
    if (distance <= 8.0D)
      n6 = 0.0245D; 
    GlStateManager.pushMatrix();
    RenderHelper.enableStandardItemLighting();
    GlStateManager.enablePolygonOffset();
    GlStateManager.doPolygonOffset(1.0F, -1500000.0F);
    GlStateManager.disableLighting();
    GlStateManager.translate((float)n, (float)tempY + 1.4F, (float)n2);
    float n7 = -(mc.getRenderManager()).playerViewY;
    float n8 = 1.0F;
    float n9 = 0.0F;
    GlStateManager.rotate(n7, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((mc.getRenderManager()).playerViewX, (mc.gameSettings.thirdPersonView == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(-n6, -n6, n6);
    GlStateManager.disableDepth();
    GlStateManager.enableBlend();
    GlStateManager.enableBlend();
    if (this.customColor.getValue()) {
      color = this.borderColor.getValue();
    } else {
      color = new APColor(0, 0, 0, 51);
    } 
    drawBorderedRectReliant((-n4 - 1), -mc.fontRenderer.FONT_HEIGHT, (n4 + 2), 1.0F, 1.8F, new APColor(0, 4, 0, 85), color);
    GlStateManager.disableBlend();
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), renderEntityName(entityPlayer), -n4, -(mc.fontRenderer.FONT_HEIGHT - 1), renderPing(entityPlayer));
    EntityPlayer entityPlayer2 = entityPlayer;
    ItemStack heldItemMainhand = entityPlayer2.getHeldItemMainhand();
    ItemStack heldItemOffhand = entityPlayer.getHeldItemOffhand();
    int n10 = 0;
    int n11 = 0;
    boolean b = false;
    GlStateManager.pushMatrix();
    int i = 3;
    int n12 = 3;
    while (i >= 0) {
      ItemStack itemStack;
      if (!(itemStack = (ItemStack)entityPlayer.inventory.armorInventory.get(n12)).isEmpty()) {
        Boolean j = Boolean.valueOf(this.durability.getValue());
        n10 -= 8;
        if (j.booleanValue())
          b = true; 
        int size;
        if (this.armor.getValue() && (size = EnchantmentHelper.getEnchantments(itemStack).size()) > n11)
          n11 = size; 
      } 
      i = --n12;
    } 
    if (!heldItemOffhand.isEmpty() && (this.armor.getValue() || (this.durability.getValue() && heldItemOffhand.isItemStackDamageable()))) {
      n10 -= 8;
      if (this.durability.getValue() && heldItemOffhand.isItemStackDamageable())
        b = true; 
      int size2;
      if (this.armor.getValue() && (size2 = EnchantmentHelper.getEnchantments(heldItemOffhand).size()) > n11)
        n11 = size2; 
    } 
    if (!heldItemMainhand.isEmpty()) {
      Nametags nametags;
      int size3;
      if (this.armor.getValue() && (size3 = EnchantmentHelper.getEnchantments(heldItemMainhand).size()) > n11)
        n11 = size3; 
      int k = armorValue(n11);
      if (this.armor.getValue() || (this.durability.getValue() && heldItemMainhand.isItemStackDamageable()))
        n10 -= 8; 
      if (this.armor.getValue()) {
        ItemStack itemStack2 = heldItemMainhand;
        int n13 = n10;
        int n14 = k;
        k -= 32;
        renderItems(itemStack2, n13, n14, n11);
      } 
      if (this.durability.getValue() && heldItemMainhand.isItemStackDamageable()) {
        int n15 = k;
        renderItemDurability(heldItemMainhand, n10, k);
        k = n15 - (HUD.customFont.getValue() ? FontUtils.getFontHeight(HUD.customFont.getValue()) : mc.fontRenderer.FONT_HEIGHT);
        nametags = this;
      } else {
        if (b)
          k -= HUD.customFont.getValue() ? FontUtils.getFontHeight(HUD.customFont.getValue()) : mc.fontRenderer.FONT_HEIGHT; 
        nametags = this;
      } 
      if (nametags.itemName.getValue())
        renderItemName(heldItemMainhand, n10, k); 
      if (this.armor.getValue() || (this.durability.getValue() && heldItemMainhand.isItemStackDamageable()))
        n10 += 16; 
    } 
    int l = 3;
    int n16 = 3;
    while (l >= 0) {
      ItemStack itemStack3;
      if (!(itemStack3 = (ItemStack)entityPlayer.inventory.armorInventory.get(n16)).isEmpty()) {
        int m2 = armorValue(n11);
        if (this.armor.getValue()) {
          ItemStack itemStack4 = itemStack3;
          int n17 = n10;
          int n18 = m2;
          m2 -= 32;
          renderItems(itemStack4, n17, n18, n11);
        } 
        if (this.durability.getValue() && itemStack3.isItemStackDamageable())
          renderItemDurability(itemStack3, n10, m2); 
        n10 += 16;
      } 
      l = --n16;
    } 
    if (!heldItemOffhand.isEmpty()) {
      int m3 = armorValue(n11);
      if (this.armor.getValue()) {
        ItemStack itemStack5 = heldItemOffhand;
        int n19 = n10;
        int n20 = m3;
        m3 -= 32;
        renderItems(itemStack5, n19, n20, n11);
      } 
      if (this.durability.getValue() && heldItemOffhand.isItemStackDamageable())
        renderItemDurability(heldItemOffhand, n10, m3); 
      n10 += 16;
    } 
    GlStateManager.popMatrix();
    float n21 = 1.0F;
    double posZ2 = posZ;
    Entity entity3 = entity;
    double posY2 = posY;
    entity.posX = posX;
    entity3.posY = posY2;
    entity3.posZ = posZ2;
    GlStateManager.enableDepth();
    GlStateManager.disableBlend();
    GlStateManager.disablePolygonOffset();
    GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
    GlStateManager.popMatrix();
  }
  
  private APColor renderPing(EntityPlayer entityPlayer) {
    if (Friends.isFriend(entityPlayer.getName()))
      return ColorMain.getFriendGSColor(); 
    if (Enemies.isEnemy(entityPlayer.getName()))
      return ColorMain.getEnemyGSColor(); 
    if (entityPlayer.isInvisible())
      return new APColor(128, 128, 128); 
    if (mc.getConnection() != null && mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()) == null)
      return new APColor(239, 1, 71); 
    if (entityPlayer.isSneaking())
      return new APColor(255, 153, 0); 
    return new APColor(255, 255, 255);
  }
  
  private String renderEntityName(EntityPlayer entityPlayer) {
    String s = entityPlayer.getDisplayName().getFormattedText();
    if (this.entityId.getValue())
      s = (new StringBuilder()).insert(0, s).append(" ID: ").append(entityPlayer.getEntityId()).toString(); 
    Nametags nametags = null;
    if (this.gamemode.getValue()) {
      if (entityPlayer.isCreative()) {
        s = (new StringBuilder()).insert(0, s).append(" [C]").toString();
        nametags = this;
      } else if (entityPlayer.isSpectator()) {
        s = (new StringBuilder()).insert(0, s).append(" [I]").toString();
        nametags = this;
      } else {
        s = (new StringBuilder()).insert(0, s).append(" [S]").toString();
        nametags = this;
      } 
      if (this.ping.getValue() && mc.getConnection() != null && mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()) != null)
        s = (new StringBuilder()).insert(0, s).append(" ").append(mc.getConnection().getPlayerInfo(entityPlayer.getUniqueID()).getResponseTime()).append("ms").toString(); 
      if (!this.health.getValue())
        return s; 
      String s2 = TextFormatting.GREEN.toString();
      double ceil;
      if ((ceil = Math.ceil((entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()))) > 0.0D) {
        if (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= 5.0F) {
          s2 = TextFormatting.RED.toString();
        } else if (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() > 5.0F && entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= 10.0F) {
          s2 = TextFormatting.GOLD.toString();
        } else if (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() > 10.0F && entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= 15.0F) {
          s2 = TextFormatting.YELLOW.toString();
        } else if (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() > 15.0F && entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() <= 20.0F) {
          s2 = TextFormatting.DARK_GREEN.toString();
        } else if (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount() > 20.0F) {
          s2 = TextFormatting.GREEN.toString();
        } 
      } else {
        s2 = TextFormatting.DARK_RED.toString();
      } 
      return (new StringBuilder()).insert(0, s).append(s2).append(" ").append((ceil > 0.0D) ? Integer.valueOf((int)ceil) : "0").toString();
    } 
    nametags = this;
  }
  
  private int armorValue(int n) {
    int n2 = this.armor.getValue() ? -26 : -27;
    if (n > 4)
      n2 -= (n - 4) * 8; 
    return n2;
  }
}
