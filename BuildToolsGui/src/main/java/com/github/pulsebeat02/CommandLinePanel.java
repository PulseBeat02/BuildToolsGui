package com.github.pulsebeat02;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLinePanel extends JPanel {

  private static final long serialVersionUID = -4069304377559214750L;
  private final JTextArea console;
  private final String separator;

  public CommandLinePanel() {
    setToolTipText("Command Line Output");
    console = new JTextArea("Console");
    separator = System.getProperty("line.separator");
  }

  public void startBuildTools(final MinecraftVersion version, final String arguments)
      throws IOException {
    final int size = DownloadManager.getBuildToolsFileSize();
    DownloadManager.downloadBuildTools(
        value -> logInformation("Downloaded " + (value / size) + "%"));
    final File f = new File(System.getProperty("user.dir") + "/BuildTools.jar");
    final Process process =
        new ProcessBuilder(
                "java", "-jar", f.getAbsolutePath(), "--rev", version.getVersion(), arguments)
            .start();
    final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = reader.readLine()) != null) {
      console.append(line);
      console.append(separator);
    }
  }

  public void logInformation(final String line) {
    console.append(line);
    console.append(separator);
  }
}
