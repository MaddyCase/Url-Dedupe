package com.urldedupe.domain.service.impl;

import com.urldedupe.domain.Url;
import com.urldedupe.domain.service.UrlService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Qualifier("db")
public class UrlServiceImpl implements UrlService {

    @Autowired
    public UrlServiceImpl() { }

    @Override
    public void saveAll(@NonNull final Set<String> urls) {
        throw new NotImplementedException();
    }

    @Override
    public List<Url> findAll() {
        throw new NotImplementedException();
    }
}
