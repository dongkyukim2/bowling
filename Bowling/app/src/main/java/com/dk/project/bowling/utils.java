package com.dk.project.bowling;

public class utils {

  public static float getScorePercent(int score) {
    return 1.0f - (score / 300.0f);
  }
}
