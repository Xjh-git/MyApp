package com.example.fuchaung.utils;


import com.example.fuchaung.bean.QA;
import com.example.fuchaung.bean.QAS;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class QA_Test {

    public List<QA> readExcel(String filename) {
        List<QA> qaList = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            Workbook wb = null;
            if (filename.toLowerCase().endsWith("xls")) {
                wb = new HSSFWorkbook(fileInputStream);
            }
            int numberofsheet = wb.getNumberOfSheets();
            for (int i = 0; i < numberofsheet; ++i) {
                Sheet sheet = wb.getSheetAt(i);
                Iterator<Row> rowIterator = sheet.rowIterator();
                while (rowIterator.hasNext()) {
                    String PossibleQ = "";
                    String StandardQ = "";
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case STRING:
                                if (PossibleQ.equalsIgnoreCase("")) {
                                    PossibleQ = cell.getStringCellValue().trim();
                                } else if (StandardQ.equalsIgnoreCase("")) {
                                    StandardQ = cell.getStringCellValue().trim();
                                } else {
                                    System.out.println("Random data::" + cell.getStringCellValue());
                                }
                                break;
                            case NUMERIC:
                                //                                if (bk_price == 0) {
                                //                                    bk_price = cell.getNumericCellValue();
                                //                                } else {
                                //                                    //System.out.println("Random data::"+cell.getNumericCellValue());
                                //                                }
                        }
                    }
                    if (!"PossibleQ".equals(PossibleQ)) {
                        QA qa = new QA(PossibleQ, StandardQ);
                        qaList.add(qa);
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qaList;
    }

    public List<QA> queryList(String question) {
        List<QA> res = new ArrayList<>();
        List<QA> standard = readExcel("/storage/emulated/0/tencent/QQfile_recv/train.xls");
        QA standardQuestion;
        double sim = 0.0;
        List<QAS> list = new ArrayList<>();

        for (int i = 0; i < standard.size(); i++) {
            for (int j = standard.size() - 1; j > i; j--) {
                if (standard.get(i).getStandardQ().equals(standard.get(j).getStandardQ())) {
                    standard.remove(j);
                }
            }
        }

        for (int i = 0; i < standard.size(); i++) {
            standardQuestion = standard.get(i);
            sim = SimFeatureUtil.sim(question, standardQuestion.getStandardQ());
            QAS qas = new QAS(standardQuestion, sim);
            list.add(qas);
        }
        Collections.sort(list);
        for (int i = 0; i < 5; i++) {
            res.add(list.get(i).getQa());
        }

        return res;
    }

}
