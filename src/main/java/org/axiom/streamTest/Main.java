package org.axiom.streamTest;

import java.io.IOException;

public class Main {
    private static String alphabet = "1234";
    private static int maxPasslLen = 6;
    private static String username = "user";
    private static String url = "http://10.240.226.4/A/";
    private static int port = 80;

    public static void main(String[] args) throws IOException {
        argsPreprocessor(args);

        long startTime = System.currentTimeMillis();
        //  new Connector(alphabet, "10.240.226.4", username, url, port).getPass(6);
        System.out.println("Time spent: " + (System.currentTimeMillis() - startTime) / 1000);



        startTime = System.currentTimeMillis();
        new Connector(alphabet, "10.240.226.4", username, url, port).getPass("**34");
        System.out.println("Time spent: " + (System.currentTimeMillis() - startTime) / 1000);


        startTime = System.currentTimeMillis();
        new Connector(alphabet, "10.240.226.4", username, url, port).getPass("/home/axiom/Desktop/Workspace/Java/passBreaker/PasswordBreaker51984/src/main/java/org/axiom/streamTest/500-worst-passwords.txt");
        System.out.println("Time spent: " + (System.currentTimeMillis() - startTime) / 1000);

    }


    private static void argsPreprocessor(String[] args) {
        //  todo
    }
}
