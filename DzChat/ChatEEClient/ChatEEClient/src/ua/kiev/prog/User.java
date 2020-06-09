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
import java.util.Iterator;

public class User {
    public static final String BUSY = "busy";
    public static final String ONLINE = "online";
    public static final String SLEEP = "sleep";

    private String login;
    private String pass;
    private String status;

    public User(String login, String pass) {
        this.login = login;
        this.pass = pass;
        this.status = ONLINE;
    }

    public String getLogin() {
        return login;
    }

    public String getStatus() {
        return status;
    }

    public boolean checkStatus(String status) {
        if (status.substring(7).equals(BUSY)) {
            this.status = BUSY;
            return true;
        } else if (status.substring(7).equals(ONLINE)) {
            this.status = ONLINE;
            return true;
        } else if (status.substring(7).equals(SLEEP)) {
            this.status = SLEEP;
            return true;
        }
        System.out.println("Wrong status");

        return false;
    }

    public void setStatus(String status) throws IOException {
        if (checkStatus(status)) {
            sendUser(Utils.getURL());
            System.out.println("Status set to: " + this.status);
        }
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public int sendUser(String url) throws IOException {
        URL obj = new URL(url + "/User");
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

    public UsersList getUsers() throws IOException {
        URL obj = new URL(Utils.getURL() + "/User");
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        InputStream is = conn.getInputStream();
        byte[] usersBytes = getBytes(is);
        String bufStr = new String(usersBytes, StandardCharsets.UTF_8);
        UsersList list = UsersList.fromJSON(bufStr);
        return list;


    }

    public void printUsers() throws IOException {
        UsersList list = getUsers();
        Iterator<User> itr = list.getUsers().iterator();
        while (itr.hasNext())
            System.out.println(itr.next());
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
        return "User{" +
                "login='" + login + '\'' +
                "status='" + status + '\'' +
                '}';
    }
}
