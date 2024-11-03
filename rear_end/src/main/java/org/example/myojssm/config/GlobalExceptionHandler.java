package org.example.myojssm.config;

import jakarta.validation.ConstraintViolationException;
import org.example.myojssm.common.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: liaoyueyue
 * Date: 2024-03-23
 * Time: 13:58
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<Void> exceptionAdvice(Exception e) {
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())?e.getMessage():"Operation failed");
    }

    @ExceptionHandler(NullPointerException.class)
    public Result<Void> nullPointerExceptionAdvice(NullPointerException e) {
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())?e.getMessage():"Operation failed");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> constraintViolationExceptionAdvice(ConstraintViolationException e) {
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())?"Illegal parameters":"Operation failed");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> methodArgumentNotValidExceptionAdvice(MethodArgumentNotValidException e) {
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())?"Illegal parameters":"Operation failed");
    }

}
