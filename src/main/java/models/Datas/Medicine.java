package models.Datas;

import models.Filing.FileIO;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Medicine {
    private String medicineID;
    private String name;
    private String category;
    private String usage;

    public Medicine(String medicineID,String name,String category, String usage){
        this.medicineID = medicineID;
        this.name = name;
        this.category = category;
        this.usage = usage;
    }
    public void setMedicineID(String medID) {
        this.medicineID = medicineID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setUsage(String usage) {
        this.usage = usage;
    }
    public String getMedicineID(){return medicineID;}
    public String getName(){return name;}
    public String getCategory(){return category;}
    public String getUsage(){return usage;}


    @Override
    public String toString() {
        return MessageFormat.format("{0}, {1}, {2}, {3}", medicineID, name, category,usage);
    }

    public void addToMedicineFile() {
        FileIO appender = new FileIO("a", "medicine");
        appender.appendFile(toString() + "\n");
        System.out.println("New Medicine Added");
    }
}
