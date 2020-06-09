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

@WebServlet(name = "room", urlPatterns = {"/room"})
public class RoomServlet extends HttpServlet {
    private RoomList roomList = RoomList.getInstance();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        byte[] buf = requestBodyToArray(req);
        String bufStr = new String(buf, StandardCharsets.UTF_8);
        String refresh = req.getParameter("refresh");
        Room room = Room.fromJSON(bufStr);
        boolean ref = false;
        if (refresh.equals("yes")) {
            roomList.addRoom(room);
            ref = true;
        }
        if (room != null && !ref) {
            boolean toAdd = true;
            for (Room r : roomList.getRooms()
            ) {
                if (room.getName().equals(r.getName())) {
                    toAdd = false;
                    resp.setStatus(210);
                    break;
                } else {
                    toAdd = true;
                }
            }
            if (toAdd)
                roomList.addRoom(room);

        } else
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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

    public String toJSON(RoomList roomList) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(roomList);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OutputStream os = resp.getOutputStream();
        try {
            String json = toJSON(roomList);
            os.write(json.getBytes(StandardCharsets.UTF_8));

        } finally {
            os.close();
        }


    }
}
