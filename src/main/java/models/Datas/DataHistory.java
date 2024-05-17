package models.Datas;

import models.Filing.FileIO;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;

public class DataHistory {
    private String role;
    private String roleCount;

    public DataHistory(String role, String roleCount) {
        this.role = role;
        this.roleCount = roleCount;
    }

    public static int getExistingCount(String target) throws IOException {
        int count = 0;
        FileIO reader = new FileIO("r", "dataHistory");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(target)) {
                count += Integer.parseInt(arr[1]);
            }
        }
        return count;
    }

    public static String getNewId(String target) throws IOException {
        int id = 0;
        FileIO reader = new FileIO("r", "dataHistory");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(target)) {
                id = Integer.parseInt(arr[1]) + 1;
            }
        }
        return String.valueOf(id);
    }

    public static void updateUserHistoryId(String target) throws IOException {
        ArrayList<String> data = new ArrayList<>();
        FileIO reader = new FileIO("r", "dataHistory");
        for (String row : reader.readFile()) {
            String[] arr = FileIO.splitString(row);
            if (arr[0].equals(target)) {
                int id = Integer.parseInt(arr[1]) + 1;
                arr[1] = String.valueOf(id);
                row = MessageFormat.format("{0}, {1}", target, id);
            }
            data.add(row);
        }
        writeToUserHistoryFile(data);
    }

    public static void writeToUserHistoryFile(ArrayList<String> data) {
        FileIO writer = new FileIO("w", "dataHistory");
        writer.writeFile(data);
    }
}
