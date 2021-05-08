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

public class SeleccionCuentaAdaptadorItem2 extends RecyclerView.Adapter<SeleccionCuentaAdaptadorItem2.ViewHolder> {
    List<SelectedAccountItem> list2;
    Context ctx;
    private OnClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tipCuenta2;
        Context ctx;
        LinearLayout linearLayout;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            tipCuenta2 = itemView.findViewById(R.id.tituloBtnTipCuenta2);
            linearLayout = itemView.findViewById(R.id.linearTipoCuenta2);
        }
    }

    public SeleccionCuentaAdaptadorItem2(List<SelectedAccountItem> list, Context ctx) {
        this.list2 = list;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_seleccion_cuenta_btn2, null, false);
        return new ViewHolder(view, ctx);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String titulo = list2.get(position).getProductDescription()+ (!list2.get(position).getCurrencyDescription().equals("") ? "\n" + list2.get(position).getCurrencyDescription() : "");
        holder.tipCuenta2.setText(titulo);

        holder.linearLayout.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(view, list2.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list2.size();
    }
}
