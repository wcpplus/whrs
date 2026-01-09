package org.farm2.base.exception;

/**
 * Farm业务异常
 */
public class FarmAppException extends RuntimeException {
    public FarmAppException(String msg) {
        super(msg);
    }
}
