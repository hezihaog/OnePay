package com.zh.android.onepay.alipay;

import android.text.TextUtils;

import java.util.Map;

/**
 * <b>Package:</b> com.zh.android.onepay.alipay <br>
 * <b>Create Date:</b> 2020-02-10  17:40 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 支付结果实体，取自支付宝Demo <br>
 */
public class AliPayResult {
    private String resultStatus;
    private String result;
    private String memo;

    AliPayResult(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }
        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result")) {
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
}