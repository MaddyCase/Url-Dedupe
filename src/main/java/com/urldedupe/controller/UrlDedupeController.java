package com.urldedupe.controller;

import com.urldedupe.domain.service.UrlService;
import com.urldedupe.service.UrlFileReader;
import com.urldedupe.view.UrlView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.Set;

@Controller
@RequestMapping(value = "/urldedupe", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UrlDedupeController {

    private final UrlService mUrlService;
    private final UrlFileReader mUrlFileReader;

    @Autowired
    public UrlDedupeController(@Qualifier("in-memory") final UrlService urlService,
                               final UrlFileReader urlFileReader) {
        mUrlService = urlService;
        mUrlFileReader = urlFileReader;
    }

    /**
     * Dedupes all urls that are within all files within the given directory.
     *
     * Requirements:
     * dirname must exist within urlFiles
     * Any number of Files may exist within your chosen dirname
     *
     * Each File Must:
     * - Have no more than 1000000 urls
     * - Be a csv Format
     * - Have one and only one url on each line, with no deliminators
     *
     * @param dirname
     * @return
     */
    @GetMapping(path = "/{dirname}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@PathVariable("dirname") final String dirname) {
        long startTime = System.currentTimeMillis();
        try {
            mUrlFileReader.getFiles(dirname)
                          .parallelStream()
                          .forEach(this::processFile);

            return ResponseEntity.ok("Successfully deduped! Total time elapsed: " + getTimeElapsed(startTime));
        } catch (Exception e) {
            log.error("Error occurred while attempting to dedupe urls for dirName: {}", dirname, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to dedupe urls for dirName: " + dirname);
        }
    }

    /**
     * Retrieves all Urls that we have determined to be unique.
     *
     * Note that this currently only works for smaller in memory subsets. Plans for further improvement on this can be seen in the ReadMe
     * @return
     */
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UrlView> get() {
        long startTime = System.currentTimeMillis();
        try {
            UrlView urlView = UrlView.builder()
                                     .urls(mUrlService.findAll())
                                     .build();
            return ResponseEntity.ok().body(urlView);
        } catch (Exception e) {
            log.error("Error occurred while attempting to retrieve urls", e);
            return ResponseEntity.badRequest()
                                 .body(UrlView.builder()
                                              .build());
        }
    }

    private void processFile(final File file) {
        try {
            Set<String> urls = mUrlFileReader.getUrls(file);
            mUrlService.saveAll(urls);
        } catch (Exception e) {
            log.error("Unable to process file: {}", file.getName(), e);
        }
    }

    private long getTimeElapsed(final long startTime) {
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
