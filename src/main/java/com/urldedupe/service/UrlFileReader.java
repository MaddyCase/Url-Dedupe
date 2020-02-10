package com.urldedupe.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UrlFileReader {

    private static final Integer SET_CAPACITY = 100;
    private static final String FILE_BASE = "./urlFiles/";

    public Set<String> getUrls(@NonNull final String fileName) throws IOException {
        Set<String> setOfUrls = new HashSet<>(SET_CAPACITY);
        try (BufferedReader br = new BufferedReader(new FileReader( FILE_BASE + fileName))) {
            setOfUrls.add(br.readLine().trim());
            String line;
            while ((line = br.readLine()) != null) {
                setOfUrls.add(StringUtils.trimWhitespace(line));
            }
        }
        return setOfUrls;
    }
}
