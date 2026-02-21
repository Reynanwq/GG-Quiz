package com.ggquiz.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Region {
    private Integer id;
    private String slug;
    private String name;
    private boolean active;
}