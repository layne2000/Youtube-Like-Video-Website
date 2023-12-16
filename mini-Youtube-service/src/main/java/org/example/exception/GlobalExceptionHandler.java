package org.example.exception;

import org.example.util.JsonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public JsonResponse<String> handleGlobalException(Exception e){
        if(e instanceof CustomizedException){
            return new JsonResponse<>(((CustomizedException) e).getCode(), e.getMessage());
        } else{
            return new JsonResponse<>("500", e.getMessage());
        }
    }
}
