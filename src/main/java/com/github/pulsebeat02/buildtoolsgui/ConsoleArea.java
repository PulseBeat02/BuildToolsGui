package com.github.pulsebeat02.buildtoolsgui;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

public class ConsoleArea extends JTextPane implements Runnable {

  private static final long serialVersionUID = 3758732842473877336L;

  private final Thread stdOutReader;
  private final Thread stdErrReader;
  private final PipedInputStream stdOutPin;
  private final PipedInputStream stdErrPin;
  private final StyledDocument doc;
  private final Style style;
  private boolean stopThreads;

  public ConsoleArea() {

    setVisible(true);
    setEditable(false);
    setBackground(Color.WHITE);
    stdOutPin = new PipedInputStream();
    stdErrPin = new PipedInputStream();
    doc = (StyledDocument) getDocument();
    style = doc.addStyle("ConsoleStyle", null);
    StyleConstants.setFontFamily(style, "MonoSpaced");
    StyleConstants.setFontSize(style, 12);

    try {
      final PipedOutputStream stdOutPos = new PipedOutputStream(stdOutPin);
      final PipedOutputStream stdErrPos = new PipedOutputStream(stdErrPin);
      System.setOut(new PrintStream(stdOutPos, true));
      System.setErr(new PrintStream(stdErrPos, true));
    } catch (final IOException | SecurityException io) {
      setText("Couldn't redirect output to this console!\n" + io.getMessage());
    }

    stopThreads = false;

    stdOutReader = new Thread(this);
    stdOutReader.setDaemon(true);
    stdOutReader.start();

    stdErrReader = new Thread(this);
    stdErrReader.setDaemon(true);
    stdErrReader.start();
  }

  public synchronized void close() {
    stopThreads = true;
    this.notifyAll();
    try {
      stdOutReader.join(1000);
      stdOutPin.close();
    } catch (final Exception ignored) {
    }
    try {
      stdErrReader.join(1000);
      stdErrPin.close();
    } catch (final Exception ignored) {
    }
  }

  @Override
  public synchronized void run() {
    try {
      while (Thread.currentThread() == stdOutReader) {
        try {
          this.wait(100);
        } catch (final InterruptedException ignored) {
        }
        if (stdOutPin.available() != 0) {
          final String input = this.readLine(stdOutPin);
          StyleConstants.setForeground(style, Color.black);
          doc.insertString(doc.getLength(), input, style);
          setCaretPosition(getDocument().getLength());
        }
        if (stopThreads) {
          return;
        }
      }
      while (Thread.currentThread() == stdErrReader) {
        try {
          this.wait(100);
        } catch (final InterruptedException ignored) {
        }
        if (stdErrPin.available() != 0) {
          final String input = this.readLine(stdErrPin);
          StyleConstants.setForeground(style, Color.red);
          doc.insertString(doc.getLength(), input, style);
          setCaretPosition(getDocument().getLength());
        }
        if (stopThreads) {
          return;
        }
      }
    } catch (final Exception e) {
      setText("ERROR: " + e);
    }
  }

  private synchronized String readLine(final PipedInputStream in) throws IOException {
    final StringBuilder input = new StringBuilder();
    do {
      final int available = in.available();
      if (available == 0) {
        break;
      }
      final byte[] b = new byte[available];
      final int status = in.read(b);
      input.append(new String(b, 0, b.length));
    } while (!input.toString().endsWith("\n")
        && !input.toString().endsWith("\r\n")
        && !stopThreads);
    return input.toString();
  }
}
