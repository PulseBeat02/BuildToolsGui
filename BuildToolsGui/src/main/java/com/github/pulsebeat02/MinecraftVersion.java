package com.github.pulsebeat02;

public enum MinecraftVersion {
  VER_LATEST,
  VER_1_16_5,
  VER_1_16_4,
  VER_1_16_3,
  VER_1_16_2,
  VER_1_16_1,
  VER_1_15_2,
  VER_1_15_1,
  VER_1_15,
  VER_1_14_4,
  VER_1_14_3,
  VER_1_14_2,
  VER_1_14_1,
  VER_1_14,
  VER_1_13_2,
  VER_1_13_1,
  VER_1_13,
  VER_1_12_2,
  VER_1_12_1,
  VER_1_12,
  VER_1_11_2,
  VER_1_11_1,
  VER_1_11,
  VER_1_10_2;

  public static MinecraftVersion fromVersion(final String ver) {
    for (MinecraftVersion mv : values()) {
      if (mv.name().contains(ver)) {
        return mv;
      }
    }
    return null;
  }

  public String getVersion() {
    return name().substring(4).replaceAll("_", ".");
  }
}
