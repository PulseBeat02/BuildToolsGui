package com.github.pulsebeat02.buildtoolsgui;

import com.github.pulsebeat02.buildtoolsgui.gui.BuildToolsMenu;

public class Main {

  public static void main(final String[] args) {
    if (new ApplicationLock().checkApplication()) {
      return;
    }
    final BuildToolsMenu menu = new BuildToolsMenu();
    menu.setVisible(true);
  }
}
