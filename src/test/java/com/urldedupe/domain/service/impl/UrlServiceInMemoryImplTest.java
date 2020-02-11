package com.urldedupe.domain.service.impl;

import com.urldedupe.domain.Url;
import com.urldedupe.domain.repository.UrlRepository;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UrlServiceInMemoryImplTest {

    UrlServiceInMemoryImpl service;

    @Mock
    private UrlRepository mockUrlRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UrlServiceInMemoryImpl(mockUrlRepository);
    }

    @Nested
    class SaveAll {

        private String url1 = "http://www.url1.com";
        private String url2 = "http://www.url2.com";
        private String url3 = "http://www.url3.com";

        @Test
        void success() {
            Set<String> urls = Sets.newHashSet();
            urls.add(url1);
            urls.add(url2);
            urls.add(url3);
            doThrow(new DataIntegrityViolationException("boom!")).when(mockUrlRepository).save(createUrl(url1));
            doThrow(new RuntimeException("boom!")).when(mockUrlRepository).save(createUrl(url2));

            verify(mockUrlRepository, times(1)).save(createUrl(url1));
            verify(mockUrlRepository, times(1)).save(createUrl(url2));
            verify(mockUrlRepository, times(1)).save(createUrl(url3));
        }

        @Test
        void emptySet() {
            service.saveAll(Collections.emptySet());
            verify(mockUrlRepository, times(0)).save(any());
        }

        @Test
        void npe() {
            assertThrows(NullPointerException.class, () -> service.saveAll(null));
        }

        private Url createUrl(final String url) {
            return Url.builder()
                      .url(url)
                      .build();
        }

    }
}
