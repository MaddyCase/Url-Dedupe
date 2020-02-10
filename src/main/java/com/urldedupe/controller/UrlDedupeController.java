package com.urldedupe.controller;

import com.urldedupe.domain.service.UrlService;
import com.urldedupe.service.UrlFileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping(path = "/{filename}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@PathVariable("filename") final String filename) {
        long startTime = System.nanoTime(); // in nanoseconds
        try {
            // Read File into a Set
            Set<String> urls = mUrlFileReader.getUrls(filename);

            // Once we have the Set, lets save it all with the Service
            mUrlService.saveAll(urls);

            return ResponseEntity.ok("Successfully deduped! Total time elapsed: " + getTimeElapsed(startTime));
        } catch (Exception e) {
            log.error("Error occurred while attempting to dedupe urls for filename: {}", filename, e);
            return ResponseEntity
                    .status(500)
                    .body("Unable to dedupe urls for fileName: " + filename);
        }
    }

    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity get() {
        try {
            return ResponseEntity.ok("Urls: " + mUrlService.findAll());
        } catch (Exception e) {
            log.error("Error occurred while attempting to retrieve urls", e);
            return ResponseEntity
                    .status(500)
                    .body("Unable to retrieve urls");
        }
    }

    private long getTimeElapsed(final long startTime) {
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
