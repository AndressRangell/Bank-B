package com.bcp.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.newpos.pay.R;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class WifiAdapter extends BaseAdapter {

    private Context context;
    private List<String> listItems;
    private String redActual;

    public WifiAdapter(Context context, List<String>  listItems, String redActual) {
        this.context = context;
        this.listItems = listItems;
        this.redActual = redActual;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public String getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return showRed(position);
    }

    private View showRed(int position) {
        String valor = getItem(position);

        View view = LayoutInflater.from(context).inflate(R.layout.setting_list_item, null);

        TextView txtNombre = view.findViewById(R.id.setting_listitem_tv);
        ImageView imageViewPrincipal = view.findViewById(R.id.setting_listitem_iv);
        ImageView imageView = view.findViewById(R.id.img_item);

        txtNombre.setHeight(100);
        txtNombre.setGravity(Gravity.CENTER);
        txtNombre.setText( valor );
        imageViewPrincipal.setImageResource(R.drawable.ic_wifi_list);

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if ( valor.equals( redActual.replace("\"","") ) && wifi.isConnected() ){

            imageView.setImageResource(R.drawable.ic_check_icon);
            imageView.getLayoutParams().height = 70;
            imageView.getLayoutParams().width = 70;

            txtNombre.setGravity(Gravity.START);
            txtNombre.setText(Html.fromHtml(valor + "<br>" + "Conectado" ) );
        }
        return view;
    }

}
