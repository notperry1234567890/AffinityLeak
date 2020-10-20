package com.gamesense.client;

import com.gamesense.api.Stopper;
import com.gamesense.api.event.EventProcessor;
import com.gamesense.api.players.enemy.Enemies;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.settings.SettingsManager;
import com.gamesense.api.util.config.LoadConfiguration;
import com.gamesense.api.util.config.LoadModules;
import com.gamesense.api.util.config.SaveConfiguration;
import com.gamesense.api.util.config.SaveModules;
import com.gamesense.api.util.font.CFontRenderer;
import com.gamesense.api.util.world.TpsUtils;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.command.CommandManager;
import com.gamesense.client.macro.MacroManager;
import com.gamesense.client.module.ModuleManager;
import java.awt.Font;
import me.zero.alpine.EventBus;
import me.zero.alpine.EventManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

@Mod(modid = "affinityplus", name = "Affinity Plus", version = "v0.0.1", clientSideOnly = true)
public class AffinityPlus {
  public static final String MODID = "affinityplus";
  
  public static final String MODVER = "v0.0.1";
  
  public static final String FORGENAME = "Affinity Plus";
  
  public static final EventBus EVENT_BUS = (EventBus)new EventManager();
  
  public static final String MODNAME = "Affinity+";
  
  public static final String DISCORDAPPID = "756374885139218473";
  
  public static final Logger log = LogManager.getLogger("Affinity+");
  
  public static CFontRenderer fontRenderer;
  
  public static Enemies enemies;
  
  @Instance
  private static AffinityPlus INSTANCE;
  
  public ClickGUI clickGUI;
  
  public SettingsManager settingsManager;
  
  public Friends friends;
  
  public ModuleManager moduleManager;
  
  public SaveConfiguration saveConfiguration;
  
  public LoadConfiguration loadConfiguration;
  
  public SaveModules saveModules;
  
  public LoadModules loadModules;
  
  public MacroManager macroManager;
  
  EventProcessor eventProcessor;
  
  public AffinityPlus() {
    INSTANCE = this;
  }
  
  public static AffinityPlus getInstance() {
    return INSTANCE;
  }
  
  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {}
  
  @EventHandler
  public void init(FMLInitializationEvent event) {
    this.eventProcessor = new EventProcessor();
    this.eventProcessor.init();
    fontRenderer = new CFontRenderer(new Font("Ariel", 0, 18), true, false);
    TpsUtils tpsUtils = new TpsUtils();
    this.settingsManager = new SettingsManager();
    log.info("Settings initialized!");
    this.friends = new Friends();
    enemies = new Enemies();
    log.info("Friends and enemies initialized!");
    this.moduleManager = new ModuleManager();
    log.info("Modules initialized!");
    this.clickGUI = new ClickGUI();
    log.info("ClickGUI initialized!");
    this.macroManager = new MacroManager();
    log.info("Macros initialized!");
    this.saveConfiguration = new SaveConfiguration();
    Runtime.getRuntime().addShutdownHook((Thread)new Stopper());
    log.info("Config Saved!");
    this.loadConfiguration = new LoadConfiguration();
    log.info("Config Loaded!");
    this.saveModules = new SaveModules();
    Runtime.getRuntime().addShutdownHook((Thread)new Stopper());
    log.info("Modules Saved!");
    this.loadModules = new LoadModules();
    log.info("Modules Loaded!");
    CommandManager.initCommands();
    log.info("Commands initialized!");
    log.info("Initialization complete!\n");
  }
  
  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    Display.setTitle("Affinity+ v0.0.1");
    log.info("Capes initialised!");
    log.info("PostInitialization complete!\n");
  }
}
