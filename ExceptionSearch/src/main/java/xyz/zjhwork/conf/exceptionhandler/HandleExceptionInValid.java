package xyz.zjhwork.conf.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.zjhwork.dto.ResponseModel;

import java.util.Objects;

/**
 * @author zjhChester
 */
@Slf4j
@Component
@ControllerAdvice
public class HandleExceptionInValid {


    @ResponseBody
    @ExceptionHandler({org.springframework.validation.BindException.class})

    public ResponseEntity<ResponseModel> handleValidException(BindException e) {
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        //将错误信息返回给前台
        return ResponseEntity.status(500).body(ResponseModel.failResModel(0,Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));
    }
    //

    /**
     * MethodArgumentNotValidException.class
     * @param e 异常类型
     * @return 用于控制系统的整体返回格式DTO
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        //将错误信息返回给前台
        return ResponseEntity.status(500).body(ResponseModel.failResModel(0,Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage()));

    }
}
