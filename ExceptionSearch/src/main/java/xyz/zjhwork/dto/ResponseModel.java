package xyz.zjhwork.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用于控制系统的整体返回格式DTO
 * @author zjhChester
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModel {
    /**
     * code 成功：1   失败：0    异常：-1
     */
    private Integer code;
    /**
     * 描述信息
     */
    private String desc;
    /**
     * 请求结果  成功：携带返回体   失败null
     */
    private Object[] result;

    public static ResponseModel successResModel(Integer code,String desc,Object[] result){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setResult(result);
        responseModel.setDesc(desc);
        responseModel.setCode(code);
        return responseModel;
    }
    public static ResponseModel failResModel(Integer code,String desc){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setDesc(desc);
        responseModel.setCode(code);
        return responseModel;
    }
    public static ResponseModel failResModel(Integer code,String desc,Object[] result){
        ResponseModel responseModel = new ResponseModel();
        responseModel.setDesc(desc);
        responseModel.setCode(code);
        responseModel.setResult(result);
        return responseModel;
    }
    public static ResponseModel getResponseModel(){
        return new ResponseModel();
    }

}
