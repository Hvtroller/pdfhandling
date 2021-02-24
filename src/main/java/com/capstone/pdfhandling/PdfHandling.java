/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.pdfhandling;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.AbstractDocument;
import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.cos.COSBoolean;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSNull;
import org.apache.pdfbox.cos.COSNumber;
import org.apache.pdfbox.cos.COSObject;
import org.apache.pdfbox.cos.COSObjectKey;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.cos.ICOSVisitor;
import org.apache.pdfbox.io.ScratchFile;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDStream;

/**
 *
 * @author vuvie
 */
public class PdfHandling {

    private String fileName = "demo.pdf";
    private PDDocument document;

    public void printText(String fileName) {

        try {
            document = PDDocument.load(new File(fileName));
            COSDocument doc = document.getDocument();
            List<COSObject> bases = doc.getObjects();

            for (COSObject base : bases) {
                if (crawl(base)) {
                    COSObjectKey key = new COSObjectKey(base);
                    doc.removeObject(key);
                    Map<COSObjectKey, Long> xrefTable = doc.getXrefTable();
                    xrefTable.remove(key);
                    doc.addXRefTable(xrefTable);
                }
            }

            bases = doc.getObjects();
            System.out.println("====================");
            doc.print();
            
            document.close();
            doc.close();
//            for (COSObject base : bases) {
//                System.out.println("===================");
//                crawl(base);
//            }

        } catch (IOException ex) {
            Logger.getLogger(PdfHandling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private boolean crawl(COSBase base) {
        if (base == null) {
            return false;
        }
        if (base instanceof COSName
                || base instanceof COSString
                || base instanceof COSStream
                || base instanceof COSNull
                || base instanceof COSNumber
                || base instanceof COSBoolean) {
            return false;
        }

        if (base instanceof COSObject) {
            base = ((COSObject) base).getObject();
        }

        if (base instanceof COSDictionary) {
            COSDictionary dict = (COSDictionary) base;
            Set<Map.Entry<COSName, COSBase>> entrySet = dict.entrySet();
            for (Map.Entry<COSName, COSBase> entry : entrySet) {
                if ("JS".equals(entry.getKey().getName())
                        || "JavaScript".equals(entry.getKey().getName())) {
                    System.out.println("remove JS in /JS");
                    return true;
                }

                if ("S".equals(entry.getKey().getName())) {
                    if (entry.getValue() instanceof COSName) {
                        if ("JavaScript".equals(((COSName) entry.getValue()).getName())) {
                            System.out.println("remove JS in /S");
                            return true;
                        }
                    }
                }

                if ("AA".equals(entry.getKey().getName())) {
                    System.out.println("remove JS in /AA");
                    entrySet.remove(entry);
                    return true;
                }
                System.out.println(entry.getKey().getName());
                if (entry.getKey().getName().equalsIgnoreCase("kids")) {
                    return false;
                }
                crawl(entry.getValue());
            }
//            while (it.hasNext()) {
//                Map.Entry<COSName, COSBase> entry = it.next();
//                if ("JS".equals(entry.getKey().getName())
//                        || "JavaScript".equals(entry.getKey().getName())) {
//                    System.out.println("remove JS in /JS");
//                    it.remove();
//                    continue;
//                }
//
//                if ("S".equals(entry.getKey().getName())) {
//                    if (entry.getValue() instanceof COSName) {
//                        if ("JavaScript".equals(((COSName) entry.getValue()).getName())) {
//                            System.out.println("remove JS in /S");
//                            it.remove();
//                            continue;
//                        }
//                    }
//                }
//
//                if ("AA".equals(entry.getKey().getName())) {
//                    System.out.println("remove JS in /AA");
//                    it.remove();
//                    continue;
//                }
//                System.out.println(entry.getKey().getName());
//                if(entry.getKey().getName().equalsIgnoreCase("kids")){
//                    return;
//                }
//                crawl(entry.getValue());
//            }
            return false;
        } else if (base instanceof COSArray) {
            COSArray ar = (COSArray) base;

            for (COSBase item : ar) {
                if(crawl(item)){
                    return true;
                }
            }
        } else {
            System.out.println("Unknown COS type: " + base);
            return false;
        }
        return false;
    }
}
