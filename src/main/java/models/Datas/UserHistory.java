package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class UserHistory {
    private String role;
    private String roleCount;

    public UserHistory(String role, String roleCount) {
        this.role = role;
        this.roleCount = roleCount;
    }

    public static int getExistingCount(String role) throws IOException {
        int count = 0;
        FileIO reader = new FileIO("r", "userHistory");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(role)) {
                count += Integer.parseInt(arr[1]);
            }
        }
        return count;
    }

    public static String getNewId(String role) throws IOException {
        int id = 0;
        FileIO reader = new FileIO("r", "userHistory");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(role)) {
                id = Integer.parseInt(arr[1]) + 1;
            }
        }
        return String.valueOf(id);
    }

    public static void updateUserHistoryId(String role) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "userHistory");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(role)) {
                int id = Integer.parseInt(arr[1]) + 1;
                arr[1] = String.valueOf(id);
                row = MessageFormat.format("{0}, {1}", role, id);
            }
            data.add(row);
        }
        writeToUserHistoryFile(data);
    }

    public static void writeToUserHistoryFile(ArrayList<String> data) {
        FileIO writer = new FileIO("w", "userHistory");
        writer.writeFile(data);
    }
}
