package com.ant.mall.product.exception;

import com.ant.common.exception.BizCodeEnum;
import com.ant.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据校验统一处理类
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.ant.mall.product.controller")
public class MallExceptionControllerAdvice {

    private BindingResult bindingResult;

    /**
     * 处理数据校验的异常
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        //记录异常日志
        log.error("数据校验出现问题{}。异常类型{}", e.getMessage() ,e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,Object> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().stream().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }

    /**
     * 处理所有的异常
     * @param throwable 异常类型
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    public R handleValid(Throwable throwable){
        log.error("错误：",throwable);
        return R.error(BizCodeEnum.UNKNOWN_EXCEPTION.getCode(), BizCodeEnum.UNKNOWN_EXCEPTION.getMsg());
    }
}
