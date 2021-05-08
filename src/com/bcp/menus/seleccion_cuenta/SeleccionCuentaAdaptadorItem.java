package com.bcp.menus.seleccion_cuenta;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.newpos.pay.R;
import java.util.List;

public class SeleccionCuentaAdaptadorItem extends RecyclerView.Adapter<SeleccionCuentaAdaptadorItem.ViewHolder> {
    List<SelectedAccountItem> list;
    Context ctx;
    private OnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tipCuenta;
        Context ctx;
        LinearLayout linearLayout;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            tipCuenta = itemView.findViewById(R.id.tituloBtnTipCuenta);
            linearLayout = itemView.findViewById(R.id.linearTipoCuenta);
        }
    }

    public SeleccionCuentaAdaptadorItem(List<SelectedAccountItem> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    public interface OnClickListener {
        void onClick(View view, SelectedAccountItem obj, int position);
    }

    public void setOnClickListener(final OnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_seleccion_cuenta_btn, null, false);
        return new ViewHolder(view, ctx);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String titulo = list.get(position).getProductDescription()+ (!list.get(position).getCurrencyDescription().equals("") ? "\n" + list.get(position).getCurrencyDescription() : "");
        holder.tipCuenta.setText(titulo);


        holder.linearLayout.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(view, list.get(position), position);
                listener = null;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
