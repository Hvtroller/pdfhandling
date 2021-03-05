/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.capstone.pdfhandling;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vuvie
 */
public class Main {

    public static void main(String[] args) {
        PdfHandling handler = new PdfHandling();
        handler.sanitize("input/bigjs.pdf");
    }
}
