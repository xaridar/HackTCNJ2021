package com.topperbibb.hacktcnj2021.server;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Manages IDs for the server, and generates new IDs for each connections based on connected clients
 */
public class IDManager {

    // Holds the current ID value
    public static int currentId = 0;

    // A ConcurrentLinkedQueue containing the discarded IDs from disconnected clients
    public static ConcurrentLinkedQueue<Integer> availableQueue = new ConcurrentLinkedQueue<>();

    /**
     * Finds a new ID for a connecting client
     * If {@link #availableQueue} is not empty, an ID is removed from it; otherwise, {@link #currentId} is incremented
     * @return a new ID
     */
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

    /**
     * Adds an ID to {@link #availableQueue}
     * @param id a newly available ID
     */
    public static void addAvailableId(int id) {
        availableQueue.offer(id);
    }
}
