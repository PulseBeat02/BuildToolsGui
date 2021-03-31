package com.github.pulsebeat02.buildtoolsgui;

public class ArtifactURLs {

  public static final String BUILDTOOLS_URL =
      "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar";

  public static String getBuildToolsUrl() {
    return BUILDTOOLS_URL;
  }
}
