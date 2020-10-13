package com.jozefbaso.prijimackykody;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.google.android.gms.common.util.JsonUtils;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReadWriteCsv {

    public static String FillPointsIfMissingInFile(String[] data, int index) {
        if (data != null && index >= 0 && index < data.length) return data[index];
        else return "-1";
    }

    public static void readCsv(Map<String, Student> listOfStudents, Uri inputFile, Context context) throws IOException {
        try (InputStream inputStream = context.getContentResolver().openInputStream(inputFile);
             BufferedReader csvReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8))) {
            //System.out.println("------------file open------------");
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                Student s = new Student(data[0], data[1], data[2], FillPointsIfMissingInFile(data, 3), FillPointsIfMissingInFile(data, 4));
                //System.out.println("reading: " + s.getFirstName() + " " + s.getLastName());
                listOfStudents.put(data[2], s);
                //System.out.println("saved to hashMap: " + listOfStudents.get(data[2]));
            }
            inputStream.close();
        }
    }

    public static void writeCsv(Map<String, Student> listOfStudents, Uri outputFile, Context context) throws IOException {
        String fileName = "documents/list of students.csv";
        String dirName =  android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        File myFile = new File(dirName, fileName);

        try {
            FileWriter fileWriter = new FileWriter(myFile);
            fileWriter.append("meno,priezvisko,kód,SJL,MAT" + "\n");
            Set<String> newRows = listOfStudents.keySet();
            for (String barcode : newRows) {
                fileWriter.write(Objects.requireNonNull(listOfStudents.get(barcode)).toString() + "\n");
            }
            fileWriter.flush();
            fileWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
