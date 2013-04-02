package com.first.tribes.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.first.tribes.core.Tribes;

public class TribesActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Tribes());
  }
}
