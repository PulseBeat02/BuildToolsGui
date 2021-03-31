package com.github.pulsebeat02.buildtoolsgui;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLinePanel extends JPanel {

  private static final long serialVersionUID = -4069304377559214750L;

  public CommandLinePanel() {
    setToolTipText("Command Line Output");
    final ConsoleArea console = new ConsoleArea();
    add(console);
    Runtime.getRuntime().addShutdownHook(new Thread(console::close));
  }

  public void startBuildTools(final MinecraftVersion version, final String arguments)
      throws IOException {
    final int size = DownloadManager.getBuildToolsFileSize();
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
  }

  public void logInformation(final String line) {
    System.out.println(line);
  }
}
