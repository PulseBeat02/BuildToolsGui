/*
 * Created by JFormDesigner on Tue Mar 30 17:37:37 EDT 2021
 */

package com.github.pulsebeat02.buildtoolsgui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class BuildToolsMenu extends JFrame {

  private static final long serialVersionUID = 4909403567316540439L;

  private boolean installingBuildTools;
  private JButton start;
  private JComboBox<String> mcSelectionList;
  private JTextField minMemoryField;
  private JTextField maxMemoryField;

  public BuildToolsMenu() {
    initComponents();
  }

  private void initComponents() {

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(800, 480));
    setResizable(false);

    final Container contentPane = getContentPane();
    contentPane.setLayout(null);

    final CommandLinePanel panel = new CommandLinePanel(this);
    final JScrollPane consoleOutput = new JScrollPane();
    consoleOutput.setViewportView(panel);
    consoleOutput.setBounds(20, 130, 760, 220);
    contentPane.add(consoleOutput);

    final JLabel mcVersionLabel = new JLabel();
    mcVersionLabel.setText("Select Minecraft Version");
    mcVersionLabel.setBounds(20, 25, 165, 25);
    contentPane.add(mcVersionLabel);

    final JLabel extraArgumentsLabel = new JLabel();
    extraArgumentsLabel.setText("Extra Arguments");
    extraArgumentsLabel.setBounds(20, 80, 165, 25);
    contentPane.add(extraArgumentsLabel);

    mcSelectionList =
        new JComboBox<>(
            Arrays.stream(MinecraftVersion.values())
                .map(MinecraftVersion::getVersion)
                .toArray(String[]::new));
    mcSelectionList.setBounds(195, 25, 205, 30);
    contentPane.add(mcSelectionList);

    final JTextField extraArgumentsField = new JTextField();
    extraArgumentsField.setBounds(192, 80, 210, 30);
    contentPane.add(extraArgumentsField);

    final JLabel minMemoryLabel = new JLabel();
    minMemoryLabel.setText("Min Memory (-Xmx)");
    minMemoryLabel.setBounds(425, 80, 140, 30);
    contentPane.add(minMemoryLabel);

    minMemoryField = new JTextField();
    minMemoryField.setBounds(570, 80, 210, 30);
    contentPane.add(minMemoryField);

    final JLabel maxMemoryLabel = new JLabel();
    maxMemoryLabel.setText("Max Memory (-Xmx)");
    maxMemoryLabel.setBounds(425, 25, 140, 30);
    contentPane.add(maxMemoryLabel);

    maxMemoryField = new JTextField();
    maxMemoryField.setBounds(570, 25, 210, 30);
    contentPane.add(maxMemoryField);

    start = new JButton();
    start.setText("Start");
    start.setBounds(20, 370, 195, 55);
    start.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(final MouseEvent evt) {
            disableComponents();
            final MinecraftVersion mv =
                MinecraftVersion.fromVersion(String.valueOf(mcSelectionList.getSelectedItem()));
            final StringBuilder sb = new StringBuilder(extraArgumentsField.getText());
            final String minMem = minMemoryField.getText();
            if (minMem != null && !minMem.isEmpty()) {
              sb.append(" -Xms").append(minMem);
            }
            final String maxMem = maxMemoryField.getText();
            if (maxMem != null && !maxMem.isEmpty()) {
              sb.append(" -Xmx").append(maxMem);
            }
            panel.logInformation("Selecting Version: " + mv.getVersion());
            CompletableFuture.runAsync(
                () -> {
                  try {
                    panel.startBuildTools(mv, sb.toString());
                  } catch (final IOException e) {
                    e.printStackTrace();
                  }
                });
          }
        });
    contentPane.add(start);

    final JButton close = new JButton();
    close.setText("Close");
    close.setBounds(585, 370, 195, 55);
    close.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(final MouseEvent evt) {
            if (installingBuildTools) {
              final int result =
                  JOptionPane.showConfirmDialog(
                      null,
                      "Are you sure you want to close?\n" + "(BuildTools will stop)\n",
                      "Close",
                      JOptionPane.YES_NO_OPTION);
              if (result == JOptionPane.YES_OPTION) {
                if (BuildToolsPath.BUILDTOOLS_FOLDER_PATH.delete()) {
                  System.out.println("Deleted BuildTools Folder");
                }
                System.exit(0);
              }
            } else {
              System.exit(0);
            }
          }
        });
    contentPane.add(close);

    final JButton github = new JButton();
    github.setText("View on Github");
    github.setBounds(305, 370, 195, 55);
    github.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(final MouseEvent evt) {
            try {
              Desktop.getDesktop().browse(new URI("https://github.com/PulseBeat02/BuildToolsGui"));
            } catch (final IOException | URISyntaxException ioException) {
              ioException.printStackTrace();
            }
          }
        });
    contentPane.add(github);

    pack();
    setLocationRelativeTo(getOwner());
  }

  public void disableComponents() {
    installingBuildTools = true;
    start.setEnabled(false);
    mcSelectionList.setEnabled(false);
    minMemoryField.setEnabled(false);
    maxMemoryField.setEnabled(false);
  }

  public void enableComponents() {
    installingBuildTools = false;
    start.setEnabled(true);
    mcSelectionList.setEnabled(true);
    minMemoryField.setEnabled(true);
    maxMemoryField.setEnabled(true);
  }
}
