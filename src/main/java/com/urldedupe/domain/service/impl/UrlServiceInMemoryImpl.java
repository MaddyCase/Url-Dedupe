package com.urldedupe.domain.service.impl;

import com.urldedupe.domain.service.UrlService;
import com.urldedupe.domain.Url;
import com.urldedupe.domain.repository.UrlRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
@Qualifier("in-memory")
public class UrlServiceInMemoryImpl implements UrlService {

    private UrlRepository mUrlRepository;

    @Autowired
    public UrlServiceInMemoryImpl(final UrlRepository urlRepository) {
        mUrlRepository = urlRepository;
    }

    @Override
    public void saveAll(@NonNull final Set<String> urls) {
        urls.parallelStream().forEach(this::save);
    }

    private void save(final String url) {
        try {
            mUrlRepository.save(Url.builder()
                                   .url(url)
                                   .build()
            );
        } catch (DataIntegrityViolationException e) {
            // do nothing since this is an anticipated case when running parallel streams
        } catch (Exception e) {
            log.error("Unexpected error occurred when attempting to save url: {}", url, e);
        }
    }
}
