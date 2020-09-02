package xyz.zjhwork.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.resmodel.ResponseModel;

import java.util.Objects;

@Slf4j
@Component
@ControllerAdvice
public class HandleExceptionInValid {

    private final static String EXCEPTION_MSG_KEY = "Exception message : ";

    @ResponseBody
    @ExceptionHandler({org.springframework.validation.BindException.class})
    public ResponseModel handleValidException(BindException e) {
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        //将错误信息返回给前台
        return ResponseModel.failResModel(0,Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    //    MethodArgumentNotValidException.class
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseModel handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        //将错误信息返回给前台
        return ResponseModel.failResModel(0,Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}
