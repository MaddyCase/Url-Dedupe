package com.urldedupe.domain.service;

import com.urldedupe.domain.Url;

import java.util.List;
import java.util.Set;

public interface UrlService {
    public void saveAll(final Set<String> urls);
    public List<Url> findAll();
}
