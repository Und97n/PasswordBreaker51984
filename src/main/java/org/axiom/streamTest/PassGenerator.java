package org.axiom.streamTest;

import java.util.*;
import java.util.function.Supplier;

public class PassGenerator implements Supplier<String> {
    private final char[] alphabet;
    private String previousString;


    public PassGenerator(String alphabet) {
        this.alphabet = alphabet.toCharArray();
        this.previousString = "" + this.alphabet[0] + this.alphabet[0];
    }


    public PassGenerator(String alphabet, int minLen) {
        this.alphabet = alphabet.toCharArray();
        StringBuilder temp = new StringBuilder();
        for(int i=0 ; i<minLen ; ++i)
            temp.append(alphabet.charAt(0));

        this.previousString = temp.toString();
    }


    @Override
    public String get() {
        String temp = previousString;
        this.previousString = nextString(previousString);
        return temp;
    }


    @Deprecated
    public List<String> getTestList(int maxLen) {
        List<String> list = new LinkedList<>();

        //  get() doesn't process empty-string
        list.add("");

        for (String s = "" + alphabet[0] ; s.length() <= maxLen ; s = nextString(s))
            list.add(s);

        return list;
    }


    private String nextString(String previous) {
        Pair<String, Boolean> temp = nextString(previous.toCharArray());
        return temp.getValue() ? alphabet[0] + temp.getKey() : temp.getKey();
    }


    //  Increments the string by one
    private Pair<String, Boolean> nextString(char[] arr) {
        if(arr.length == 1)
            return new Pair<>("" + nextAlphabetLetter(arr[0]), arr[0] == alphabet[alphabet.length - 1]);

        char currChar = arr[0];

        Pair<String, Boolean> temp = nextString(Arrays.copyOfRange(arr, 1, arr.length));
        if(temp.getValue()) {
            if(nextAlphabetLetter(currChar) == alphabet[0]) //  overlap goes up
                return new Pair<>(alphabet[0] + temp.getKey(), true);
            else    //overlap is consumed
                return new Pair<>(nextAlphabetLetter(currChar) + temp.getKey(), false);
        } else
            return new Pair<>(currChar + temp.getKey(), false);
    }


    private char nextAlphabetLetter(char letter) {
        int i;
        for (i = 0 ; alphabet[i] != letter ; ++i);

        return i != alphabet.length-1 ? alphabet[i+1] : alphabet[0];
    }
}
