package com.first.tribes.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.first.tribes.core.Tribes;

public class TribesJava {

  public static void main(String[] args) {
    JavaPlatform.register();
    PlayN.run(new Tribes());
  }
}
