package com.urldedupe.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void saveAll(final Set<String> urls) {
        for (String url : urls) {
            try {
                mUrlRepository.save(Url.builder()
                                       .url(url)
                                       .build()
                );
            } catch (Exception e) {
                // do nothing since this is an anticipated case
            }
        }
    }

    @Override
    public List<String> findAll() {
        return mUrlRepository.findAll().stream()
                             .map(Url::getUrl)
                             .collect(Collectors.toList());
    }
}
