package com.teasmile.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务端统一响应报文
 *
 * @param <T> 业务数据类型
 */
@Data
public class ApiResult<T> implements Serializable {

    /** 处理成功的状态码 */
    public static final int CODE_SUCCESS = 1;
    /** 处理失败的状态码 */
    public static final int CODE_ERROR = 0;

    /** 业务状态码：1 成功，0 失败 */
    private Integer code;

    /** 提示文案，失败时用于展示错误原因 */
    private String msg;

    /** 响应数据 */
    private T data;

    private ApiResult() {
    }

    /**
     * 不带数据的成功返回
     */
    public static <T> ApiResult<T> success() {
        return assemble(CODE_SUCCESS, null, null);
    }

    /**
     * 携带数据的成功返回
     */
    public static <T> ApiResult<T> success(T data) {
        return assemble(CODE_SUCCESS, null, data);
    }

    /**
     * 失败返回
     *
     * @param msg 错误提示信息
     */
    public static <T> ApiResult<T> error(String msg) {
        return assemble(CODE_ERROR, msg, null);
    }

    private static <T> ApiResult<T> assemble(int code, String msg, T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
