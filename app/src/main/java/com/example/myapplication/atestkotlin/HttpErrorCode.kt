package com.example.myapplication.atestkotlin

/**
 * Copyright (c) 2022 Raysharp.cn. All rights reserved.
 *
 * HttpErrorCode
 * @author liujianming
 * @date 2022-01-05
 */
class HttpErrorCode {

    /**
     * for 401
     */
    var UNAUTHORIZED = "Unauthorized"

    /**
     * for 400
     */
    var DEVICE_HAS_ADD = "DeviceHasAdd"
    var DEVICES_NOT_BELONG_THE_USER = "DevicesNotBelongTheUser"
    var EMAIL_HAS_REGIST = "EmailHasRegist"
    var INTERVAL = "Interval"
    var INVALID_PARAM = "InvalidParam"
    var INVALID_TOKEN = "InvalidToken"
    var NOT_SUCH_EMAIL = "NotSuchEmail"
    var NOT_SUCH_USER = "NotSuchUser"
    var VALIDATE_CODE_INVALID = "ValidateCodeInvalid"

    /**
     * for 500
     */
    var INTERNAL_SERVER_ERROR = "InternalServerError"
}