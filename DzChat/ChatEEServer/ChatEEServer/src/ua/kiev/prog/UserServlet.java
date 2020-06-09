package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

@WebServlet(name = "User", urlPatterns = {"/User"})
public class UserServlet extends HttpServlet {
    private UsersList usersList = UsersList.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        byte[] buf = requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);

        User user = User.fromJSON(bufStr);
        if (user != null) {
            boolean toAdd = true;
            for (User u : usersList.getUsers()
            ) {
                if (user.equals(u)) {
                    u.setStatus(user.getStatus());
                    toAdd=false;
                    break;
                } else if (user.getLogin().equals(u.getLogin())) {
                    toAdd=false;
                    resp.setStatus(210);
                    break;
                } else {
                    toAdd = true;
                }
            }
            if (toAdd)
                usersList.addUser(user);
        } else
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OutputStream os = resp.getOutputStream();
        try {
            String json = toJSON(usersList);
            os.write(json.getBytes(StandardCharsets.UTF_8));

        } finally {
            os.close();
        }


    }

    public String toJSON(UsersList usersList) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(usersList);
    }

    private byte[] requestBodyToArray(HttpServletRequest req) throws IOException {
        InputStream is = req.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

}
