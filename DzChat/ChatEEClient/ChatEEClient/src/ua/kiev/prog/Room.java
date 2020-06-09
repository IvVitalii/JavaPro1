package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Room implements Runnable {
    private String name;
    private final Set<User> roomUsers;
    private final List<Message> roomMessages;
    private final Gson gson;
    private int n;

    public Room(String name) {
        this.name = name;
        roomUsers = new HashSet<>();
        roomMessages = new LinkedList<>();
        gson = new GsonBuilder().create();
        n = 0;
    }

    public String getName() {
        return name;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public int sendRoom() throws IOException {
        URL obj = new URL(Utils.getURL() + "/room");
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        OutputStream os = conn.getOutputStream();
        try {
            String json = toJSON();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            return conn.getResponseCode();
        } finally {
            os.close();
        }
    }

    public static RoomList getRoomsList() throws IOException {
        URL obj = new URL(Utils.getURL() + "/room");
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        InputStream is = conn.getInputStream();
        byte[] usersBytes = getBytes(is);
        String bufStr = new String(usersBytes, StandardCharsets.UTF_8);
        RoomList list = RoomList.fromJSON(bufStr);
        return list;


    }

    public static void printRooms() throws IOException {
        RoomList list = getRoomsList();
        if (list != null) {
            Iterator<Room> itr = list.getRooms().iterator();
            while (itr.hasNext()) {
                System.out.println(itr.next());
            }
            if (list.getRooms().isEmpty())
                System.out.println("No rooms");
        }
    }

    public void chatRoom(User user) throws IOException {
        Thread chatRoom = new Thread(this);
        chatRoom.setDaemon(true);
        chatRoom.start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String text = scanner.nextLine();
            Message m = new Message(user, text);
            m.msgBuild();
            roomMessages.add(m);
            this.sendRoom();
            n++;
        }

    }

    public void addRoomUser(User user) {
        roomUsers.add(user);
    }

    public Set<User> getRoomUsers() {

        return roomUsers;
    }

    static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                '}';
    }


    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/room?refresh=" + "yes");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                InputStream is = http.getInputStream();
                try {
                    byte[] buf = responseBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    Room room = gson.fromJson(strBuf, Room.class);
                    if (room != null && !roomMessages.isEmpty()) {
                        for (int i = n - 1; i >= 0; i--) {
                            Message m = roomMessages.get(roomMessages.size() - 1);
                            System.out.println("Room:" + name + " " + m);
                            n--;
                        }
                    }
                } finally {
                    is.close();
                }

                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] responseBodyToArray(InputStream is) throws IOException {
        return User.getBytes(is);
    }
}