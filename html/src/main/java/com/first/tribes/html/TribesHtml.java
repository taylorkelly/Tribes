package com.first.tribes.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.first.tribes.core.Tribes;

public class TribesHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("Tribes/");
    PlayN.run(new Tribes());
  }
}
