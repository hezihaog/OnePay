package com.zh.android.onepay;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>Package:</b> com.zh.android.onepay <br>
 * <b>Create Date:</b> 2020-02-10  16:50 <br>
 * <b>@author:</b> zihe <br>
 * <b>Description:</b> 支付参数 <br>
 */
public class PayParams implements Serializable {
    private static final long serialVersionUID = -5858547759051610924L;

    /**
     * 支付字符串
     */
    private static final String KEY_PAY_STRING = "key_pay_string";

    /**
     * 参数存储
     */
    private Map<String, Object> mParams;

    private PayParams() {
        mParams = new ConcurrentHashMap<>();
    }

    /**
     * 根据支付字符串，快速创建，如果需要附加参数，则调用下面addParams()等的方法
     *
     * @param payString 支付字符串，一般由后端接口生成后返回
     */
    public PayParams(String payString) {
        this();
        addParams(KEY_PAY_STRING, payString);
    }

    /**
     * 获取支付字符串
     */
    public String getPayString() {
        return (String) getParams(KEY_PAY_STRING);
    }

    //---------------------- 增加、删除、获取附加参数 ----------------------

    /**
     * 增加参数
     */
    public void addParams(String key, Object value) {
        mParams.put(key, value);
    }

    /**
     * 移除参数
     */
    public void removeParams(String key) {
        mParams.remove(key);
    }

    /**
     * 获取参数
     */
    public Object getParams(String key) {
        return mParams.get(key);
    }

    /**
     * 获取参数Map
     */
    public Map<String, Object> getParamsMap() {
        return new HashMap<>(mParams);
    }
}