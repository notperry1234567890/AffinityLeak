//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util.render;

import com.gamesense.api.util.APColor;
import com.gamesense.api.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class GameSenseTessellator extends Tessellator {
  private static final Minecraft mc = Wrapper.getMinecraft();
  
  public static GameSenseTessellator INSTANCE = new GameSenseTessellator();
  
  public GameSenseTessellator() {
    super(2097152);
  }
  
  public static void prepare(int mode) {
    prepareGL();
    begin(mode);
  }
  
  public static void prepareGL() {
    GL11.glBlendFunc(770, 771);
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.glLineWidth(1.5F);
    GlStateManager.disableTexture2D();
    GlStateManager.depthMask(false);
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.disableLighting();
    GlStateManager.disableCull();
    GlStateManager.enableAlpha();
    GlStateManager.color(1.0F, 1.0F, 1.0F);
  }
  
  public static void begin(int mode) {
    INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
  }
  
  public static void release() {
    render();
    releaseGL();
  }
  
  public static void render() {
    INSTANCE.draw();
  }
  
  public static void releaseGL() {
    GlStateManager.enableCull();
    GlStateManager.depthMask(true);
    GlStateManager.enableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.enableDepth();
    GlStateManager.color(1.0F, 1.0F, 1.0F);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  }
  
  public static void drawBox(AxisAlignedBB bb, APColor color, int sides) {
    drawBox(INSTANCE.getBuffer(), bb, color, sides);
  }
  
  public static void drawBox(BlockPos blockPos, APColor color, int sides) {
    drawBox(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F, 1.0F, 1.0F, color, sides);
  }
  
  public static void drawBox(BufferBuilder buffer, AxisAlignedBB bb, APColor color, int sides) {
    drawBox(buffer, (float)bb.minX, (float)bb.minY, (float)bb.minZ, (float)(bb.maxX - bb.minX), (float)(bb.maxY - bb.minY), (float)(bb.maxZ - bb.minZ), color, sides);
  }
  
  public static void drawDownBox(BlockPos blockPos, APColor color, int sides) {
    drawDownBox(INSTANCE.getBuffer(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0F, 1.0F, 1.0F, color, sides);
  }
  
  public static void drawDownBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, APColor color, int sides) {
    drawBox(buffer, x, y, z, w, -h, d, color, sides);
  }
  
  public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, APColor color, int sides) {
    int r = color.getRed(), g = color.getGreen(), b = color.getBlue(), a = color.getAlpha();
    if ((sides & 0x1) != 0) {
      buffer.pos((x + w), y, z).color(r, g, b, a).endVertex();
      buffer.pos((x + w), y, (z + d)).color(r, g, b, a).endVertex();
      buffer.pos(x, y, (z + d)).color(r, g, b, a).endVertex();
      buffer.pos(x, y, z).color(r, g, b, a).endVertex();
    } 
    if ((sides & 0x2) != 0) {
      buffer.pos((x + w), (y + h), z).color(r, g, b, a).endVertex();
      buffer.pos(x, (y + h), z).color(r, g, b, a).endVertex();
      buffer.pos(x, (y + h), (z + d)).color(r, g, b, a).endVertex();
      buffer.pos((x + w), (y + h), (z + d)).color(r, g, b, a).endVertex();
    } 
    if ((sides & 0x4) != 0) {
      buffer.pos((x + w), y, z).color(r, g, b, a).endVertex();
      buffer.pos(x, y, z).color(r, g, b, a).endVertex();
      buffer.pos(x, (y + h), z).color(r, g, b, a).endVertex();
      buffer.pos((x + w), (y + h), z).color(r, g, b, a).endVertex();
    } 
    if ((sides & 0x8) != 0) {
      buffer.pos(x, y, (z + d)).color(r, g, b, a).endVertex();
      buffer.pos((x + w), y, (z + d)).color(r, g, b, a).endVertex();
      buffer.pos((x + w), (y + h), (z + d)).color(r, g, b, a).endVertex();
      buffer.pos(x, (y + h), (z + d)).color(r, g, b, a).endVertex();
    } 
    if ((sides & 0x10) != 0) {
      buffer.pos(x, y, z).color(r, g, b, a).endVertex();
      buffer.pos(x, y, (z + d)).color(r, g, b, a).endVertex();
      buffer.pos(x, (y + h), (z + d)).color(r, g, b, a).endVertex();
      buffer.pos(x, (y + h), z).color(r, g, b, a).endVertex();
    } 
    if ((sides & 0x20) != 0) {
      buffer.pos((x + w), y, (z + d)).color(r, g, b, a).endVertex();
      buffer.pos((x + w), y, z).color(r, g, b, a).endVertex();
      buffer.pos((x + w), (y + h), z).color(r, g, b, a).endVertex();
      buffer.pos((x + w), (y + h), (z + d)).color(r, g, b, a).endVertex();
    } 
  }
  
  public static void drawSphere(double x, double y, double z, float size, int slices, int stacks) {
    Sphere s = new Sphere();
    GL11.glPushMatrix();
    GlStateManager.blendFunc(770, 771);
    GL11.glEnable(3042);
    GL11.glLineWidth(1.2F);
    GL11.glDisable(3553);
    GL11.glDisable(2929);
    GL11.glDepthMask(false);
    s.setDrawStyle(100013);
    GL11.glTranslated(x - (mc.getRenderManager()).renderPosX, y - (mc.getRenderManager()).renderPosY, z - (mc.getRenderManager()).renderPosZ);
    s.draw(size, slices, stacks);
    GL11.glLineWidth(2.0F);
    GL11.glEnable(3553);
    GL11.glEnable(2929);
    GL11.glDepthMask(true);
    GL11.glDisable(3042);
    GL11.glPopMatrix();
  }
  
  public static void drawBoundingBox(AxisAlignedBB bb, float width, APColor color) {
    int red = color.getRed(), green = color.getGreen(), blue = color.getBlue(), alpha = color.getAlpha();
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
    GlStateManager.disableTexture2D();
    GlStateManager.depthMask(false);
    GlStateManager.glLineWidth(width);
    BufferBuilder bufferbuilder = INSTANCE.getBuffer();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
    render();
    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }
  
  public static void drawBoundingBoxBlockPos(BlockPos bp, float width, APColor color) {
    int r = color.getRed(), g = color.getGreen(), b = color.getBlue(), alpha = color.getAlpha();
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
    GlStateManager.disableTexture2D();
    GlStateManager.depthMask(false);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4352);
    GL11.glLineWidth(width);
    Minecraft mc = Minecraft.getMinecraft();
    double x = bp.getX() - (mc.getRenderManager()).viewerPosX;
    double y = bp.getY() - (mc.getRenderManager()).viewerPosY;
    double z = bp.getZ() - (mc.getRenderManager()).viewerPosZ;
    AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    tessellator.draw();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    tessellator.draw();
    bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    tessellator.draw();
    GL11.glDisable(2848);
    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }
  
  public static void drawBoundingBoxBlockPos2(BlockPos bp, float width, APColor color) {
    int r = color.getRed(), g = color.getGreen(), b = color.getBlue(), alpha = color.getAlpha();
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
    GlStateManager.disableTexture2D();
    GlStateManager.depthMask(false);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4352);
    GL11.glLineWidth(width);
    Minecraft mc = Minecraft.getMinecraft();
    double x = bp.getX() - (mc.getRenderManager()).viewerPosX;
    double y = bp.getY() - (mc.getRenderManager()).viewerPosY;
    double z = bp.getZ() - (mc.getRenderManager()).viewerPosZ;
    AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0D, y - 1.0D, z + 1.0D);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    tessellator.draw();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    tessellator.draw();
    bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex();
    tessellator.draw();
    GL11.glDisable(2848);
    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }
  
  public static void drawBoundingBoxBottom2(BlockPos bp, float width, APColor color) {
    int red = color.getRed(), green = color.getGreen(), blue = color.getBlue(), alpha = color.getAlpha();
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    GlStateManager.disableDepth();
    GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
    GlStateManager.disableTexture2D();
    GlStateManager.depthMask(false);
    GL11.glEnable(2848);
    GL11.glHint(3154, 4352);
    GL11.glLineWidth(width);
    Minecraft mc = Minecraft.getMinecraft();
    double x = bp.getX() - (mc.getRenderManager()).viewerPosX;
    double y = bp.getY() - (mc.getRenderManager()).viewerPosY;
    double z = bp.getZ() - (mc.getRenderManager()).viewerPosZ;
    AxisAlignedBB bb = new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
    bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
    tessellator.draw();
    GL11.glDisable(2848);
    GlStateManager.depthMask(true);
    GlStateManager.enableDepth();
    GlStateManager.enableTexture2D();
    GlStateManager.disableBlend();
    GlStateManager.popMatrix();
  }
  
  public static void glBillboard(float x, float y, float z) {
    float scale = 0.02666667F;
    GlStateManager.translate(x - (Minecraft.getMinecraft().getRenderManager()).renderPosX, y - (Minecraft.getMinecraft().getRenderManager()).renderPosY, z - (Minecraft.getMinecraft().getRenderManager()).renderPosZ);
    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
    GlStateManager.rotate(-(Minecraft.getMinecraft()).player.rotationYaw, 0.0F, 1.0F, 0.0F);
    GlStateManager.rotate((Minecraft.getMinecraft()).player.rotationPitch, ((Minecraft.getMinecraft()).gameSettings.thirdPersonView == 2) ? -1.0F : 1.0F, 0.0F, 0.0F);
    GlStateManager.scale(-scale, -scale, scale);
  }
  
  public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
    glBillboard(x, y, z);
    int distance = (int)player.getDistance(x, y, z);
    float scaleDistance = distance / 2.0F / (2.0F + 2.0F - scale);
    if (scaleDistance < 1.0F)
      scaleDistance = 1.0F; 
    GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
  }
}
