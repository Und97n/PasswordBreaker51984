package org.axiom.streamTest;

import java.io.IOException;

public class Main {
    private static String alphabet = "1234";
    private static int maxPasslLen = 5;
    private static String username = "root";
    private static String url = "http://127.0.0.1";
    private static int port = 80;

    public static void main(String[] args) throws IOException {
        argsPreprocessor(args);

        //  new PassGenerator("12345").getTestList(5).stream().forEach(System.out::println);

        new Connector(alphabet, url, username, "localhost/protected", port).getPass(maxPasslLen, "123**t44");

    }


    private static void argsPreprocessor(String[] args) {
        //  todo
    }
}
