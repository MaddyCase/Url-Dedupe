package com.urldedupe.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class UrlFileReader {

    private static final Integer SET_CAPACITY = 100000;
    private static final String BASE_FILE_DIR = "./urlFiles/";

    /**
     * Retrieves all files in the particular directory name. Note this this directory must live within urlFiles.
     * If the file directory is not found, we return an empty list
     *
     * @param dirname
     * @return
     */
    public List<File> getFiles(@NonNull final String dirname) {
        File dir = new File(BASE_FILE_DIR + dirname);
        File[] files = dir.listFiles();
        return files != null ?
               Arrays.asList(files) :
               Collections.emptyList();
    }

    public Set<String> getUrls(@NonNull final File file) throws IOException {
        Set<String> setOfUrls = new HashSet<>(SET_CAPACITY); // Note that we are assuming that clients have always sent over the correct capacity that we required
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                setOfUrls.add(line.trim());
            }
        }
        return setOfUrls;
    }
}
