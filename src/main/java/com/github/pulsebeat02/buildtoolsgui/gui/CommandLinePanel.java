package com.github.pulsebeat02.buildtoolsgui.gui;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

public class CommandLinePanel extends JPanel {

  private static final long serialVersionUID = -4069304377559214750L;

  private final BuildToolsMenu gui;

  public CommandLinePanel(final BuildToolsMenu gui) {
    this.gui = gui;
    setToolTipText("Command Line Output");
    final ConsoleArea console = new ConsoleArea();
    add(console);
    Runtime.getRuntime().addShutdownHook(new Thread(console::close));
  }

  public void startBuildTools(final MinecraftVersion version, final String arguments)
      throws IOException {
    DownloadManager.downloadBuildTools();
    final ProcessBuilder builder =
        new ProcessBuilder(
            "java",
            "-jar",
            BuildToolsPath.getBuildToolsJarPath().getAbsolutePath(),
            "--rev",
            version.getVersion(),
            arguments);
    builder.directory(BuildToolsPath.getBuildToolsFolderPath());
    final Process process = builder.start();
    logProcess(process);
    CompletableFuture.runAsync(
        () -> {
          try {
            if (process.waitFor() == 0) {
              System.out.println("Successfully ran BuildTools");
            } else {
              System.err.println("An Exception has Occurred During BuildTools");
            }
            gui.enableComponents();
          } catch (final InterruptedException e) {
            e.printStackTrace();
          }
        });
  }

  public void logProcess(final Process process) {
    final BufferedReader reader =
        new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line = "";
    while (true) {
      try {
        if ((line = reader.readLine()) == null) {
          break;
        }
      } catch (final IOException e) {
        e.printStackTrace();
      }
      System.out.println(line);
    }
  }
}
