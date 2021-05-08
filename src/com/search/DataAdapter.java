package com.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.inicializacion.configuracioncomercio.Companny;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    List<Companny>  mFilteredLis;
    Context mcontext;
    private setOnClickListener listener;

    Fragment fragment;

    public DataAdapter(List<Companny> arrayList, Context mcontexts, Fragment fragment){
        mFilteredLis = arrayList;
        this.mcontext = mcontexts;
        this.fragment = fragment;
    }

    public interface setOnClickListener{
        void onClick(View view, int position,Companny obj);
    }

    public void setOnclickListener(final setOnClickListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row_search, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int position) {
            holder.tvname.setText(mFilteredLis.get(position).getDescription());

            holder.tvname.setOnClickListener(view ->{
                if (listener != null){
                    listener.onClick(view,position,mFilteredLis.get(position));
                }
            });
    }

    @Override
    public int getItemCount() {
        return mFilteredLis.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname = itemView.findViewById(R.id.tv_name);
        }
    }
}
