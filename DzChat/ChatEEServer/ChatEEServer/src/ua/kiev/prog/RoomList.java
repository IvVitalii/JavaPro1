package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;
import java.util.Set;

public class RoomList {
    private static final RoomList roomList = new RoomList();
    private final Set<Room> rooms;

    private RoomList() {
        rooms = new HashSet<>();
    }

    public static RoomList getInstance() {
        return roomList;
    }
    public synchronized void addRoom(Room room){
        rooms.add(room);

    }

    public Set<Room> getRooms() {
        return rooms;
    }
    public synchronized String toJSON() {
        Gson gson=new GsonBuilder().create();
        return gson.toJson(this);
    }
}
