package com.first.tribes.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.first.tribes.core.Tribes;
import playn.java.JavaPlatform.Config;

public class TribesJava {

    public static void main(String[] args) {
        Config config = new Config();
        config.width = 800;
        config.height = 600;
        JavaPlatform.register(config);
        PlayN.run(new Tribes());
    }
}
