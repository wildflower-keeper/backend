package org.wildflowergardening.backend.core.wildflowergardening.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseBody<T>  {
    private int status;
    private String message;
    private T data;

    public BaseResponseBody(int status, String message) {
        this.status = status;
        this.message = message;
    }
}