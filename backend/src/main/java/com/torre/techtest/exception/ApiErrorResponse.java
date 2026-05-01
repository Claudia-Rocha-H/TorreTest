package com.torre.techtest.exception;

public record ApiErrorResponse(int status, String error, String message, String path) {
}