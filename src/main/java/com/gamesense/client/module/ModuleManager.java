//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.util.render.GameSenseTessellator;
import com.gamesense.client.module.modules.combat.Auto32K;
import com.gamesense.client.module.modules.combat.AutoArmor;
import com.gamesense.client.module.modules.combat.AutoCrystal;
import com.gamesense.client.module.modules.combat.AutoCrystalRewrite;
import com.gamesense.client.module.modules.combat.AutoOffhand;
import com.gamesense.client.module.modules.combat.AutoTotem;
import com.gamesense.client.module.modules.combat.AutoTrap;
import com.gamesense.client.module.modules.combat.AutoWeb;
import com.gamesense.client.module.modules.combat.BedAura;
import com.gamesense.client.module.modules.combat.BetterSurround;
import com.gamesense.client.module.modules.combat.BruceAura;
import com.gamesense.client.module.modules.combat.FastBow;
import com.gamesense.client.module.modules.combat.FootXp;
import com.gamesense.client.module.modules.combat.HoleFill;
import com.gamesense.client.module.modules.combat.KillAura;
import com.gamesense.client.module.modules.combat.OffhandCrystal;
import com.gamesense.client.module.modules.combat.OffhandGap;
import com.gamesense.client.module.modules.combat.SelfTrap;
import com.gamesense.client.module.modules.combat.SmartOffhand;
import com.gamesense.client.module.modules.combat.Surround;
import com.gamesense.client.module.modules.combat.YakgodAura;
import com.gamesense.client.module.modules.exploits.FastBreak;
import com.gamesense.client.module.modules.exploits.LiquidInteract;
import com.gamesense.client.module.modules.exploits.NoInteract;
import com.gamesense.client.module.modules.exploits.NoSwing;
import com.gamesense.client.module.modules.exploits.PopbobCoordExploit;
import com.gamesense.client.module.modules.exploits.PortalGodMode;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.HUD;
import com.gamesense.client.module.modules.hud.Notifications;
import com.gamesense.client.module.modules.hud.TextRadar;
import com.gamesense.client.module.modules.misc.Announcer;
import com.gamesense.client.module.modules.misc.AutoGG;
import com.gamesense.client.module.modules.misc.AutoReply;
import com.gamesense.client.module.modules.misc.AutoTool;
import com.gamesense.client.module.modules.misc.ChatModifier;
import com.gamesense.client.module.modules.misc.ChatSuffix;
import com.gamesense.client.module.modules.misc.DiscordRPCModule;
import com.gamesense.client.module.modules.misc.FakePlayer;
import com.gamesense.client.module.modules.misc.FastPlace;
import com.gamesense.client.module.modules.misc.MCF;
import com.gamesense.client.module.modules.misc.MultiTask;
import com.gamesense.client.module.modules.misc.NoEntityTrace;
import com.gamesense.client.module.modules.misc.NoKick;
import com.gamesense.client.module.modules.misc.PvPInfo;
import com.gamesense.client.module.modules.misc.Refill;
import com.gamesense.client.module.modules.misc.SkiddaOnBottomDupe;
import com.gamesense.client.module.modules.movement.FastFall;
import com.gamesense.client.module.modules.movement.LongJump;
import com.gamesense.client.module.modules.movement.PlayerTweaks;
import com.gamesense.client.module.modules.movement.ReverseStep;
import com.gamesense.client.module.modules.movement.Speed;
import com.gamesense.client.module.modules.movement.Sprint;
import com.gamesense.client.module.modules.movement.Step;
import com.gamesense.client.module.modules.movement.Velocity;
import com.gamesense.client.module.modules.render.BlockHighlight;
import com.gamesense.client.module.modules.render.CapesModule;
import com.gamesense.client.module.modules.render.EntityESP;
import com.gamesense.client.module.modules.render.Freecam;
import com.gamesense.client.module.modules.render.Fullbright;
import com.gamesense.client.module.modules.render.HitSpheres;
import com.gamesense.client.module.modules.render.HoleESP;
import com.gamesense.client.module.modules.render.LogoutSpots;
import com.gamesense.client.module.modules.render.MobOwner;
import com.gamesense.client.module.modules.render.Nametags;
import com.gamesense.client.module.modules.render.NoRender;
import com.gamesense.client.module.modules.render.RenderTweaks;
import com.gamesense.client.module.modules.render.ShulkerViewer;
import com.gamesense.client.module.modules.render.StorageESP;
import com.gamesense.client.module.modules.render.Tracers;
import com.gamesense.client.module.modules.render.ViewModel;
import com.gamesense.client.module.modules.render.VoidESP;
import java.util.ArrayList;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ModuleManager {
  public static ArrayList<Module> modules;
  
  public ModuleManager() {
    modules = new ArrayList<>();
    addMod((Module)new AutoArmor());
    addMod((Module)new AutoCrystal());
    addMod((Module)new AutoCrystalRewrite());
    addMod((Module)new Surround());
    addMod((Module)new BetterSurround());
    addMod((Module)new AutoTotem());
    addMod((Module)new AutoTrap());
    addMod((Module)new AutoWeb());
    addMod((Module)new FastBow());
    addMod((Module)new HoleFill());
    addMod((Module)new KillAura());
    addMod((Module)new BedAura());
    addMod((Module)new Auto32K());
    addMod((Module)new BruceAura());
    addMod((Module)new YakgodAura());
    addMod((Module)new AutoOffhand());
    addMod((Module)new SmartOffhand());
    addMod((Module)new OffhandCrystal());
    addMod((Module)new OffhandGap());
    addMod((Module)new FootXp());
    addMod((Module)new SelfTrap());
    addMod((Module)new PopbobCoordExploit());
    addMod((Module)new FastBreak());
    addMod((Module)new LiquidInteract());
    addMod((Module)new NoInteract());
    addMod((Module)new NoSwing());
    addMod((Module)new PortalGodMode());
    addMod((Module)new LongJump());
    addMod((Module)new FastFall());
    addMod((Module)new PlayerTweaks());
    addMod((Module)new ReverseStep());
    addMod((Module)new Speed());
    addMod((Module)new Sprint());
    addMod((Module)new Step());
    addMod((Module)new Velocity());
    addMod((Module)new Announcer());
    addMod((Module)new AutoGG());
    addMod((Module)new AutoReply());
    addMod((Module)new AutoTool());
    addMod((Module)new ChatModifier());
    addMod((Module)new ChatSuffix());
    addMod((Module)new DiscordRPCModule());
    addMod((Module)new FastPlace());
    addMod((Module)new Refill());
    addMod((Module)new SkiddaOnBottomDupe());
    addMod((Module)new FakePlayer());
    addMod((Module)new MCF());
    addMod((Module)new MultiTask());
    addMod((Module)new NoEntityTrace());
    addMod((Module)new NoKick());
    addMod((Module)new PvPInfo());
    addMod((Module)new BlockHighlight());
    addMod((Module)new CapesModule());
    addMod((Module)new EntityESP());
    addMod((Module)new Freecam());
    addMod((Module)new Fullbright());
    addMod((Module)new HitSpheres());
    addMod((Module)new HoleESP());
    addMod((Module)new LogoutSpots());
    addMod((Module)new MobOwner());
    addMod((Module)new Nametags());
    addMod((Module)new NoRender());
    addMod((Module)new RenderTweaks());
    addMod((Module)new ShulkerViewer());
    addMod((Module)new StorageESP());
    addMod((Module)new Tracers());
    addMod((Module)new ViewModel());
    addMod((Module)new VoidESP());
    addMod((Module)new ClickGuiModule());
    addMod((Module)new ColorMain());
    addMod((Module)new HUD());
    addMod((Module)new Notifications());
    addMod((Module)new TextRadar());
  }
  
  public static void addMod(Module m) {
    modules.add(m);
  }
  
  public static void onUpdate() {
    modules.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
  }
  
  public static void onRender() {
    modules.stream().filter(Module::isEnabled).forEach(Module::onRender);
  }
  
  public static void onWorldRender(RenderWorldLastEvent event) {
    (Minecraft.getMinecraft()).profiler.startSection("gamesense");
    (Minecraft.getMinecraft()).profiler.startSection("setup");
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    GlStateManager.shadeModel(7425);
    GlStateManager.disableDepth();
    GlStateManager.glLineWidth(1.0F);
    Vec3d renderPos = getInterpolatedPos((Entity)(Minecraft.getMinecraft()).player, event.getPartialTicks());
    RenderEvent e = new RenderEvent((Tessellator)GameSenseTessellator.INSTANCE, renderPos, event.getPartialTicks());
    e.resetTranslation();
    (Minecraft.getMinecraft()).profiler.endSection();
    modules.stream().filter(module -> module.isEnabled()).forEach(module -> {
          (Minecraft.getMinecraft()).profiler.startSection(module.getName());
          module.onWorldRender(e);
          (Minecraft.getMinecraft()).profiler.endSection();
        });
    (Minecraft.getMinecraft()).profiler.startSection("release");
    GlStateManager.glLineWidth(1.0F);
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
    GlStateManager.enableDepth();
    GlStateManager.enableCull();
    GameSenseTessellator.releaseGL();
    (Minecraft.getMinecraft()).profiler.endSection();
    (Minecraft.getMinecraft()).profiler.endSection();
  }
  
  public static ArrayList<Module> getModules() {
    return modules;
  }
  
  public static ArrayList<Module> getModulesInCategory(Module.Category c) {
    ArrayList<Module> list = (ArrayList<Module>)getModules().stream().filter(m -> m.getCategory().equals(c)).collect(Collectors.toList());
    return list;
  }
  
  public static void onBind(int key) {
    if (key == 0 || key == 0)
      return; 
    modules.forEach(module -> {
          if (module.getBind() == key)
            module.toggle(); 
        });
  }
  
  public static Module getModuleByName(String name) {
    Module m = getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    return m;
  }
  
  public static boolean isModuleEnabled(String name) {
    Module m = getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    return m.isEnabled();
  }
  
  public static boolean isModuleEnabled(Module m) {
    return m.isEnabled();
  }
  
  public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
    return (new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ)).add(getInterpolatedAmount(entity, ticks));
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
    return getInterpolatedAmount(entity, ticks, ticks, ticks);
  }
  
  public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
    return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
  }
}
