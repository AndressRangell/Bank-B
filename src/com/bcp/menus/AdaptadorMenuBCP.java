package com.bcp.menus;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.newpos.pay.R;
import java.util.List;

public class AdaptadorMenuBCP extends RecyclerView.Adapter<AdaptadorMenuBCP.ViewHolderBotones>{

    List<BotonMenuBCP> list;
    Context ctx;
    private View.OnClickListener listener;
    private long mLastClickTime = 0;

    public AdaptadorMenuBCP() {
        super();
    }

    public void setOnClickListener(View.OnClickListener listener)  {
        this.listener = listener;
    }


    public class ViewHolderBotones extends RecyclerView.ViewHolder {

        TextView titulo;
        LinearLayout linearLayoutFondo;
        ImageView imageView;
        Context ctx;

        public ViewHolderBotones(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            titulo = itemView.findViewById(R.id.title);
            linearLayoutFondo = itemView.findViewById(R.id.botonMenuBlanco);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public AdaptadorMenuBCP(List<BotonMenuBCP> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderBotones holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public ViewHolderBotones onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_bcp_item_btn, null, false);

        view.setOnClickListener(view2 -> {
            if (listener != null) {
               if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                   return;
               mLastClickTime = SystemClock.elapsedRealtime();
               listener.onClick(view);
            }
        });

        return new ViewHolderBotones(view, ctx);
    }

    @Override
    public void onBindViewHolder(ViewHolderBotones holder, int position) {
        holder.titulo.setText(list.get(position).getTitulo());
        holder.linearLayoutFondo.setBackground(list.get(position).getFondo());
        holder.titulo.setTextColor(list.get(position).getTextColor());
        holder.imageView.setBackground(list.get(position).getImageView());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
