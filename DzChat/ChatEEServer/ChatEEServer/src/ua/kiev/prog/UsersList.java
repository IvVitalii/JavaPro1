package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;
import java.util.Set;

public class UsersList {
    private static final UsersList userList = new UsersList();
    private final Set<User> users;

    private UsersList() {
        users = new HashSet<>();
    }

    public static UsersList getInstance() {
        return userList;
    }
    public synchronized void addUser(User user){
        users.add(user);

    }

    public Set<User> getUsers() {
        return users;
    }
    public synchronized String toJSON() {
        Gson gson=new GsonBuilder().create();
        return gson.toJson(this);
    }
}
