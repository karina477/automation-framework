package org.example.automationframework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private String responseStatus;
    private String responseMessage;
    private ResponseError responseError;
    private T responseBody;

    public ApiResponse() {
    }

    public ApiResponse(String responseStatus, String responseMessage, ResponseError responseError, T responseBody) {
        this.responseStatus = responseStatus;
        this.responseMessage = responseMessage;
        this.responseError = responseError;
        this.responseBody = responseBody;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseError getResponseError() {
        return responseError;
    }

    public void setResponseError(ResponseError responseError) {
        this.responseError = responseError;
    }

    public T getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(T responseBody) {
        this.responseBody = responseBody;
    }
}