package com.gamesense.api.util.world;

public class Timer {
  private long current = System.currentTimeMillis();
  
  public boolean hasReached(long delay) {
    return (System.currentTimeMillis() - this.current >= delay);
  }
  
  public boolean hasReached(long delay, boolean reset) {
    if (reset)
      reset(); 
    return (System.currentTimeMillis() - this.current >= delay);
  }
  
  public void reset() {
    this.current = System.currentTimeMillis();
  }
  
  public long getTimePassed() {
    return System.currentTimeMillis() - this.current;
  }
  
  public boolean sleep(long time) {
    if (time() >= time) {
      reset();
      return true;
    } 
    return false;
  }
  
  public long time() {
    return System.currentTimeMillis() - this.current;
  }
}
