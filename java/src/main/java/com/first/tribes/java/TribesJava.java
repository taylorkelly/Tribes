package com.first.tribes.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.first.tribes.core.Tribes;
import playn.java.JavaPlatform.Config;

public class TribesJava {

    public static void main(String[] args) {
        Config config = new Config();
        JavaPlatform platform = JavaPlatform.register(config);
        platform.graphics().setSize(platform.graphics().screenWidth() - 100, platform.graphics().screenHeight() - 100);
        PlayN.run(new Tribes(platform.graphics().screenWidth() - 100, platform.graphics().screenHeight() - 100));
    }
}
