package com.urldedupe.domain.service;

import java.util.Set;

public interface UrlService {
    void saveAll(final Set<String> urls);
}
