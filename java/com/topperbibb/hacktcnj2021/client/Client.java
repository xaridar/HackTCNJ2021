package com.topperbibb.hacktcnj2021.client;

import java.io.IOException;
import java.util.Arrays;

public class Client {
  public static void main(String[] args) {
    System.out.println("HELLO");
    try {
      System.out.println(Arrays.toString(Client.class.getClassLoader().getResourceAsStream("text.txt").readAllBytes()));
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
    }
  }
}
