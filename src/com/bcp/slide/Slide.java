package com.bcp.slide;

import android.app.Activity;
import android.content.Context;
import com.android.desert.admanager.ConvenientBanner;
import com.android.desert.admanager.holder.CBViewHolderCreator;
import com.android.desert.admanager.listener.OnItemClickListener;
import com.android.newpos.pay.R;
import com.newpos.libpay.Logger;

public class Slide implements OnItemClickListener {

    private ConvenientBanner adColumn;

    public Slide(Context context, boolean loop){
        adColumn = new ConvenientBanner(context, loop);
    }

    public void galeria(Activity activity, int id){

        String path = activity.getFilesDir() + "/ad";

        FileSlide.readCopy(path, activity);
        adColumn = activity.findViewById(id);
        adColumn.setPages(new CBViewHolderCreator<AdHolder>() {

            @Override
            public AdHolder createHolder() {
                return new AdHolder();
            }

        }, FileSlide.getAds(path + "/")).setPageIndicator(new int[]{R.drawable.dot_normal, R.drawable.dot_focused});

    }

    public void stopSlide(){
        if (adColumn != null){
            this.adColumn.stopTurning();
        }
    }

    public void setTimeoutSlide(int timeout){
        if (timeout >= 0 && adColumn != null){
            this.adColumn.startTurning(timeout);
        }
    }

    @Override
    public void onItemClick(int i) {
        Logger.debug("click******" + i);
    }

    public ConvenientBanner<String> getAdColumn() {
        return adColumn;
    }

    public void setAdColumn(ConvenientBanner<String> adColumn) {
        this.adColumn = adColumn;
    }
}
