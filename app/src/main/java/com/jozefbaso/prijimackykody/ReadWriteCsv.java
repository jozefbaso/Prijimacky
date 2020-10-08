package com.jozefbaso.prijimackykody;



import java.io.*;
import java.util.Map;
import java.util.Set;


//public class ExcelReadWrite {
//
////    public static void readExcel(Map<String, Student> listOfStudents, String inputFile) throws IOException {
////        File myFile = new File(inputFile);
////        FileInputStream fis = new FileInputStream(myFile);
////
////        BufferedReader csvReader = new BufferedReader(new FileReader(inputFile));
////        String row;
////        while ((row = csvReader.readLine()) != null) {
////            String[] data = row.split(",");
////            listOfStudents.put(data[2], new Student(data[0], data[1], data[2]));
////        }
////        csvReader.close();




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
//    }

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

//}
