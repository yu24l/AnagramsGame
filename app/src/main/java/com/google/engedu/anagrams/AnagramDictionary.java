/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final String TAG = "LogActivity";

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private ArrayList<String> wordList;

    private int wordLength;

    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWords;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        wordLength = DEFAULT_WORD_LENGTH;
        wordList = new ArrayList<String>();
        wordSet = new HashSet<String>();
        lettersToWord = new HashMap<String, ArrayList<String>>();
        sizeToWords = new HashMap<Integer, ArrayList<String>>();
        while((line = in.readLine()) != null){
            String word = line.trim();
            wordList.add(word);

            int length = word.length();
            if(sizeToWords.containsKey(length))
                sizeToWords.get(length).add(word);
            else {
                ArrayList<String> newList = new ArrayList<String>();
                newList.add(word);
                sizeToWords.put(length, newList);
            }

            // if not contain the key, add the key, then put the value in the map
            String sorted = sortLetters(word);
            if (!lettersToWord.containsKey(sorted)) // if key is present
                lettersToWord.put(sorted, new ArrayList<String>());
            lettersToWord.get(sorted).add(word);

//            if(wordSet.add(word)) {
//                String sorted = sortLetters(word);
//                if (lettersToWord.containsKey(sorted)) // if key is present
//                    lettersToWord.get(sorted).add(word);
//                else { // otherwise, add key
//                    ArrayList<String> newList = new ArrayList<String>();
//                    newList.add(word);
//                    lettersToWord.put(sorted, newList);
//                }
//            }
        }
        Log.i(TAG, "wordlist size: " + wordList.size());
        Log.i(TAG, "word (stop): " + lettersToWord.get(sortLetters("stop")));
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base)){
            return true;
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        String key = sortLetters(targetWord);
        ArrayList<String> result = lettersToWord.get(key);
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String[] alphabetList = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
                "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        for(int i = 0; i < alphabetList.length; i++){
            String tempWord = word + alphabetList[i];
            tempWord = sortLetters(tempWord);
            if(lettersToWord.containsKey(tempWord) && lettersToWord.get(tempWord) != null)
                result.addAll(lettersToWord.get(tempWord));
        }
        return result;
    }

    public String pickGoodStarterWord() {
        while(!sizeToWords.containsKey(wordLength) && wordLength < MAX_WORD_LENGTH)
            wordLength++;
        ArrayList<String> options = sizeToWords.get(wordLength);

        String result = "skate";
        int count = 0;
        int index = (int)(Math.random()*options.size());
        for(int i = index; i < options.size(); i++) {
            count++;
            String key = sortLetters(options.get(i));
            if(lettersToWord.get(key).size() >= MIN_NUM_ANAGRAMS) {
                result = options.get(i);
                break;
            }
            if(count >= lettersToWord.size()) {
                result = options.get(index);
                break;
            }
            if(i == options.size()-1)
                i = 0;
        }
        if(wordLength < MAX_WORD_LENGTH)
            wordLength++;
        return result;
    }

    private String sortLetters(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
//        return (Arrays.toString(chars));
        return new String(chars);
    }
}
