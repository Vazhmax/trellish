package org.vazhmax.trellish.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class BoardDto {
    private Integer id;
    private String name;
}
