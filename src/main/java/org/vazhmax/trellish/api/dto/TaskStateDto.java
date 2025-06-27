package org.vazhmax.trellish.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class TaskStateDto {
    private Integer id;
    private String name;
    private String lexorank;
    private Integer boardId;
}
