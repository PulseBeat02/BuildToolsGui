package com.github.pulsebeat02.buildtoolsgui;

import javax.swing.*;

public class SwingTest {
  public static void main(final String[] args) {
    final JFrame frame = new JFrame();
    frame.add(new ConsoleArea());
    frame.setVisible(true);
    System.out.println("test");
  }
}
