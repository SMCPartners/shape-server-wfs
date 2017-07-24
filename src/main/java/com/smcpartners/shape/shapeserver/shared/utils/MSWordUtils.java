package com.smcpartners.shape.shapeserver.shared.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.FileInputStream;
import java.util.List;

/**
 * Responsibility: </br>
 * 1. </br>
 * 2. </br>
 * Created By: johndestefano
 * Date: 7/22/17
 */
public class MSWordUtils {
    public static void main(String[] args) throws Exception {
        XWPFDocument docx = new XWPFDocument(
                new FileInputStream("XXXX"));
        List<XWPFTable> tables = docx.getTables();

        System.out.println("Total no of paragraph "+tables.size());
        for (XWPFTable table : tables) {
            System.out.println(table.getNumberOfRows());
        }

        docx.close();
//
//        XWPFWordExtractor we = new XWPFWordExtractor(docx);
//        List<PackagePart> parts = we.getPackage().
//        parts.forEach(part -> {
//            System.out.println(part.getPartName());
//        });

    }
}
