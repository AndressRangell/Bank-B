package com.pandingdebt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.newpos.pay.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterPandingdebt extends RecyclerView.Adapter<AdapterPandingdebt.ViewHolder> {

    List<String> arraypanding = new ArrayList<>();

    setOnCheckedChaned listener;

    public interface setOnCheckedChaned{
        void OnChackedChanedListiner(int position, View view, Boolean isCheckbox, CheckBox text);
    }

    public void setOnClickListener(setOnCheckedChaned listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterPandingdebt.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pandingdebt, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPandingdebt.ViewHolder holder, int position) {
        holder.checkPandingdebt.setText(String.valueOf(arraypanding.get(position)));

    }

    @Override
    public int getItemCount() {
        if (arraypanding == null){
            return 0;
        }
        return arraypanding.size();
    }

    public void loadItems(List<String> tournaments){
        this.arraypanding = tournaments;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkPandingdebt;
       // TextView textDeudaPendiente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkPandingdebt = itemView.findViewById(R.id.checkpandingdebt);
            checkPandingdebt.setOnClickListener(v -> {
                if (listener != null){
                    int positio = getAdapterPosition();
                    if (positio != RecyclerView.NO_POSITION){
                        listener.OnChackedChanedListiner(positio,v, checkPandingdebt.isChecked(), checkPandingdebt);
                    }
                }
            });
        }

    }
}
