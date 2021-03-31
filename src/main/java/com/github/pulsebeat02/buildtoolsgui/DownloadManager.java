package com.github.pulsebeat02.buildtoolsgui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public final class DownloadManager {

  public static File downloadBuildTools() {
    final File file = BuildToolsPath.BUILDTOOLS_JAR_PATH;
    try (final BufferedInputStream in =
            new BufferedInputStream(new URL(ArtifactURLs.getBuildToolsUrl()).openStream());
        final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      final byte[] dataBuffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return file;
  }
}
