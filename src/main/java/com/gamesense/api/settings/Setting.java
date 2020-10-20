package com.gamesense.api.settings;

import com.gamesense.api.util.APColor;
import com.gamesense.client.module.Module;
import java.util.List;

public abstract class Setting {
  private final String name;
  
  private final String configname;
  
  private final Module parent;
  
  private final Module.Category category;
  
  private final Type type;
  
  public Setting(String name, String configname, Module parent, Module.Category category, Type type) {
    this.name = name;
    this.configname = configname;
    this.parent = parent;
    this.type = type;
    this.category = category;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getConfigName() {
    return this.configname;
  }
  
  public Module getParent() {
    return this.parent;
  }
  
  public Type getType() {
    return this.type;
  }
  
  public Module.Category getCategory() {
    return this.category;
  }
  
  public enum Type {
    INT, DOUBLE, BOOLEAN, MODE, COLOR;
  }
  
  public static class Integer extends Setting {
    private final int min;
    
    private final int max;
    
    private int value;
    
    public Integer(String name, String configname, Module parent, Module.Category category, int value, int min, int max) {
      super(name, configname, parent, category, Setting.Type.INT);
      this.value = value;
      this.min = min;
      this.max = max;
    }
    
    public int getValue() {
      return this.value;
    }
    
    public void setValue(int value) {
      this.value = value;
    }
    
    public int getMin() {
      return this.min;
    }
    
    public int getMax() {
      return this.max;
    }
  }
  
  public static class Double extends Setting {
    private final double min;
    
    private final double max;
    
    private double value;
    
    public Double(String name, String configname, Module parent, Module.Category category, double value, double min, double max) {
      super(name, configname, parent, category, Setting.Type.DOUBLE);
      this.value = value;
      this.min = min;
      this.max = max;
    }
    
    public double getValue() {
      return this.value;
    }
    
    public void setValue(double value) {
      this.value = value;
    }
    
    public double getMin() {
      return this.min;
    }
    
    public double getMax() {
      return this.max;
    }
  }
  
  public static class Boolean extends Setting {
    private boolean value;
    
    public Boolean(String name, String configname, Module parent, Module.Category category, boolean value) {
      super(name, configname, parent, category, Setting.Type.BOOLEAN);
      this.value = value;
    }
    
    public boolean getValue() {
      return this.value;
    }
    
    public void setValue(boolean value) {
      this.value = value;
    }
  }
  
  public static class Mode extends Setting {
    private final List<String> modes;
    
    private String value;
    
    public Mode(String name, String configname, Module parent, Module.Category category, List<String> modes, String value) {
      super(name, configname, parent, category, Setting.Type.MODE);
      this.value = value;
      this.modes = modes;
    }
    
    public String getValue() {
      return this.value;
    }
    
    public void setValue(String value) {
      this.value = value;
    }
    
    public List<String> getModes() {
      return this.modes;
    }
  }
  
  public static class ColorSetting extends Setting {
    private boolean rainbow;
    
    private APColor value;
    
    public ColorSetting(String name, String configname, Module parent, Module.Category category, boolean rainbow, APColor value) {
      super(name, configname, parent, category, Setting.Type.COLOR);
      this.rainbow = rainbow;
      this.value = value;
    }
    
    public APColor getValue() {
      if (this.rainbow)
        return APColor.fromHSB((float)(System.currentTimeMillis() % 11520L) / 11520.0F, 1.0F, 1.0F); 
      return this.value;
    }
    
    public void setValue(boolean rainbow, APColor value) {
      this.rainbow = rainbow;
      this.value = value;
    }
    
    public int toInteger() {
      return this.value.getRGB() & 16777215 + (this.rainbow ? 1 : 0) * 16777216;
    }
    
    public void fromInteger(int number) {
      this.value = new APColor(number & 0xFFFFFF);
      this.rainbow = ((number & 0x1000000) != 0);
    }
    
    public APColor getColor() {
      return this.value;
    }
    
    public boolean getRainbow() {
      return this.rainbow;
    }
  }
}
