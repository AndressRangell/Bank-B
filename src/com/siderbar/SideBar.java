package com.siderbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.newpos.pay.R;

import java.util.LinkedHashMap;
import java.util.Map;

public class SideBar extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public String[] alphabett = getResources().getStringArray(R.array.alphabet);
    public String[] telephonyy = getResources().getStringArray(R.array.telephony);
    private int choose = -1;
    private Paint paint = new Paint();

    Map<String, Integer> mapIndex;
    Map<String, Integer> mapListIndex;

    float xPosGeneral;
    float yPosGeneral;

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    @SuppressLint("ResourceAsColor")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / alphabett.length;

        mapListIndex = new LinkedHashMap<>();

        for (int i = 0; i < telephonyy.length; i++) {
            String index = telephonyy[i].substring(0, 1);

            if (mapListIndex.get(index) == null)
                mapListIndex.put(index, i);
        }

        mapIndex = new LinkedHashMap<>();
        for (int i = 0; i < alphabett.length; i++){
            String index = alphabett[i];

            if (mapIndex.get(index)==null){
                mapIndex.put(index, i);
            }
        }

        String[] inndexlist1 = mapListIndex.keySet().toArray(new String[0]);
        String[] indexlist = mapIndex.keySet().toArray(new String[0]);

        int i = 0;
        for (String index1 : indexlist) {
            paint.setAntiAlias(true);
            paint.setTextSize(22);
            paint.setColor(R.color.color_text);
            paint.setFakeBoldText(true);
            paint.setTypeface(Typeface.create("font", Typeface.BOLD));
            float xPos = width / 2 - paint.measureText(index1) / 2;
            float yPos = singleHeight * i + singleHeight;
            for (String index : inndexlist1){
                if (index.equals(index1)){
                    paint.setColor(Color.BLACK);
                }
            }
            canvas.drawText(index1, xPos, yPos, paint);
            if (index1.contains("A") && i == 0){
                xPosGeneral = xPos;
                yPosGeneral = yPos;
            }
            paint.reset();
            i++;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;

        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * alphabett.length);
        final int yPos = (int) (y + (yPosGeneral) + xPosGeneral - 5);
        final int xPos = (int) (xPosGeneral + 590);


        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < alphabett.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(alphabett[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(alphabett[c]);
                            mTextDialog.setX(xPos);
                            mTextDialog.setY(yPos);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
}
