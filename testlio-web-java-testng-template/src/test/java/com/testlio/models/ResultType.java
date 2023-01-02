package com.testlio.models;

import java.util.Random;

public enum ResultType {
     PASSED,
     FAILED,
     ERRORED;

     public static ResultType getRandom() {
          Random random = new Random();
          return values()[random.nextInt(values().length)];
     }
}
