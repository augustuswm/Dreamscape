package com.tobenamed.dreamscape;

import com.badlogic.gdx.backends.jogl.JoglApplication;

public class DesktopApp {
	  public static void main (String[] argv) {
          new JoglApplication(new App(), "Dreamscape", 480, 320, false);               
  }
}