package com.topperbibb.hacktcnj2021.server;

import java.util.*;

/**
 * Manages all created rooms
 */
public class RoomManager {

    // Maximum/minimum size for a game
    public static final int MAX_ROOM_SIZE = 2;
    public static final int MIN_ROOM_SIZE = 2;

    // Maps Rooms to the Connections they contain
    public static Map<Room, List<Connection>> rooms = new HashMap<>();

    // Holds all rooms that have not started a game yet
    public static Set<Room> availableRooms = new HashSet<>();

    /**
     * Returns all Connections associated with a given room
     * @param room the Room requesting its connections
     * @return a list of all Connections in the room
     */
    public static List<Connection> getAllConnections(Room room) {
        return rooms.get(room);
    }

    /**
     * Adds an empty room to {@link #rooms}
     * @param room a new Room to add
     */
    public static void addRoom(Room room) {
        if (rooms.containsKey(room)) return;
        rooms.put(room, new ArrayList<>());
        availableRooms.add(room);
    }

    /**
     * Adds a Connection to a Room
     * @param room the Room to add to
     * @param conn the Connection to associate with {@code room}
     */
    public static void addConnection(Room room, Connection conn) {
        rooms.get(room).add(conn);
        if (canPlay(room)) {
            availableRooms.remove(room);
        }
    }

    /**
     * Removes a Connection from a Room
     * @param room the Room to remove from
     * @param conn the Connection to remove from {@code room}
     */
    public static void removeConnection(Room room, Connection conn) {
        rooms.get(room).remove(conn);
    }

    /**
     * Determines whether a Room can start/continue playing a game
     * @param room the Room in question
     * @return a boolean representing whether {@code room} can start/continue its game
     */
    public static boolean canPlay(Room room) {
        return rooms.get(room).size() >= MIN_ROOM_SIZE && rooms.get(room).size() <= MAX_ROOM_SIZE;
    }

    /**
     * Either returns the next available Room or a new one, to add a new Connection to
     * @return a Room for adding a Connection to
     */
    public static Room getNewRoomForConnection() {
        if (availableRooms.size() == 0) {
            return new Room();
        } else {
            return availableRooms.iterator().next();
        }
    }

    /**
     * Returns the Room associated with a given Connection, or null if none is found
     * @param connection the Connection to search for
     * @return the Room associated with a given Connection, or null if none is found
     */
    @SuppressWarnings("unused")
    public static Room getRoomByConn(Connection connection) {
        for (Room room : rooms.keySet()) {
            if (rooms.get(room).contains(connection)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Searches a Room for a Connection with a given id, and returns it
     * @param room the Room to search in
     * @param id the Connection id to search for
     * @return the Connection in {@code room} associated with the id, or null if none is found
     */
    public static Connection getConnById(Room room, int id) {
        for (Connection connection : getAllConnections(room)) {
            if (connection.id == id) {
                return connection;
            }
        }
        return null;
    }

    /**
     * Removes a Room from {@link #rooms}
     * @param room the Room to remove
     */
    public static void removeRoom(Room room) {
        rooms.remove(room);
    }
}
