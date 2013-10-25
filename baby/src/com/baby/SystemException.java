package com.baby;
/**
 * 异常类
 * @author Zhoujun
 */
public class SystemException extends Exception {
	private static final long serialVersionUID = -1142757504410805212L;
	private int statusCode = -1;

    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(Exception cause) {
        super(cause);
    }

    public SystemException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public SystemException(String msg, Exception cause) {
        super(msg, cause);
    }

    public SystemException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
