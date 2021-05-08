package com.bcp.historydetail;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.newpos.pay.R;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.printer.PrintRes;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.trans.translog.TransLogData;
import com.newpos.libpay.utils.PAYUtils;
import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.base.BaseActivity;

public class HistoryDetail extends BaseActivity {

    TextView cardno;
    TextView date;
    TextView merchantno;
    TextView refno;
    TextView terminalno;
    TextView tranctype;
    TextView voucherno;
    TextView head;
    TextView amount;
    TextView authno;
    Button detailButton;

    TransLogData clickData;

    private static final int MSG_ERROR_MSG = 0x01;
    private static final int MSG_PRINT_OK = 0x02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail);

        cardno = findViewById(R.id.detail_cardno);
        date = findViewById(R.id.detail_date);

        merchantno = findViewById(R.id.detail_merchantno);
        terminalno = findViewById(R.id.detail_terminalno);
        tranctype = findViewById(R.id.detail_tranctype);
        voucherno = findViewById(R.id.detail_voucherno);
        head = findViewById(R.id.detail_head);
        amount = findViewById(R.id.detail_amount);
        authno = findViewById(R.id.detail_authno);
        refno = findViewById(R.id.detail_refno);
        detailButton = findViewById(R.id.detail_button);

        setNaviTitle(UIUtils.getStringByInt(this,R.string.trans_detail),
                this.getResources().getColor(R.color.base_blue));
        setReturnVisible(View.VISIBLE);
        setRightVisiblity(View.GONE);

        transClick();

        detailButton.setOnClickListener(v -> new Thread(() -> print(clickData)).start());
    }

    private void transClick(){
        TMConfig cfg = TMConfig.getInstance();
        clickData = (TransLogData)getIntent().getSerializableExtra(getResources().getString(R.string.seledmsg));
        if(clickData != null){

            head.setText(PrintRes.CH.AMOUNT);

            if(clickData.getAmount() != 0 && clickData.getAmount() != null){
                amount.setText( PrintRes.CH.RMB+" "+ PAYUtils.getStrAmount(clickData.getAmount()));
            } else {
                amount.setVisibility(View.INVISIBLE);
            }

            if (!PAYUtils.isNullWithTrim(clickData.getPan())) {
                StringBuilder temp = new StringBuilder();
                temp.append(clickData.getMode() == ServiceEntryMode.QRC ? PrintRes.CH.SCANCODE : PrintRes.CH.CARD_NO);
                temp.append(" ");
                temp.append(clickData.getPan());
                cardno.setText(temp.toString());
            } else{
                cardno.setVisibility(View.INVISIBLE);
            }

            String timeStr = PAYUtils.stringPattern(clickData.getLocalDate() + clickData.getLocalTime(), getResources().getString(R.string.msgDMH), getResources().getString(R.string.msgyymmhh));
            date.setText(PrintRes.CH.DATE_TIME+ " " + timeStr);

            fillTextView(PrintRes.CH.VOUCHER_NO,clickData.getTraceNo(),voucherno);

            String type = PAYUtils.formatTranstype(clickData.getEName());
            fillTextView(PrintRes.CH.TRANS_TYPE,type,tranctype);

            merchantno.setText(PrintRes.CH.TERNIMAL_ID + " " + cfg.getMerchID());
            terminalno.setText(PrintRes.CH.MERCHANT_ID + " " + cfg.getTermID());

            fillTextView(PrintRes.CH.REF_NO,clickData.getRrn(),refno);
            fillTextView(PrintRes.CH.AUTH_NO,clickData.getAuthCode(),authno);
        }
    }

    private void fillTextView(String tittle, String data, TextView textView){
        StringBuilder content = new StringBuilder();
        if(!PAYUtils.isNullWithTrim(data)){
            content.append(tittle);
            content.append(" ");
            content.append(data);
            textView.setText(content.toString());
        }else{
            textView.setVisibility(View.INVISIBLE);
        }
    }

    private void print(TransLogData clickData) {
        Logger.debug("click" + clickData);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MSG_ERROR_MSG:
                    showError((int)msg.obj);
                    HistoryDetail.this.finish();
                    break;
                case MSG_PRINT_OK:
                    HistoryDetail.this.finish();
                    break;
                default:
                    break;
            }
        }

        private void showError(int obj) {
            Logger.debug("error" + obj);
        }
    };

    private void setNaviTitle(String stringByInt, int color) {
        Logger.debug("naviTittle" + stringByInt + getResources().getString(R.string.msgColor) + color);
    }
}
