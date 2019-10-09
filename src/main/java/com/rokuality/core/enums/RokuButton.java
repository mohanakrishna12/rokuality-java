package com.rokuality.core.enums;

public enum RokuButton {

  RIGHT_ARROW("rightArrow"), LEFT_ARROW("leftArrow"), DOWN_ARROW("downArrow"), UP_ARROW("upArrow"), SELECT("select"),
  BACK("back"), HOME("home"), PLAY("play"), PAUSE("pause"), FAST_FORWARD("fastForward"), REWIND("rewind"),
  OPTION("option");

  private final String value;

  private RokuButton(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public static RokuButton getEnumByString(String value) {
    for (RokuButton button : RokuButton.values()) {
      if (value.equalsIgnoreCase(button.value)) {
        return button;
      }
    }
    return null;
  }

}
