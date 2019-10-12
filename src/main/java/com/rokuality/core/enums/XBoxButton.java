package com.rokuality.core.enums;

public enum XBoxButton {

  RIGHT_ARROW("DirectionRight"), LEFT_ARROW("DirectionLeft"), DOWN_ARROW("DirectionDown"), 
      UP_ARROW("DirectionUp"), SELECT("A"), BACK("B"), OPTION("Menu"),
      PLAY ("Play"), PAUSE ("Pause"), FAST_FORWARD ("FastForward"), REWIND ("Rewind");

  private final String value;

  private XBoxButton(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public static XBoxButton getEnumByString(String value) {
    for (XBoxButton button : XBoxButton.values()) {
      if (value.equalsIgnoreCase(button.value)) {
        return button;
      }
    }
    return null;
  }

}