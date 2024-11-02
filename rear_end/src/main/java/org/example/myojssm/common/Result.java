package org.example.myojssm.common;


import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description: 统一数据格式返回
 * User: liaoyueyue
 * Date: 2024-2-29
 * Time: 22:07
 */
@Data
public class Result<T> {
    // 状态码
    private Integer code;
    // 状态码描述信息
    private String msg;
    // 返回的数据
    private T data;

    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(-1, msg, null);
    }

    public static <T> Result<T> error() {
        return new Result<>(-1, "illegal request", null);
    }

}
