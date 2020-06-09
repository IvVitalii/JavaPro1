package ua.kiev.prog;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            User user = logging();
            signedIn(user);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public static void signedIn(User user) throws IOException {
        System.out.println("---Enter \"exit\" to exit ---");
        System.out.println("---Enter \"status value\" to set status(\"busy\", \"sleep\", \"online\")---");
        System.out.println("---Enter \"login:text\" to private message ---");
        System.out.println("---Enter \"users\" to get users list ---");
        System.out.println("Enter your message: ");
        while (true) {
            String text = scanner.nextLine();
            if (text.equals(Message.EXIT)) {
                break;
            } else if (text.equals(Message.GET_USERS)) {
                user.printUsers();
            } else if (text.equals(Message.GET_ROOMS)) {
                Room.printRooms();
            } else if (text.length() > 5 && text.substring(0, 6).equals(Message.STATUS)) {
                user.setStatus(text);
            } else if (text.length() > 3 && text.substring(0, 4).equals(Message.ROOM)) {
                Room room = new Room(text.substring(5));
                int res = room.sendRoom();
                if (res == 210) {
                    System.out.println("room already exist");
                    continue;
                }
                System.out.println("room :" + room.getName() + " created");
                room.chatRoom(user);
            } else {
                Message m = new Message(user, text);
                m.msgBuild();
                if (m.isToSend()) {
                    int res = m.send(Utils.getURL() + "/add");
                    if (res != 200) { // 200 OK
                        System.out.println("HTTP error occured: " + res);
                        return;
                    }
                }
            }
        }
    }


    public static User logging() throws IOException {

        while (true) {
            System.out.println("Enter your login: ");
            String login = scanner.nextLine();
            System.out.println("Enter your password: ");
            String pass = scanner.nextLine();
            User user = new User(login, pass);
            int code = user.sendUser(Utils.getURL());
            if (code == 210) {
                continue;
            }
            Thread th = new Thread(new GetThread(user));
            th.setDaemon(true);
            th.start();

            return user;
        }
    }

}