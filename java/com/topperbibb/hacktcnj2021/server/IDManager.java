package com.topperbibb.hacktcnj2021.server;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IDManager {

    public static int currentId = 0;

    public static ConcurrentLinkedQueue<Integer> availableQueue = new ConcurrentLinkedQueue<>();

    public static int generateID() {
        int id;
        if (availableQueue.size() == 0) {
            id = currentId;
            currentId++;
        } else {
            id = availableQueue.poll();
        }
        return id;
    }

    public static void addAvailableId(int id) {
        availableQueue.offer(id);
    }
}
