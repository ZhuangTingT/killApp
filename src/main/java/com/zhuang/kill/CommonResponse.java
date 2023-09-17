package com.zhuang.kill;

public class CommonResponse {
    // 表明对应请求的返回处理结果 1(success) 或 0("fail")
    private Integer status;
    private Object data;

    // 定义一个通用的创建方法
    public static CommonResponse create(Object data){
        return CommonResponse.create(1, data);
    }

    public static CommonResponse create(Integer status, Object data){
        CommonResponse commonReturnType = new CommonResponse();
        commonReturnType.setData(data);
        commonReturnType.setStatus(status);
        return commonReturnType;
    }
    // 若 status = 1 ,则 data内包含前端需要的json数据
    // 若 status = 0 ，则data内使用通用的错误码 格式
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
