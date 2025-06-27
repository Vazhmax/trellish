package org.vazhmax.trellish.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public final class BoardDto {
    private Integer id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TaskStateDto> taskStates;
}
