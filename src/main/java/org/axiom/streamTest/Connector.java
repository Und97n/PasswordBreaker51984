package org.axiom.streamTest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.function.Supplier;

public class Connector {
    /**
     * @return  password, that is suitable for the given server
     */
    public String getPass(String alphabet, String host, String username, String fullURL, int port, int maxPassLen) throws IOException {
        Supplier<String> passwordSupplier = new PassGenerator(alphabet);

        DefaultHttpClient httpClient = new DefaultHttpClient();

        for(String password = passwordSupplier.get() ; password.length() <= maxPassLen ; password = passwordSupplier.get()) {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(host, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpGet httpGet = new HttpGet(fullURL);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
//            System.out.println(httpClient.);

            if(response.getStatusLine().getStatusCode() == 200) {
                System.out.println("Password found!");
                System.out.println("Pass: " + password);
                break;
            } else {
//                System.out.println(password + " " + response.getStatusLine().getStatusCode());
            }

            entity.consumeContent();
        }

        System.out.println("DONE");
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown();

        return null;

    }

}
