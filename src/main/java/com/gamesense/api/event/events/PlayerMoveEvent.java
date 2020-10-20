package com.gamesense.api.event.events;

import com.gamesense.api.event.GameSenseEvent;
import net.minecraft.entity.MoverType;

public class PlayerMoveEvent extends GameSenseEvent {
  public double x;
  
  public double y;
  
  public double z;
  
  MoverType type;
  
  public PlayerMoveEvent(MoverType moverType, double xx, double yy, double zz) {
    this.type = moverType;
    this.x = xx;
    this.y = yy;
    this.z = zz;
  }
  
  public MoverType getType() {
    return this.type;
  }
  
  public void setType(MoverType type) {
    this.type = type;
  }
  
  public double getX() {
    return this.x;
  }
  
  public void setX(double xx) {
    this.x = xx;
  }
  
  public double getY() {
    return this.y;
  }
  
  public void setY(double yy) {
    this.y = yy;
  }
  
  public double getZ() {
    return this.z;
  }
  
  public void setZ(double zz) {
    this.z = zz;
  }
}
