package com.urldedupe.controller;

import com.urldedupe.domain.service.UrlService;
import com.urldedupe.service.UrlFileReader;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UrlDedupeControllerTest {

    UrlDedupeController controller;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UrlService mockUrlService;

    @Mock
    private UrlFileReader mockUrlFileReader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new UrlDedupeController(mockUrlService, mockUrlFileReader);
    }

    @Nested
    class Create {
        private static final String DIR_NAME = "testUrls";
        private static final String ENDPOINT = "/urldedupe/" + DIR_NAME;
        private static final String TEST_FILE_DIR = "./src/test/java/testFiles";

        @AfterEach
        void tearDown() {
            Mockito.clearInvocations(mockUrlService, mockUrlFileReader);
        }


        @Test
        void success() throws Exception {
            List<File> testFiles = createTestFiles();
            Set<String> testOneSetValues = Sets.newHashSet();
            testOneSetValues.add("testOneSetUrl");
            Set<String> testTwoSetValues = Sets.newHashSet();
            testTwoSetValues.add("testTwoSetUrl");
            Set<String> testThreeSetValues = Sets.newHashSet();

            for (File file : testFiles) {
                if ("TestOne".equals(file.getName())) {
                    doReturn(testOneSetValues).when(mockUrlFileReader).getUrls(file);
                } else if ("TestTwo".equals(file.getName())) {
                    doReturn(testTwoSetValues).when(mockUrlFileReader).getUrls(file);
                } else if ("TestThree".equals(file.getName())) {
                    doReturn(testThreeSetValues).when(mockUrlFileReader).getUrls(file);
                }
            }

            doReturn(testFiles).when(mockUrlFileReader.getFiles(DIR_NAME));
            mockMvc.perform(get(ENDPOINT)).andExpect(status().isOk());

            verify(mockUrlService, times(3)).saveAll(any());
            verify(mockUrlService, times(1)).saveAll(testOneSetValues);
            verify(mockUrlService, times(1)).saveAll(testTwoSetValues);
            verify(mockUrlService, times(1)).saveAll(testThreeSetValues);
        }

        private List<File> createTestFiles() {
            File dir = new File(TEST_FILE_DIR);
            File[] files = dir.listFiles();
            return files != null ?
                   Arrays.asList(files) :
                   Collections.emptyList();
        }
    }
}