package com.urldedupe.view;

import com.urldedupe.domain.Url;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UrlView implements Serializable {
    private List<Url> urls;
}
