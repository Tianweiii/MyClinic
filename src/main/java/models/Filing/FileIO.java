package models.Filing;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileIO {
    BufferedReader reader;
    BufferedWriter appender;
    BufferedWriter writer;

    public FileIO(String type, String filename) {
        if (type.equalsIgnoreCase("a")) {
            try {
                appender = new BufferedWriter(new FileWriter("src/main/java/models/TextFiles/" + filename, true));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (type.equalsIgnoreCase("r")) {
            try {
                reader = new BufferedReader(new FileReader("src/main/java/models/TextFiles/" + filename));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        } else if (type.equalsIgnoreCase("w")) {
            try {
                writer = new BufferedWriter(new FileWriter("src/main/java/models/TextFiles/" + filename));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public List<String> readFile() throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            reader.close();
        }
    }

    public int countRowNum() throws IOException {
        String line;
        int rowCount = 0;
        while ((line = reader.readLine()) != null) {
            rowCount++;
        }
        return rowCount;
    }

    public void appendFile(String data) {
        try {
            appender.write(data);
            appender.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeFile(ArrayList<String> data) {
        try {
            for (String row : data) {
                writer.write(row + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] splitString(String data) {
        return data.split(",\\s");
    }
}