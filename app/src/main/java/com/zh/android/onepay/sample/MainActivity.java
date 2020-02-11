package com.zh.android.onepay.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zh.android.onepay.IPayCallback;
import com.zh.android.onepay.PayParams;
import com.zh.android.onepay.sample.pay.PayManager;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button callAlipay = findViewById(R.id.call_alipay);
        Button wxPay = findViewById(R.id.call_wxpay);
        final IPayCallback payCallback = new IPayCallback() {
            @Override
            public void onPaySuccess() {
                toast("用户取消-成功");
            }

            @Override
            public void onPayFailture(Throwable error, String msg) {
                toast("用户取消-失败，原因：" + msg);
            }

            @Override
            public void onPayCancel() {
                toast("用户取消-支付");
            }
        };
        //支付宝支付
        callAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payString = "{charge_url:\"\",parameters:\"{\"string\":\"app_id=2017120600403397&method=alipay.trade.app.pay&format=JSON&return_url=http%3A%2F%2Fsandbox.money.linghit.com%2Freturn%2F45%2F152188183109500080&charset=utf8&sign_type=RSA2&timestamp=2018-03-24+16%3A57%3A08&version=1.0&notify_url=http%3A%2F%2Fsandbox.money.linghit.com%2Fnotify_charge%2F45&biz_content=%7B%22out_trade_no%22%3A%22152188183109500080%22%2C%22total_amount%22%3A%220.10%22%2C%22subject%22%3A%22%5Cu6613%5Cu8d77%5Cu95ee%22%2C%22body%22%3A%22%5Cu6613%5Cu8d77%5Cu95ee%22%7D&sign=HwC7i1JkrWqmLRwUeuwhx8XGIspIv%2BkIJdApJt7PSKn30XjvkksT4aefdANNiW9QWTgNmGsmzf8Nz52UMEpDcs0qBxPF5Rc2lP%2B8SwKqruCUHiv7j4%2FCO1%2F82lLLCjahD87S%2B8ccUS7pfxCC44GZRndKpAZFUA9cEaFIMIvwW1ie9wmBScwua0NksNecA7U5Mm%2BRkhUlTt4mMb7SugFE9tTBkzLpUgTr2OVie1TFboxuqbMi34RTGLjW8wLzuTIEvPj%2F%2BxfcISU6wJ0MKAla4D8golcdbfHeO6xU80kwCj9kqy9LewWqOuVDAwu%2B7fFCy7VjzOlRhQQXdtlo84%2BPzQ%3D%3D\"}\"}";
                PayManager.getInstance().alipay(MainActivity.this, new PayParams(payString), payCallback);
            }
        });
        //微信支付
        wxPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payString = "{charge_url:\"\",parameters:\"{\"appid\":\"wx4c7e15768818a0fb\",\"partnerid\":\"1220079301\",\"prepayid\":\"wx201803241656559023f4b1cf0766325797\",\"package\":\"Sign=WXPay\",\"timestamp\":1521881815,\"noncestr\":\"5ab612d7b1afb\",\"sign\":\"B739A8678BDFC5172D543D0B752731B5\"}\"}";
                PayManager.getInstance().wxpay(MainActivity.this, new PayParams(payString), payCallback);
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(MainActivity.this.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}