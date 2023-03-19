import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        //add stuff to array list
        // YOUR CODE HERE — Call your recursive method!
        wordGen("", letters);
    }

    public void wordGen(String first, String rest) {
        //generates so many dupes, can't find out why - either find dupes doesnt work or method is wrong
        if (rest.length() == 0) {
            words.add(first);
        }
        else {
            for (int i = 0; i < rest.length(); i++) {
                //does the thing in the hint where it separates the "first" part of the word and the rest and recurses
                wordGen(first + rest.charAt(i), rest.substring(0, i) + rest.substring(i + 1));
            }
            //more recursion!!
            wordGen(first, rest.substring(1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        //calls recursive method
        mergeSort(words);
    }

    public void mergeSort(ArrayList<String> list) {
        int wholeind = 0, leftind = 0, rightind = 0;
        ArrayList<String> left;
        ArrayList<String> right;
        if (list.size() == 1) {
            return;
        }
        else {
            int pointer = list.size() / 2;
            //everything left of pointer gets added
            left = new ArrayList<String>();
            for (int i = 0; i < pointer; i++) {
                left.add(list.get(i));
            }
            //everything right of pointer gets added
            right = new ArrayList<String>();
            for (int j = pointer; j < list.size(); j++) {
                right.add(list.get(j));
            }
            //recursion
            mergeSort(left);
            mergeSort(right);

            //compare strings
            while(leftind < left.size() && rightind < right.size()) {
                if (left.get(leftind).compareTo(right.get(rightind)) < 0) {
                    //set list left half element
                    //have to increment all of these in statement or else error because unreachable statement
                    list.set(wholeind++, left.get(leftind++));
                }
                else {
                    list.set(wholeind++, right.get(rightind++));
                }
                while(leftind < left.size()) {
                    list.set(wholeind++, left.get(leftind++));
                }
                while(rightind < right.size()) {
                    list.set(wholeind++, right.get(rightind++));
                }
            }
        }
    }


    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        int i = 0;
        while (i < words.size()) {
            String word = words.get(i);
            if (!found(word)) {
                words.remove(i);
            }
            else {
                i++;
            }
        }
    }

    public boolean found(String s) {
        //found out arrays.binarysearch through google because original method was much more complicated and this is
        //one line statement
        int index = Arrays.binarySearch(DICTIONARY, s);
        return index >= 0;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
