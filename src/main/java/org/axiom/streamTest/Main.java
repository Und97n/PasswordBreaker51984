package org.axiom.streamTest;

import java.io.IOException;

public class Main {
    private static String alphabet = "1234";
    private static String username = "user";
    //private static String url = "127.0.0.1";
    private static int port = 80;

    public static void main(String[] args) throws IOException {
        argsPreprocessor(args);

        new Connector().getPass(alphabet, "http://localhost/", username, "http://localhost/A/", port, 5);

    }


    private static void argsPreprocessor(String[] args) {
        //  todo
    }
}
