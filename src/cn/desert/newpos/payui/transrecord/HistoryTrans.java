package cn.desert.newpos.payui.transrecord;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.amount.MsgScreenAmount;
import com.bcp.document.InputInfo;
import com.bcp.historydetail.HistoryDetail;
import com.bcp.menus.seleccion_cuenta.InputSelectAccount;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.bcp.printer.MsgScreenPrinter;
import com.newpos.libpay.device.card.CardInfo;
import com.newpos.libpay.device.pinpad.OfflineRSA;
import com.newpos.libpay.device.pinpad.PinInfo;
import com.newpos.libpay.device.printer.PrintManager;
import com.newpos.libpay.device.scanner.QRCInfo;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.translog.TransLogData;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogWs;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import cn.desert.newpos.payui.UIUtils;
import cn.desert.newpos.payui.base.PayApplication;

public abstract class HistoryTrans extends Activity implements
        HistorylogAdapter.OnItemReprintClick, TransUI, View.OnClickListener {

    ListView lvtrans;
    View viewnodata;
    View viewreprint;
    EditText searchedit;
    ImageView search;
    LinearLayout z;
    LinearLayout root;
    ImageView close;

    private HistorylogAdapter adapter;
    private boolean isSearch = false;
    public static final String EVENTS = "EVENTS";
    public static final String LAST = "LAST";
    public static final String COMMON = "COMMON";
    public static final String ALL = "ALL";
    public static final String ALL_F_ACUM = "ALL_F_ACUM";
    public static final String ALL_F_REDEN = "ALL_F_REDEN";
    private boolean isCommonEvents = false;
    private PrintManager manager = null;

    private ArrayList allData=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        PayApplication.getInstance().addActivity(this);
        close = findViewById(R.id.iv_close);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(this);
        lvtrans = findViewById(R.id.history_lv);
        viewnodata = findViewById(R.id.history_nodata);
        viewreprint = findViewById(R.id.reprint_process);
        searchedit = findViewById(R.id.history_search_edit);
        search = findViewById(R.id.history_search);
        z = findViewById(R.id.history_search_layout);
        root = findViewById(R.id.transaction_details_root);
        adapter = new HistorylogAdapter(this, this);
        lvtrans.setAdapter(adapter);
        viewreprint.setVisibility(View.GONE);
        search.setOnClickListener(new SearchListener());
        manager = PrintManager.getmInstance(this, this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String even = bundle.getString(HistoryTrans.EVENTS);
            switch (Objects.requireNonNull(even)) {
                case LAST:
                    reprint(TransLogWs.getInstance().getLastTransLogDataWs());
                    break;
                case ALL:
                case ALL_F_ACUM:
                case ALL_F_REDEN:
                    printAll();
                    break;
                default:
                    isCommonEvents = true;
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<TransLogDataWs> list = TransLogWs.getInstance().getData();
        List<TransLogDataWs> temp = new ArrayList<>();
        int num = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            temp.add(num, list.get(i));
            num++;
        }
        if (list.isEmpty()) {
            showView(false);
            adapter.setList(temp);
            adapter.notifyDataSetChanged();
            isSearch = true;
            search.setImageResource(android.R.drawable.ic_menu_search);
        } else {
            showView(true);
        }
    }

    private void showView(boolean isShow) {
        if (isShow) {
            lvtrans.setVisibility(View.GONE);
            viewnodata.setVisibility(View.VISIBLE);
        } else {
            lvtrans.setVisibility(View.VISIBLE);
            viewnodata.setVisibility(View.GONE);
        }
    }

    private void star2HistoryDetail(TransLogData transLogData){
                Bundle bundle=new Bundle();
                bundle.putSerializable("Selected", transLogData);
                Intent i = new Intent();
                i.putExtras(bundle);
                i.setClass(this,HistoryDetail.class);
                startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id ) {
        if(position >=allData.size()){
            return;
        }

        TransLogData clickdata = null;
        clickdata = (TransLogData) allData.get(allData.size()-position-1);
        if(clickdata !=null){
            star2HistoryDetail(clickdata);
        }
    }

    private void reprint(final TransLogDataWs data) {
        final ProgressDialog progressDialog = new ProgressDialog(HistoryTrans.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getResources().getString(R.string.trans_result));
        progressDialog.setMessage(getResources().getString(R.string.msg_re_print));
        progressDialog.show();
        new Thread() {
            @Override
            public void run() {
                manager.print(data, false, true);
                mHandler.sendEmptyMessage(0);
                progressDialog.dismiss();
            }
        }.start();


    }

    private void printAll() {
        new Thread() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewreprint.setVisibility(View.GONE);
            lvtrans.setVisibility(View.VISIBLE);
            z.setVisibility(View.VISIBLE);
            if (!isCommonEvents) {
                finish();
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.equals(close)) {
            finish();
        }
    }

    private final class SearchListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (isSearch) {
                String edit = ISOUtil.padleft(searchedit.getText().toString() + "", 6, '0');
                if (!PAYUtils.isNullWithTrim(edit)) {
                    TransLogWs transLogWs = TransLogWs.getInstance();
                    TransLogDataWs data = transLogWs.searchTransLogByTraceNo(edit);
                    if (data != null) {
                        InputMethodManager imm = (InputMethodManager) HistoryTrans.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        Objects.requireNonNull(imm).hideSoftInputFromWindow(searchedit.getWindowToken(), 0);
                        List<TransLogDataWs> list = new ArrayList<>();
                        list.add(0, data);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        search.setImageResource(android.R.drawable.ic_menu_revert);
                        isSearch = false;
                    } else {
                        UIUtils.toast(HistoryTrans.this, R.drawable.ic_lg_light, HistoryTrans.this.getResources().getString(R.string.not_any_record), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
                    }
                }
            } else {
                loadData();
            }
        }
    }

    @Override
    public void handlingBCP(int tittle, int status, boolean imgProgress) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public InputInfo alertView(String transEname, String msg1, String msg2,String msg3) {
        return null;
    }

    @Override
    public void verifyCard(String tittle,  boolean imgProgress) {
        //El metodo heredado de TransUI.class
    }
    @Override
    public void handlingBCP(String title, String status, boolean imgProgress ) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public int showCardConfirm(int timeout, String cn) {
        return 0;
    }

    @Override
    public InputInfo showMessageInfo(String title, String msg, String btnCancel, String btnConfirm, int timeout, boolean isQR) {
        return null;
    }

    @Override
    public InputInfo showMessageImpresion(MsgScreenPrinter msgScreenPrinter) {
        return null;
    }

    @Override
    public int showCardApplist(int timeout,String nameTrans, String[] list) {
        return 0;
    }

    @Override
    public int showMultiLangs(int timeout, String[] langs) {
        return 0;
    }

    @Override
    public void trannSuccess(int code, String... args) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public void showError(String tittle, int errcode) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public void showError(String tittle, String cod, String errcode) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public InputInfo showInputUser(int timeout, String title, String label, int min, int max) {
        return null;
    }

    @Override
    public void toasTrans(int errcode, boolean sound, boolean isErr) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public void toasTrans(String errcode, boolean sound, boolean isErr) {
        //El metodo heredado de TransUI.class
    }

    @Override
    public InputInfo showConfirmAmount(int timeout, String title, String[] label, String amnt, boolean isHTML, float textSize) {
        return null;
    }

    @Override
    public InputInfo getOutsideInput(MsgScreenAmount msgScreenAmount) {
        return null;
    }

    @Override
    public CardInfo getCardUse(String i, String i1, int i2, int i3) {
        return null;
    }

    @Override
    public CardInfo getCardFallback(String msg, int timeout, int mode, String title) {
        return null;
    }

    @Override
    public PinInfo getPinpadOnlinePin(int i, String s, String s1, String tittle) {
        return null;
    }

    @Override
    public PinInfo getPinpadOfflinePin(int i, int i1, OfflineRSA rsaPinKey, int i2) {
        return null;
    }

    @Override
    public InputInfo showSignature(int timeout, String title, String transType) {
        return null;
    }

    @Override
    public InputInfo showList(int timeout, String title, String transType, final ArrayList<String> listMenu, int id) {
        return null;
    }

    @Override
    public InputSelectAccount showSelectAccount(int timeout, String[] data, List<SelectedAccountItem> items, List<SelectedAccountItem> items2) {
        return null;
    }

    @Override
    public QRCInfo getQRCInfo(int i, InputManager.Style style) {
        return null;
    }
}
