package com.github.pulsebeat02.buildtoolsgui;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;

public class CommandLinePanel extends JPanel {

  private static final long serialVersionUID = -4069304377559214750L;

  private final BuildToolsGui gui;

  public CommandLinePanel(final BuildToolsGui gui) {
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
            BuildToolsPath.BUILDTOOLS_JAR_PATH.getAbsolutePath(),
            "--rev",
            version.getVersion(),
            arguments);
    builder.directory(BuildToolsPath.BUILDTOOLS_FOLDER_PATH);
    final Process process = builder.start();
    final BufferedReader reader =
        new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      logInformation(line);
    }
    CompletableFuture.runAsync(() -> {
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



  public void logInformation(final String line) {
    System.out.println(line);
  }
}
