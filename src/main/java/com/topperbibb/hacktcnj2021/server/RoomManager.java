package com.topperbibb.hacktcnj2021.server;

import java.util.*;

public class RoomManager {

    public static final int MAX_ROOM_SIZE = 2;
    public static final int MIN_ROOM_SIZE = 2;

    public static Map<Room, List<Connection>> rooms = new HashMap<>();
    public static Set<Room> availableRooms = new HashSet<>();

    public static List<Connection> getAllConnections(Room room) {
        return rooms.get(room);
    }

    public static void addRoom(Room room) {
        rooms.put(room, new ArrayList<>());
        availableRooms.add(room);
    }

    public static void addConnection(Room room, Connection conn) {
        rooms.get(room).add(conn);
        if (canPlay(room)) {
            availableRooms.remove(room);
        }
    }

    public static void removeConnection(Room room, Connection conn) {
        rooms.get(room).remove(conn);
    }

    public static boolean canPlay(Room room) {
        return rooms.get(room).size() >= MIN_ROOM_SIZE && rooms.get(room).size() <= MAX_ROOM_SIZE;
    }

    public static Room getNewRoomForConnection() {
        if (availableRooms.size() == 0) {
            return new Room();
        } else {
            return availableRooms.iterator().next();
        }
    }

    public static Room getRoomByConn(Connection connection) {
        for (Room room : rooms.keySet()) {
            if (rooms.get(room).contains(connection)) {
                return room;
            }
        }
        return null;
    }

    public static Connection getConnById(Room room, int id) {
        for (Connection connection : getAllConnections(room)) {
            if (connection.id == id) {
                return connection;
            }
        }
        return null;
    }

    public static void removeRoom(Room room) {
        rooms.remove(room);
    }
}
