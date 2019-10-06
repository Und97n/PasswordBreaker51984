package org.axiom.streamTest;

import java.io.IOException;

public class Main {
    private static String alphabet = "1234";
    private static int maxPasslLen = 6;
    private static String username = "users";
    private static String url = "http://localhost/A/";
    private static int port = 80;

    public static void main(String[] args) throws IOException {
        argsPreprocessor(args);

        new Connector(alphabet, "localhost", username, url, port).getPass(maxPasslLen, "123*");

    }


    private static void argsPreprocessor(String[] args) {
        //  todo
    }
}
