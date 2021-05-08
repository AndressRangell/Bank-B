package com.siderbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.android.newpos.pay.R;
import com.bcp.inicializacion.configuracioncomercio.Companny;
import com.search.DataAdapter;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list;
    List<Companny>  mFilteredLis;
    private Context mContext;
    private DataAdapter.setOnClickListener listener;

    public SortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public interface setOnClickListener{
        void onClick(View view, int position, Companny obj);
    }

    public void setOnclickListener(final DataAdapter.setOnClickListener listener){
        this.listener = listener;
    }

    public void updateListView(List<SortModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_sidebar, null);
            viewHolder.tvTitle  =  view.findViewById(R.id.tv_user_item_name);
            viewHolder.tvLetter =  view.findViewById(R.id.catalog);
            viewHolder.liena    =  view.findViewById(R.id.linear);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);

        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
            viewHolder.liena.setBackgroundResource(R.drawable.boton_linea_sidebar);
        } else {
            viewHolder.liena.setBackgroundResource(R.color.zxing_transparent);
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        SortModel model = list.get(position);

        viewHolder.tvTitle.setText(model.getName().getDescription());
       
        return view;

    }

    final static class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        LinearLayout liena;

    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
