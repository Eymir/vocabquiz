/*
 * Question.java
 * Created on January 9, 2007, 7:22 PM
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

public class Question {
    
    protected String[] options = new String[4];
    protected String word;
    private int correctInteger;
    private String correctString;
    
    /** Creates a new instance of Question */
    public Question(int index, VocabList list) {
        word = list.getWord(index);
        
        correctString = list.getTranslation(index);
        int nmbrOpts = list.options > 4 ? 4 : list.options;
        correctInteger = (int) Math.abs(Math.random() * nmbrOpts);
        
        for (int i = 0; i < 4; i++) {
            options[i] = null;
        }
        
        for (int i = 0; i < nmbrOpts; i++) {
            if (i == correctInteger) {
                options[correctInteger] = correctString;
                continue;
            }
            
            while (true) {
                int temp = (int) Math.floor(Math.random() * list.entries);
                String trans = list.getTranslation(temp);
                
                boolean ok = true;
                
                if (trans.equals(correctString)) ok = false;
                
                for (int j = 0; j < i; j++) {
                    if (trans.equals(options[j])) ok = false;
                }
                
                if (ok) {
                    options[i] = list.getTranslation(temp);
                    break;
                }
            }
        }
    }
    
    protected boolean check(int input) {
        if (input == correctInteger) return true;
        else return false;
    }
    
    protected boolean check(String input) {
        if (input.equals(correctString)) return true;
        else return false;
    }
}
