package com.ivy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Serializer {
    public static void serialize(Object o) throws IllegalAccessException, IOException {
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuilder data = new StringBuilder();
        for (Field f : fields
        ) {
            if (f.isAnnotationPresent(Save.class)) {
                if (Modifier.isPrivate(f.getModifiers())) {
                    f.setAccessible(true);
                }
                Class<?> fieldtype = f.getType();
                if (fieldtype == int.class) {
                    data.append("i:" + f.getName() + "=" + f.get(o) + System.lineSeparator());
                }
                if (fieldtype == String.class) {
                    data.append("s:" + f.getName() + "=" + f.get(o) + System.lineSeparator());
                }
            }

        }
        saveToFile(data.toString());
    }

    private static void saveToFile(String data) throws IOException {
        if (!Files.exists(Paths.get("D:\\save.txt"))) {
            Files.createFile(Paths.get("D:\\save.txt"));
        }
        Files.write(Paths.get("D:\\save.txt"), data.getBytes(), StandardOpenOption.WRITE);


    }

    public static <T> T deserialize(String filePath, Class cls) throws IOException, NoSuchFieldException, IllegalAccessException, InstantiationException {
        Scanner sc = new Scanner(Paths.get(filePath));
        T obj = (T) cls.newInstance();
        while (sc.hasNextLine()) {
            String[] field = sc.nextLine().split("[:=]");
            if (field.length > 3) {
                throw new NoSuchFieldException();
            }
            String name = field[1];
            Field f = cls.getDeclaredField(name);
            f.setAccessible(true);
            if (field[0].equals("i")) {
                int value = Integer.valueOf(field[2]);
                f.set(obj, value);
            }
            if (field[0].equals("s")) {
                String value = field[2];
                f.set(obj, value);
            }

        }
        return obj;
    }


}
