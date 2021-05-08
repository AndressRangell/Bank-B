package com.bcp.reportes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.newpos.pay.R;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.utils.PAYUtils;
import java.util.List;

import cn.desert.newpos.payui.UIUtils;

public class AdapterDetailReport extends BaseAdapter {

    private TextView date;
    private TextView time;
    private TextView traceNo;
    private TextView symbol;
    private TextView amount;

    protected Activity activity;
    protected List<TransLogDataWs> items;

    public AdapterDetailReport (Activity activity, List<TransLogDataWs> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<TransLogDataWs> category) {
        items.addAll(category);
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.list_item, null);
        }

        instanceElements(v);

        fillElements(position);

        return v;
    }

    private void instanceElements(View view){
        date = view.findViewById(R.id.txtDate);
        time = view.findViewById(R.id.txtHour);
        traceNo = view.findViewById(R.id.txtTrace);
        symbol = view.findViewById(R.id.txtSymbol);
        amount = view.findViewById(R.id.txtAmount);
    }

    private void fillElements(int position){
        date.setText(items.get(position).getTransactionDate() != null ? PAYUtils.stringPattern(items.get(position).getTransactionDate(), "dd/MM/yy", "dd/MM") : "");
        time.setText(items.get(position).getTransactionTime() != null ? items.get(position).getTransactionTime() : "");
        traceNo.setText(items.get(position).getTransactionNumber() != null ? items.get(position).getTransactionNumber() : "");
        String amountTrans;
        String symbolTrans = "S/";
        amountTrans = items.get(position).getAmount() != null ? UIUtils.formatMiles(items.get(position).getAmount().replaceAll("[,.]","")) : "";

        if (items.get(position).getNameTrans().equals(Trans.GIROS) || items.get(position).getNameTrans().equals(Trans.PAGOSERVICIOS)){
            if (items.get(position).getEntryMode().equals("5")){
                symbolTrans = items.get(position).getTotalAmountCurrency();
            }
            amountTrans = items.get(position).getTotalAmount() != null ? UIUtils.formatMiles(items.get(position).getTotalAmount().replaceAll("[,.]","")) : "";
        }
        symbol.setText(symbolTrans);
        amount.setText(amountTrans);
    }


}
