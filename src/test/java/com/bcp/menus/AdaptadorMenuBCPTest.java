package com.bcp.menus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.newpos.pay.R;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class AdaptadorMenuBCPTest {


    private  View.OnClickListener listener;
    private  View view;
    int  viewType;
    Context ctx;
    AdaptadorMenuBCP adaptadorMenuBCP = new AdaptadorMenuBCP();

    @Mock
    AdaptadorMenuBCP.ViewHolderBotones holder;
    int position;
    List<BotonMenuBCP> list;
    View itemView;
    ViewGroup parent;

    @Before
    public void Init() {

         adaptadorMenuBCP = new AdaptadorMenuBCP();
    }

    @Test
    public void setOnClickListener() {
        adaptadorMenuBCP.setOnClickListener(listener);
        this.listener = listener;
        assertEquals(listener,listener);
    }

    @Test
    public void onCreateViewHolder() {

        AdaptadorMenuBCP adaptadorMenuBCP = mock(AdaptadorMenuBCP.class);
        adaptadorMenuBCP.onCreateViewHolder(parent,viewType);
        verify(adaptadorMenuBCP).onCreateViewHolder(parent,viewType);
        //verify(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_bcp_item_btn,null,false));
        //verify(itemView);

    }

    @Test
    public void onBindViewHolder() {
        AdaptadorMenuBCP adaptadorMenuBCP = mock(AdaptadorMenuBCP.class);
        adaptadorMenuBCP.onBindViewHolder(holder,position);
        verify(adaptadorMenuBCP).onBindViewHolder(holder,position);
    }

    @Test
    public void getItemCount() {
        
    }
}