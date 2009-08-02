/*
 * VocabList.java
 * Created on January 7, 2007, 9:03 PM
 * This file is part of VocabQuiz.
 *
 * Copyright © 2007-2009, Neil Isaac. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.*;
import java.util.*;

public class VocabList {
    
    private final String[] listCodes = { "VQT1.1TR", "VQT1.1VT", "VQT1.1VC", 
            "VQT1.1VJ", "VQT1.1NC", "VQT1.1ND", "VQT1.1NG" };
    private final String[] listTypes = { "Translation", "Verb Tense", 
            "Verb Tense Conversion", "Verb Conjugation", "Noun Case", 
            "Noun Declension", "Noun Gender" };
    
    protected int format;
    protected String set = null;
    protected String word = null;
    protected String trans = null;
    protected int entries = 0;
    protected int options = 0;
    
    private Vector<String> words = new Vector<String>();
    private Vector<String> translations = new Vector<String>();
    private boolean[] used;
    
    /** load file */
    public VocabList(String fileName) {
        if (!readFile(fileName)) {
            return;
        }

        used = new boolean[entries];
        for (int i = 0; i < entries; i++) used[i] = false;
        options = getNmbrOpts();
    }
    
    /** create new list */
    public VocabList() {
        used = new boolean[entries];
        for (int i = 0; i < entries; i++) used[i] = false;
        options = getNmbrOpts();
    }
    
    private boolean readFile(String fileName) {
        BufferedReader inFile = null;
        
        try {
            inFile = new BufferedReader(new FileReader(fileName));
            
            format = getNumericalCode(inFile.readLine());
            set = inFile.readLine();
            word = inFile.readLine();
            trans = inFile.readLine();
            entries = Integer.parseInt(inFile.readLine());
            
            for (int i = 0 ; i < entries ; i++) {
                words.addElement(inFile.readLine());
                translations.addElement(inFile.readLine());
            }
            
            inFile.close();
        }
        
        catch (IOException ex) {
            System.out.println("Error reading file \'" + fileName + "\'.");
            if (inFile != null){
                try {
                    inFile.close();
                } catch (IOException e) {
                    // ignore
                }

                return false;
            }
        }

        return true;
    }
    
    protected void saveFile(String fileName) {
        BufferedWriter outFile = null;
        
        try {
            outFile = new BufferedWriter(
                    new FileWriter(fileName));
            
            outFile.write(this.getCode() + "\n" + set + "\n" + word + "\n" +
                    trans + "\n" + entries + "\n");
            
            for (int i = 0 ; i < entries ; i++) {
                outFile.write(words.elementAt(i) + "\n");
                outFile.write(translations.elementAt(i) + "\n");
            }
            
            outFile.flush();
            outFile.close();
        }
        
        catch (IOException ex) {
            System.out.println("Error writing file \'" + fileName + "\'.");
            if (outFile != null){
                try {
                    outFile.flush();
                    outFile.close();
                } catch (IOException e) {
                    
                }
            }
        }
    }
    
    private int getUnused() {
        boolean valid = false;
        for (int i = 0; i < entries; i++) {
            if (!used[i]) {
                valid = true;
                break;
            }
        }
        if (!valid) return -1;
        
        while(true) {
            int r = (int) Math.abs(Math.random() * entries);
            if (!used[r]) return r;
        }
    }
    
    protected void addEntry(String w, String t){
        words.addElement(w);
        translations.addElement(t);
        
        entries += 1;
    }
    
    protected void editEntry(int i, String w, String t){
        words.setElementAt(w, i);
        translations.setElementAt(t, i);
    }
    
    protected void deleteEntry(int i){
        words.removeElementAt(i);
        translations.removeElementAt(i);
        
        if (entries > 0) entries -= 1;
    }
    
    protected String getTranslation(int i){
        if (i >= translations.size() || i < 0) return null;
        else return (String) translations.elementAt(i);
    }
    
    protected String getWord(int i){
        if (i > entries) return null;
        else return (String) words.elementAt(i);
    }
    
    protected String getCombinedForm(int i){
        if (i > entries) return null;
        else return (String) words.elementAt(i) + " - " +
                (String) translations.elementAt(i);
    }
    
    protected Question getQuestion() {
        int q = getUnused();
        if (q == -1) return null;
        
        Question question = new Question(q, this);
        used[q] = true;
        
        return question;
    }
    
    protected String getCode() {
        return listCodes[this.format];
    }
    
    private int getNumericalCode(String c) {
        int code = 0;
        
        for (int i = 0; i < listCodes.length; i++) {
            if (c.equals(listCodes[i])) code = i;
        }
        
        return code;
    }
    
    protected String getLiteralCode() {
        return listTypes[this.format];
    }
    
    private int getNmbrOpts() {
        Vector<String> ok = new Vector<String>();
        
        for (int i = 0; i < translations.size(); i++) {
            if (ok.indexOf(translations.elementAt(i)) == -1) {
                ok.addElement(translations.elementAt(i));
            }
        }
        
        return ok.size();
    }
}
