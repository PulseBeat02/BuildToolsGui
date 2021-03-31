package com.github.pulsebeat02.buildtoolsgui;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public final class DownloadManager {

  public static File downloadBuildTools() {
    try (final BufferedInputStream in =
            new BufferedInputStream(new URL(ArtifactURLs.BUILDTOOLS_URL).openStream());
        final FileOutputStream fileOutputStream =
            new FileOutputStream(BuildToolsPath.BUILDTOOLS_JAR_PATH)) {
      final byte[] dataBuffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return BuildToolsPath.BUILDTOOLS_JAR_PATH;
  }

  public static int getBuildToolsFileSize() {
    URLConnection conn = null;
    try {
      conn = new URL(ArtifactURLs.BUILDTOOLS_URL).openConnection();
      if (conn instanceof HttpURLConnection) {
        ((HttpURLConnection) conn).setRequestMethod("HEAD");
      }
      conn.getInputStream();
      return conn.getContentLength();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    } finally {
      if (conn instanceof HttpURLConnection) {
        ((HttpURLConnection) conn).disconnect();
      }
    }
  }
}