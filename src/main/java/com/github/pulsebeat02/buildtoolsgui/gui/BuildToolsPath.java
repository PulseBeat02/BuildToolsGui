package com.github.pulsebeat02.buildtoolsgui.gui;

import java.io.File;

public class BuildToolsPath {

  public static final File WORKING_DIRECTORY;
  public static final File BUILDTOOLS_FOLDER_PATH;
  public static final File BUILDTOOLS_JAR_PATH;

  static {
    WORKING_DIRECTORY = new File(System.getProperty("user.dir"));
    BUILDTOOLS_FOLDER_PATH = new File(WORKING_DIRECTORY, "/BuildTools");
    if (!BUILDTOOLS_FOLDER_PATH.isDirectory()) {
      if (BUILDTOOLS_FOLDER_PATH.mkdir()) {
        System.out.println("Made BuildTools Directory");
      }
    }
    BUILDTOOLS_JAR_PATH = new File(BUILDTOOLS_FOLDER_PATH, "/BuildTools.jar");
  }

  public static File getWorkingDirectory() {
    return WORKING_DIRECTORY;
  }

  public static File getBuildToolsFolderPath() {
    return BUILDTOOLS_FOLDER_PATH;
  }

  public static File getBuildToolsJarPath() {
    return BUILDTOOLS_JAR_PATH;
  }
}
