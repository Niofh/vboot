package com.example.redisdemo;


/**
 * created by Nicofh on 2020-03-08
 */
public class ResultUtil<T> {

    private Result<T> result;

    public ResultUtil() {
        result = new Result<>();
        result.setSuccess(true);
        result.setMsg("success");
        result.setCode(200);
    }

    public Result<T> setData(T t) {
        this.result.setData(t);
        this.result.setCode(200);
        return this.result;
    }

    public Result<T> setSuccessMsg(String msg) {
        this.result.setSuccess(true);
        this.result.setMsg(msg);
        this.result.setCode(200);
        this.result.setData(null);
        return this.result;
    }

    public Result<T> setData(T t, String msg) {
        this.result.setData(t);
        this.result.setCode(200);
        this.result.setMsg(msg);
        return this.result;
    }

    public Result<T> setErrorMsg(String msg) {
        this.result.setSuccess(false);
        this.result.setMsg(msg);
        this.result.setCode(500);
        return this.result;
    }

    public Result<T> setErrorMsg(Integer code, String msg) {
        this.result.setSuccess(false);
        this.result.setMsg(msg);
        this.result.setCode(code);
        return this.result;
    }

    public static <T> Result<T> data(T t) {
        return new ResultUtil<T>().setData(t);
    }

    public static <T> Result<T> data(T t, String msg) {
        return new ResultUtil<T>().setData(t, msg);
    }

    public static <T> Result<T> success(String msg) {
        return new ResultUtil<T>().setSuccessMsg(msg);
    }

    public static <T> Result<T> error(String msg) {
        return new ResultUtil<T>().setErrorMsg(msg);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new ResultUtil<T>().setErrorMsg(code, msg);
    }
}
