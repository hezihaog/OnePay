package com.zh.android.onepay.wxpay.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * <b>Package:</b> com.zh.android.onepay.wxpay <br>
 * <b>Create Date:</b> 2020-02-11  10:10 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 微信支付结果回调界面的基类 <br>
 */
public class WXPayActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXPayActivity.class.getSimpleName();

    /**
     * 微信支付API对象
     */
    protected IWXAPI mWXAPI;
    /**
     * 微信支付回调
     */
    private OnWXPayEntryaCallBack mWXPayCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_NoDisplay);
        super.onCreate(savedInstanceState);
        mWXPayCallBack = WXPayCallBackInstance.get().getWXPayCallBack();
        String id = WXStorage.getWxPayAppId();
        mWXAPI = WXPayHelper.getWxApi(this, id, false, true);
        mWXAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWXAPI.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //调用的是否微信支付
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //客户端返回状态码
            if (resp.errCode == 0) {
                onPaySuccessed(resp.errStr);
            } else if (resp.errCode == -2) {
                onPayCancel(resp.errStr);
            } else if (resp.errCode == -1) {
                onPayFailture(resp.errStr, String.valueOf(resp.errCode));
            }
            Log.i(TAG, "str:" + resp.errStr + "\nopenid:" + resp.openId + " \ntran:" + resp.transaction + "\ncode:" + resp.errCode);
        }
        finish();
    }

    /**
     * 支付过程取消
     *
     * @param developerpayload 回调的代码
     */
    protected void onPayCancel(String developerpayload) {
        if (mWXPayCallBack != null) {
            mWXPayCallBack.onPayCancel(developerpayload);
        }
    }

    /**
     * 支付成功
     *
     * @param developerpayload 回调的代码
     */
    protected void onPaySuccessed(String developerpayload) {
        if (mWXPayCallBack != null) {
            mWXPayCallBack.onPaySuccessed(developerpayload);
        }
    }

    /**
     * 支付失败
     *
     * @param developerpayload 回调的代码
     * @param code             失败代号
     */
    protected void onPayFailture(String developerpayload, String code) {
        if (mWXPayCallBack != null) {
            mWXPayCallBack.onPayFailture(developerpayload, code);
        }
    }
}