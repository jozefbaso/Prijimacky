package com.jozefbaso.prijimackykody;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

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
        }
    }


//        // Finds the workbook instance for XLSX file
//        XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
//
//        // Return first sheet from the XLSX workbook
//        XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//
//        // Process all the rows in current sheet
//        for (Row row : mySheet) {
//            Student currentStudent = new Student(row.getCell(0).toString(), row.getCell(1).toString(), row.getCell(2).toString());
//            listOfStudents.put(row.getCell(2).toString(), currentStudent);
//            System.out.println("Added student " + currentStudent.getFirstName() + " " + currentStudent.getLastName() + " with code: " + currentStudent.getCode());
//        }


//    public static void writeExcel(Map<String, Student> listOfStudents, String outputFile) throws IOException {
//        Workbook wb = new XSSFWorkbook();
//        CreationHelper createHelper = wb.getCreationHelper();
//        Sheet sheet = wb.createSheet("Sheet 1");
//
//        Row firstRow = sheet.createRow(0);
//        firstRow.createCell(0).setCellValue("Meno");
//        firstRow.createCell(1).setCellValue("Priezvisko");
//        firstRow.createCell(2).setCellValue("SJL");
//        firstRow.createCell(3).setCellValue("MAT");
//
//        Set<String> newRows = listOfStudents.keySet();
//        int rowNum = sheet.getLastRowNum();
//
//        for (String code : newRows) {
//            Row row = sheet.createRow(++rowNum);
//            row.createCell(0).setCellValue(listOfStudents.get(code).getFirstName());
//            row.createCell(1).setCellValue(listOfStudents.get(code).getLastName());
//            row.createCell(2).setCellValue(listOfStudents.get(code).getCode());
//            row.createCell(3).setCellValue(listOfStudents.get(code).getPointsSjl());
//            row.createCell(4).setCellValue(listOfStudents.get(code).getPointsMat());
//        }
//
//        // open an OutputStream to save written data into Excel file
//        // Write the output to a file
//        try (OutputStream fileOut = new FileOutputStream(outputFile)) {
//            wb.write(fileOut);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        // Close workbook
//        wb.close();
//    }

}
