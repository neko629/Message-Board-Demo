package com.laorunzi.msgboard.model;

import com.laorunzi.msgboard.utils.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 2 * @Author: Crimson
 * 3 * @Date: 2022/3/5 14:40
 * 4 service 返回用对象
 */
public class ServiceResult<T> {
    private boolean ok; //执行结果
    private int errorCode;
    private String errorMsg; //错误信息
    private T model; //返回数据, 不需要返回数据的操作返回 boolean 类型

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    /**
     * 私有化无参构造函数
     */
    private ServiceResult() {

    }

    public ServiceResult(boolean ok, int errorCode, String errorMsg, T model) {
        this.ok = ok;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.model = model;
    }

    public ServiceResult(boolean ok, int errorCode, String errorMsg) {
        this.ok = ok;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.model = null;
    }

    /**
     * 转为传输给前端的 map
     * @param result
     * @param keyName 返回前端的数据的key，其他数据的 key 为 data
     * @return
     */
    public static Map<String, Object> transfer2Map(ServiceResult result, String keyName) {
        Map<String, Object> resultMap = new HashMap<>();
        if (result.ok) { // 处理成功
            resultMap.put("code", result.getErrorCode());
            resultMap.put("msg",result.getErrorMsg());
            if (result.getModel() != null) {
                resultMap.put(StringUtils.isNotBlank(keyName) ? keyName : "data", result.getModel());
            }
        } else { // 处理失败，返回错误信息
            resultMap.put("code", result.getErrorCode());
            resultMap.put("msg", result.getErrorMsg());
            if (result.getModel() != null) {
                resultMap.put(StringUtils.isNotBlank(keyName) ? keyName : "data", result.getModel());
            }
        }
        return resultMap;
    }

    public static Map<String, Object> transfer2Map(ServiceResult result) {
        return transfer2Map(result, null);
    }
}
