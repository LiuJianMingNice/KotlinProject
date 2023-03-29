package com.example.myapplication.atest;

/**
 * @author liuaibin
 */
public interface HttpErrorCode {

    /**
     * for 401
     */
    String UNAUTHORIZED = "Unauthorized";

    /**
     * for 400
     */
    String DEVICE_HAS_ADD = "DeviceHasAdd";
    String DEVICES_NOT_BELONG_THE_USER = "DevicesNotBelongTheUser";
    String EMAIL_HAS_REGIST = "EmailHasRegist";
    String INTERVAL = "Interval";
    String INVALID_PARAM = "InvalidParam";
    String INVALID_TOKEN = "InvalidToken";
    String NOT_SUCH_EMAIL = "NotSuchEmail";
    String NOT_SUCH_USER = "NotSuchUser";
    String VALIDATE_CODE_INVALID = "ValidateCodeInvalid";

    /**
     * for 500
     */
    String INTERNAL_SERVER_ERROR = "InternalServerError";
}
