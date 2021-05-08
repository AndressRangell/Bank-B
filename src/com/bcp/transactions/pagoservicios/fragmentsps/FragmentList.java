package com.bcp.transactions.pagoservicios.fragmentsps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.inicializacion.configuracioncomercio.Companny;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.Trans;
import com.siderbar.SideBar;
import com.siderbar.SortAdapter;
import com.siderbar.SortModel;

import java.util.ArrayList;
import java.util.List;

import static com.android.newpos.pay.StartAppBCP.checkCompanny;


public class FragmentList extends Fragment {

    ListView sortListView;
    SideBar sideBar;
    TextView dialog;
    SortAdapter adapter;
    ImageView back;
    TextView tittleToolbar;


    private List<SortModel> sourceDateList;
    boolean isNeedChecked;

    Activity activity;
    Context thiscontext;
    View vista;
    ClassArguments classArguments;
    private static final String MSGSCREENTIMER = "FragmentList";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.thiscontext = context;
        this.activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista         = inflater.inflate(R.layout.fragment_list, container, false);

        tittleToolbar = vista.findViewById(R.id.title_toolbar);
        sideBar       = vista.findViewById(R.id.sidrbar);
        dialog        = vista.findViewById(R.id.dialog);
        back          = vista.findViewById(R.id.back);

        sideBar.setTextView(dialog);

        back.setVisibility(View.VISIBLE);
        tittleToolbar.setText(R.string.telefonia_y_Cable);


        return vista;
    }

    @SuppressLint("ClickaViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TransView)activity).deleteTimer();

        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);


        classArguments = ((TransView)activity).getArguments();

        back.setOnClickListener(view13 -> NavHostFragment.findNavController(FragmentList.this)
                .navigate(R.id.action_fragmentList_to_fragmentServiceSelector));
        initViews();

    }

    private void initViews() {
        sideBar.setOnTouchingLetterChangedListener(s -> {
            int position = adapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                sortListView.setSelection(position);
            }
        });

        sortListView = vista.findViewById(R.id.country_lvcountry);

        sortListView.setOnItemClickListener((parent, view, position, id) -> {
            if (!isNeedChecked) {
                classArguments.setObjCompanny(sourceDateList.get(position).getName());
                ((TransView)activity).setArgumentsClass(classArguments);

                NavHostFragment.findNavController(FragmentList.this)
                        .navigate(R.id.action_fragmentList_to_navigationConRango);
            } else {
                sourceDateList.get(position).setChecked(!sourceDateList.get(position).isChecked());
                adapter.notifyDataSetChanged();
            }
        });

        sourceDateList = filledData(checkCompanny.selectCompannyForCategory(getContext(),classArguments.getSelectedAccountItem().getCurrencyCode()));
        adapter = new SortAdapter(thiscontext, sourceDateList);
        sortListView.setAdapter(adapter);

    }

    private List<SortModel> filledData(List<Companny>compannyList){

        String sortString = "";
        List<SortModel> mSortList = new ArrayList<>();

        for (int i = 0; i < compannyList.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(compannyList.get(i));
            String pinyin = compannyList.get(i).getDescription();
            if(pinyin != null)
                sortString = pinyin.substring(0, 1).toUpperCase();
            sortModel.setSortLetters(sortString.toUpperCase());

            mSortList.add(sortModel);
        }
        return mSortList;
    }
}