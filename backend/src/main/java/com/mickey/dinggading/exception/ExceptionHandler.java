package com.mickey.dinggading.exception;

import com.mickey.dinggading.base.code.BaseErrorCode;

public class ExceptionHandler extends GeneralException {

    public ExceptionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}