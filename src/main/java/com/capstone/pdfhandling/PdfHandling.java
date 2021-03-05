/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.pdfhandling;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;

/**
 *
 * @author vuvie
 */
public class PdfHandling {

    private String fileName = "demo.pdf";
    private PDDocument document;

    public void sanitize(String fileName) {

        try {
            document = PDDocument.load(new File(fileName));
            PDPageTree pages = document.getPages();
            //remove open actions
            for (PDPage page : pages) {
                page.setActions(null);
            }
            //generate new document
            PDDocument doc2 = new PDDocument();
            for (PDPage page : pages) {
                doc2.addPage(page);
            }
            doc2.save("output/nojs.pdf");
            doc2.close();
            document.close();
            
//            for (COSObject base : bases) {
//                System.out.println("===================");
//                crawl(base);
//            }

        } catch (IOException ex) {
            Logger.getLogger(PdfHandling.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
