package com.dk.project.post.retrofit;

/**
 * Created by dkkim on 2017-12-12.
 */

public class ErrorResponse {

    private int errorType;
    private int errorCode;
    private String errorMessage;


    public ErrorResponse(int errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(int errorType, int errorCode, String errorMessage) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
