package com.zl.minio.exception;


/**
 * @ClassName
 * @Description excel表格操作异常
 * @Date $ $
 **/
public class ExcelException extends  BaseException {
    public ExcelException() {
        super(SystemErrorType.EXCEL_ERROR);
    }

    public ExcelException(String message) {
        super(SystemErrorType.EXCEL_ERROR, message);
    }
}