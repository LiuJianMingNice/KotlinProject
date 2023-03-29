package com.example.myapplication.atest;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import okhttp3.Response;

/**
 * @author liuaibin
 */
public class HandleErrorInterceptor extends AbstractResponseBodyInterceptor {
    private static final String TAG = "HandleErrorInterceptor";

    @Override
    Response intercept(Response response, String url, String body) throws RequestException {
        if (response.code() == 400) {
            JSONObject jsonObject = JSON.parseObject(body);
            if (jsonObject.containsKey("errorCode")) {
                String errorCode = jsonObject.getString("errorCode");
                if (HttpErrorCode.EMAIL_HAS_REGIST.equals(errorCode)
                        || HttpErrorCode.VALIDATE_CODE_INVALID.equals(errorCode)
                        || HttpErrorCode.NOT_SUCH_EMAIL.equals(errorCode)
                        || HttpErrorCode.NOT_SUCH_USER.equals(errorCode)
                        || HttpErrorCode.DEVICE_HAS_ADD.equals(errorCode)
                        || HttpErrorCode.DEVICES_NOT_BELONG_THE_USER.equals(errorCode)) {
                    throw new RequestException(errorCode);
                } else if (HttpErrorCode.INTERVAL.equals(errorCode)
                        || HttpErrorCode.INVALID_PARAM.equals(errorCode)
                        || HttpErrorCode.INVALID_TOKEN.equals(errorCode)) {
                    String errorMessage = jsonObject.getString("errorMessage");
                    throw new RequestException(errorCode, errorMessage);
                } else {
//                    Log.e(TAG, "UnKnown errorCode [%s], body: %s", errorCode, body);
                }
            } else {
//                Log.e(TAG, "UnKnown error, body: %s", body);
            }
        } else if (response.code() == 401) {
            Log.e(TAG, "Got en 401 Error");
            throw new RequestException(HttpErrorCode.UNAUTHORIZED);
        } else if (response.code() == 500) {
            throw new RequestException(HttpErrorCode.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

        return response;
    }
}
