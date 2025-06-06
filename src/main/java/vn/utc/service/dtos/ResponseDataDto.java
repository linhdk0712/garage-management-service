package vn.utc.service.dtos;

import lombok.Getter;
import vn.utc.service.config.ResponseCode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Getter
public class ResponseDataDto implements  Serializable {
    private String errorCode = ResponseCode.SUCCESS.name();
    private String errorMessage = ResponseCode.SUCCESS.getMessage();
    private String tranDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault(Locale.Category.FORMAT))
            .format(new Date());
    private Object data = null;

    public ResponseDataDto setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public ResponseDataDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public ResponseDataDto setTranDate(String tranDate) {
        this.tranDate = tranDate;
        return this;
    }

    public ResponseDataDto setData(Object data) {
        this.data = data;
        return this;
    }
}
