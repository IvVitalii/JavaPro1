package com.ivy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

@SaveTo("D:\\save.txt")
public class TextContainer {
    private String text;

    public TextContainer(String text) {
        this.text = text;
    }

    @Saver
    public void save(String path, String text) throws IOException {

        try (PrintWriter pw = new PrintWriter(new File(path));
        ) {
            pw.print(text);
        }

    }
}
