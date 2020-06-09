package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;

public class Message {
    public static final String EXIT="exit";
    public static final String GET_USERS="users";
    public static final String GET_ROOMS="rooms";
    public static final String STATUS="status";
    public static final String ROOM="room";


    private Date date = new Date();
    private String from;
    private String to;
    private String text;
    private boolean toSend;
    private User user;


    public Message(User user, String text) {
        this.text = text;
        this.user = user;
        this.from = user.getLogin();
        this.toSend=false;
    }

    public Message() {
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static Message fromJSON(String s) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, Message.class);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("[").append(date)
                .append(", From: ").append(from).append(", To: ").append(to)
                .append("] ").append(text)
                .toString();
    }

    public int send(String url) throws IOException {
        URL obj = new URL(url);
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

    public boolean toSmbCheck() {
        if (text.split(":").length == 2) {
            return true;
        }
        return false;
    }

    public boolean targetCheck() throws IOException {
        if (toSmbCheck()) {
            UsersList users = user.getUsers();
            Set<User> set = users.getUsers();
            String[] msg = (text.split(":"));
            for (User u : set
            ) {
                if (msg[0].equals(u.getLogin())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void msgBuild() throws IOException {
        if (targetCheck()) {
            String[] msg = (text.split(":"));
            text = msg[1];
            to = msg[0];
            toSend = true;
        } else if (toSmbCheck() && !targetCheck()) {
            System.out.println("No such user");
        } else if (!toSmbCheck()) {
            to ="all";
            toSend=true;
        }
    }


    public boolean isToSend() {
        return toSend;
    }

    public void setToSend(boolean toSend) {
        this.toSend = toSend;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
