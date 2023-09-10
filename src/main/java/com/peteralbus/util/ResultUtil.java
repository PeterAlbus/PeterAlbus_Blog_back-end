package com.peteralbus.util;

import com.peteralbus.domain.Result;

/**
 * The type Result util.
 */
public class ResultUtil {

    /**
     * Success result.
     *
     * @param <T>  the type parameter
     * @param data the data
     * @return the result
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * Success result with message.
     *
     * @param <T>     the type parameter
     * @param data    the data
     * @param message the message
     * @return the result
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * Error result.
     *
     * @param code    the code
     * @param message the message
     * @return the result
     */
    public static Result<?> error(int code, String message) {
        Result<?> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

}
