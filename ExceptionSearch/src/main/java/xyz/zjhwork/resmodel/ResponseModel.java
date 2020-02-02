package xyz.zjhwork.resmodel;

import java.util.Arrays;

public class ResponseModel {
    private Integer code;
    private String desc;
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
    public static ResponseModel getResponseModel(){
        return new ResponseModel();
    }
    @Override
    public String toString() {
        return "ResponseModel{" +
                "code=" + code +
                ", desc='" + desc + '\'' +
                ", result=" + Arrays.toString(result) +
                '}';
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object[] getResult() {
        return result;
    }

    public void setResult(Object[] result) {
        this.result = result;
    }
}
