package org.vazhmax.trellish.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;
}
