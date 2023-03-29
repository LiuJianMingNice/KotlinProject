package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.myapplication.R
import com.example.myapplication.atestkotlin.AccountApiServiceGenerator
import com.example.myapplication.atestkotlin.HttpErrorCode
import com.example.myapplication.atestkotlin.RequestException
import com.example.myapplication.atestkotlin.UserGetValidateCode
import com.example.myapplication.callback.MyClickableSpan
import com.example.myapplication.databinding.ActivityRegister1Binding
//import com.example.myapplication.atest.AccountApiServiceGenerator
//import com.example.myapplication.atest.HttpErrorCode
//import com.example.myapplication.atest.RequestException
//import com.example.myapplication.atest.UserGetValidateCode
//import com.example.myapplication.databinding.ActivityRegisterBinding
import com.example.myapplication.viewmodel.RegisterViewModel
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    lateinit var dataBinding: ActivityRegister1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView<ActivityRegister1Binding>(this,R.layout.activity_register1)
        dataBinding.registerViewModel = RegisterViewModel()

        dataBinding.registerPasswordRulesUtv.setOnClickListener {
            if (dataBinding.registerPasswordRulesContentTv.visibility == View.VISIBLE){
                dataBinding.registerPasswordRulesContentTv.visibility = View.GONE
            } else {
                dataBinding.registerPasswordRulesContentTv.visibility = View.VISIBLE
            }
        }

        dataBinding.registerGetVerificationCodeCb.setOnClickListener {
            dataBinding.registerGetVerificationCodeCb.sendVerifyCode()
            getVerifyCode()
            dataBinding.registerGetVerificationCodeTipTv.visibility = View.VISIBLE
        }

        dataBinding.registerChooseRegionEt.setOnKeyListener(null)
        dataBinding.registerChooseRegionEt.setText("sss")
        dataBinding.registerChooseRegionEt.setOnClickListener {
            Log.d("ljm", "点击了区域选择框2")
        }
        dataBinding.registerChooseRegionEt.inputType = EditorInfo.TYPE_NULL

        var str = getString(R.string.register_signup_agreeTerms_1) + " " + getString(R.string.register_signup_agreeTerms_2)
        var endStr = getString(R.string.register_signup_agreeTerms_2)
        var agreement = SpannableString(str)
        val startingPosition: Int = str.indexOf(endStr)
        val endingPosition: Int = startingPosition + endStr.length
        agreement.setSpan(MyClickableSpan(endStr), startingPosition, endingPosition, SpannableString.SPAN_INCLUSIVE_INCLUSIVE)
        dataBinding.registerPrivacyContentTv.setText(agreement)
        dataBinding.registerPrivacyContentTv.movementMethod = LinkMovementMethod.getInstance()



        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                dataBinding.registerSignUpTv.isEnabled = isRegisterInfoReady()
            }
        }

        dataBinding.registerEmailAddressEt.addTextChangedListener {
            dataBinding.registerSignUpTv.isEnabled = isRegisterInfoReady()
            if (!dataBinding.registerEmailAddressEt.text.toString().equals("")) {
                dataBinding.registerEmailAddressIv.visibility = View.VISIBLE
                if (isEmail(dataBinding.registerEmailAddressEt.text.toString())) {
                    dataBinding.registerEmailAddressIv.setImageDrawable(getDrawable(R.drawable.ic_selected))
                } else {
                    dataBinding.registerEmailAddressIv.setImageDrawable(getDrawable(R.drawable.ic_selected_on_green))
                }
            } else {
                dataBinding.registerEmailAddressIv.visibility = View.GONE
            }

            dataBinding.registerGetVerificationCodeCb.isEnabled = isEmail(dataBinding.registerEmailAddressEt.text.toString().trim { it <= ' ' })
        }
        dataBinding.registerPasswordEt.addTextChangedListener(textWatcher)
        dataBinding.registerConfirmPasswordPv.addTextChangedListener(textWatcher)
        dataBinding.registerEnterCodeEt.addTextChangedListener(textWatcher)

        dataBinding.registerAgreePrivacyCb.setOnClickListener {
            dataBinding.registerSignUpTv.isEnabled = isRegisterInfoReady()
        }
    }

    private fun isRegisterInfoReady(): Boolean {

        val email: String = dataBinding.registerEmailAddressEt.text.toString().trim { it <= ' ' }
        val password: String = dataBinding.registerPasswordEt.text.toString().trim()
        val confirmPassword: String = dataBinding.registerConfirmPasswordPv.text.toString().trim()
        val validateCode: String = dataBinding.registerEnterCodeEt.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            return false
        }
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            return false
        }
        if (TextUtils.isEmpty(validateCode)) {
            return false
        }
        if (!dataBinding.registerAgreePrivacyCb.isChecked) {
            return false
        }
        return true
    }

    fun getVerifyCode() {
        val email: String = dataBinding.registerEmailAddressEt.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "IDS_ACCOUNT_MANAGER_USERNAME_EMPT",Toast.LENGTH_SHORT).show()
            return
        }


