package com.urldedupe.service;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UrlFileReaderTest {

    private UrlFileReader service;

    private static final String TEST_FILE_DIR = "./src/test/java/testFiles";

    @Before
    public void setUp() {
        service = new UrlFileReader();
    }

    @Test
    public void getUrls() throws IOException {
        List<File> files = createTestFiles();
        assertEquals(3, files.size());
        Set<String> urlsFromFileOne = service.getUrls(files.get(0));
        Set<String> urlsFromFileTwo = service.getUrls(files.get(1));
        Set<String> urlsFromFileThree = service.getUrls(files.get(2));

        assertEquals(getExpectedSetForTestOneFile(files.get(0)), urlsFromFileOne);
        assertEquals(getExpectedSetForTestOneFile(files.get(1)), urlsFromFileTwo);
        assertEquals(getExpectedSetForTestOneFile(files.get(2)), urlsFromFileThree);
    }

    private Set<String> getExpectedSetForTestOneFile(File file) {
        Set<String> set = new HashSet<>();
        if ("TestOne".equals(file.getName())) {
            set.add("http://www.somekindatesturl.com");
            set.add("http://www.somekindafella.com");
            set.add("https://www.somekindatesturl.com");
            set.add("http://www.happydoggo.com");
        } else if ("TestTwo".equals(file.getName())) {
            set.add("http://www.happydoggo.com");
        }
        return set;
    }

    private List<File> createTestFiles() {
        File dir = new File(TEST_FILE_DIR);
        File[] files = dir.listFiles();
        return files != null ?
               Arrays.asList(files) :
               Collections.emptyList();
    }
}