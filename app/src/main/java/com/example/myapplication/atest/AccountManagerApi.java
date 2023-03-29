package com.example.myapplication.atest;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author liuaibin
 */
public interface AccountManagerApi {

    /**
     * 用户注册
     * <p>
     * 不需要鉴权
     *
     * @param param {@link UserRegister.RequestParam}
     * @return
     */
    @POST("common/api/User/Regist")
    Observable<ResponseBody> register(@Body UserRegister.RequestParam param);

    /**
     * 获取验证码
     * <p>
     * 不需要鉴权
     *
     * @param param
     * @return
     */
    @POST("common/api/User/GetValidateCode")
    Observable<ResponseBody> getValidateCode(@Body UserGetValidateCode.RequestParam param);


}
