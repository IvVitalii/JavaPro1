package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class Room {
    private String name;
    private final Set<User> roomUsers;
    private final List<Message> roomMessages;
    private final Gson gson;
    private int n;

    public Room(String name) {
        this.name = name;
        gson = new GsonBuilder().create();
        roomUsers = new HashSet<>();
        roomMessages = new LinkedList<>();
    }

    public String getName() {
        return name;
    }

    public static Room fromJSON(String s) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, Room.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return name.equals(room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}