//        AccountApiServiceGenerator.createService(this@RegisterActivity)
//            .getValidateCode(UserGetValidateCode.RequestParam(email))
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<ResponseBody> {
//                override fun onSubscribe(d: Disposable) {}
//                override fun onNext(responseBody: ResponseBody) {
//                    Log.i("ljm", "contentType: " + responseBody.contentType() + ", contentLength: " + responseBody.contentLength()
//                    )
//                }
//
//                override fun onError(e: Throwable) {
//                    if (e is RequestException) {
//                        val errorCode: String = (e as RequestException).getCode()
//                        Log.e(
//                            "ljm",
//                            "getValidateCode failed, errorCode: %s" + errorCode
//                        )
//                        if (HttpErrorCode.EMAIL_HAS_REGIST.equals(errorCode)) {
//                            Toast.makeText(applicationContext, "IDS_ACCOUNT_MANAGER_EMAIL_HAS_REGIST",Toast.LENGTH_SHORT).show()
//                            return
//                        } else if (HttpErrorCode.INTERNAL_SERVER_ERROR.equals(errorCode)) {
//                            val errorMessage = e.message
//                            if (!TextUtils.isEmpty(errorMessage)) {
//                                Toast.makeText(applicationContext, errorMessage,Toast.LENGTH_SHORT).show()
//                            }
//                            Log.w(
//                                "ljm",
//                                "getValidateCode failed, reason: $errorMessage"
//                            )
//                            return
//                        } else if (HttpErrorCode.INTERVAL.equals(errorCode)) {
//                            val errorMessage = e.message
//                            if (!TextUtils.isEmpty(errorMessage)) {
//                                val interval = errorMessage!!.toDouble()
//                                Log.w(
//                                    "ljm",
//                                    "getValidateCode failed, interval: $interval"
//                                )
//                                Toast.makeText(applicationContext, "IDS_TOAST_CONTENT_REQUEST_TOO_FREQUENTLY",Toast.LENGTH_SHORT).show()
//                            }
//                            return
//                        } else if (HttpErrorCode.INVALID_PARAM.equals(errorCode)) {
//                            Toast.makeText(applicationContext, "IDS_ACCOUNT_MANAGER_INVALID_PARAM",Toast.LENGTH_SHORT).show()
//                            return
//                        }
//                    } else {
//                        Log.e("ljm", "getValidateCode onError, message: %s" + e.message)
//                    }
//                    Toast.makeText(applicationContext, "IDS_TOAST_CONTENT_REQUEST_FAILED",Toast.LENGTH_SHORT).show()
//                    Log.d("ljm", "onError: " + e.message)
//                }
//
//                override fun onComplete() {
//                    Log.d("ljm", "onComplete: ")
//                }
//            })

        AccountApiServiceGenerator().createService(this@RegisterActivity)
            ?.getValidateCode(UserGetValidateCode.RequestParam(email))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<ResponseBody?> {
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(responseBody: ResponseBody?) {
                    Log.i("ljm", "contentType: " + responseBody!!.contentType() + ", contentLength: " + responseBody.contentLength()
                    )
                }

                override fun onError(e: Throwable) {
                    if (e is RequestException) {
                        val errorCode: String = e.getCode().toString()
                        Log.e(
                            "ljm",
                            "getValidateCode failed, errorCode: %s" + errorCode
                        )
                        if (HttpErrorCode().EMAIL_HAS_REGIST.equals(errorCode)) {
                            Toast.makeText(applicationContext, "IDS_ACCOUNT_MANAGER_EMAIL_HAS_REGIST",Toast.LENGTH_SHORT).show()
                            return
                        } else if (HttpErrorCode().INTERNAL_SERVER_ERROR.equals(errorCode)) {
                            val errorMessage = e.message
                            if (!TextUtils.isEmpty(errorMessage)) {
                                Toast.makeText(applicationContext, errorMessage,Toast.LENGTH_SHORT).show()
                            }
                            Log.w(
                                "ljm",
                                "getValidateCode failed, reason: $errorMessage"
                            )
                            return
                        } else if (HttpErrorCode().INTERVAL.equals(errorCode)) {
                            val errorMessage = e.message
                            if (!TextUtils.isEmpty(errorMessage)) {
                                val interval = errorMessage!!.toDouble()
                                Log.w(
                                    "ljm",
                                    "getValidateCode failed, interval: $interval"
                                )
                                Toast.makeText(applicationContext, "IDS_TOAST_CONTENT_REQUEST_TOO_FREQUENTLY",Toast.LENGTH_SHORT).show()
                            }
                            return
                        } else if (HttpErrorCode().INVALID_PARAM.equals(errorCode)) {
                            Toast.makeText(applicationContext, "IDS_ACCOUNT_MANAGER_INVALID_PARAM",Toast.LENGTH_SHORT).show()
                            return
                        }
                    } else {
                        Log.e("ljm", "getValidateCode onError, message: %s" + e.message)
                    }
                    Toast.makeText(applicationContext, "IDS_TOAST_CONTENT_REQUEST_FAILED",Toast.LENGTH_SHORT).show()
                    Log.d("ljm", "onError: " + e.message)
                }

                override fun onComplete() {
                    Log.d("ljm", "onComplete: ")
                }

            })
    }

    fun isEmail(email: String?): Boolean {
        val str =
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"
        val p: Pattern = Pattern.compile(str)
        val m: Matcher = p.matcher(email)
        return m.matches()
    }
}