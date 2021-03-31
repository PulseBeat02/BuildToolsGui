package com.github.pulsebeat02.buildtoolsgui;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

public class ApplicationLock {

  public ApplicationLock() {}

  public boolean checkApplication() {
    final String userHome = System.getProperty("user.home");
    final File file = new File(userHome, "buildtoolsgui.lock");
    try {
      final FileChannel fc =
          FileChannel.open(file.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      final FileLock lock = fc.tryLock();
      if (lock == null) {
        return true;
      }
    } catch (final IOException e) {
      throw new Error(e);
    }
    return false;
  }
}
