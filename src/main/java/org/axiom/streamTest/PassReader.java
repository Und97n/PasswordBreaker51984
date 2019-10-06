package org.axiom.streamTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PassReader implements Supplier<String> {
    private final Path path;
    private List<String> cache;

    private BufferedReader bufferedReader;


    public PassReader(Path path) throws IOException {
        this.path = path;
        cache = new ArrayList<>(20);

        InputStream inputStream = Files.newInputStream(path);
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        updateCache();

    }


    @Override
    public String get() {
        if(cache.size() == 0) {
            try {
                updateCache();
            }
            catch (IOException ioe) {return "0000000000000000000000000000000000000000000"; }
            return cache.remove(0);
        }
        else
            return cache.remove(0);

    }


    private void updateCache() throws IOException {
        String line;
        for(int i=0 ; i<20 ; ++i) {
            line = bufferedReader.readLine();
            if (line == null)
                throw new IOException();
            cache.add(line);
        }
    }
}
