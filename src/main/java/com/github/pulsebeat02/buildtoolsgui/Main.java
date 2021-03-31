package com.github.pulsebeat02.buildtoolsgui;

public class Main {

  public static void main(final String[] args) {
    if (new ApplicationLock().checkApplication()) {
      return;
    }
    new BuildToolsMenu();
  }
}
