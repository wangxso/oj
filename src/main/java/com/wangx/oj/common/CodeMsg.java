package com.wangx.oj.common;

/**
 * 只要get不要set,进行更好的封装
 */
public class CodeMsg {

    private int code;
    private String msg;

    //通用的异常
    public static CodeMsg SUCCESS=new CodeMsg(0,"success");

    public static CodeMsg SERVER_ERROR=new CodeMsg(50000,"服务端异常");
    public static CodeMsg PASSWORD_EMPTY=new CodeMsg(50001,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(50002,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR=new CodeMsg(50003,"手机号格式错误");
    public static CodeMsg NO_USER = new CodeMsg(50004,"用户不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(50005,"密码错误");
    public static CodeMsg NOT_FOUND = new CodeMsg(50006, "此数据不存在");
    public static CodeMsg ACCOUNT_LOCKED = new CodeMsg(50007, "账户已锁定");
    public static CodeMsg VERITY_CODE_ERROR = new CodeMsg(50008, "验证码错误，或已失效");
    public static CodeMsg ACCOUNT_PASSWORD_EMPTY=new CodeMsg(50009,"账号或密码不能为空");
    public static CodeMsg EXCESSIVE_ATTEMPTS = new CodeMsg(50010, "操作频繁，请稍后再试");
    public static CodeMsg NOT_LOGIN = new CodeMsg(50011, "未登录");
    public static CodeMsg PERMIT_DENY = new CodeMsg(50012, "权限不足");
    public static CodeMsg SESSION_TIMEOUT = new CodeMsg(50013, "会话超时");
    public static CodeMsg VERITY_CODE_IN_TIME = new CodeMsg(50014, "验证码已发送，请五分钟后再试");
    public static CodeMsg UPLOAD_FAIL = new CodeMsg(50015, "上传失败");


    public CodeMsg fillArgs(Object...args){
        int code=this.code;
        String message=String.format(this.msg,args);
        return new CodeMsg(code,message);
    }

    private CodeMsg(int code, String msg) {
        this.code=code;
        this.msg=msg;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    //注意需要重写toString 方法,不然到前端页面是一个对象的地址....
    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

}
