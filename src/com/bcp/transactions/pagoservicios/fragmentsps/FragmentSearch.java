package com.bcp.transactions.pagoservicios.fragmentsps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.inicializacion.configuracioncomercio.Companny;
import com.bcp.teclado_alfanumerico.ClickKeyboardFragment;
import com.google.errorprone.annotations.Var;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.search.DataAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.android.newpos.pay.StartAppBCP.checkCompanny;


public class FragmentSearch extends Fragment {

    EditText searchView;
    TextView textTittle;
    Button cancelar;
    RecyclerView mRecyclerView;
    RelativeLayout relCancelar;
    DataAdapter mAdapter;
    List<Companny> list = new ArrayList<>();
    List<Companny> listfile = new ArrayList<>();
    Activity activity;
    ClassArguments classArguments;
    Companny selectedAccountItem;

    private ClickKeyboardFragment keyboards;
    private String MSGSCREENTIMER = "FragmentSearch";

    File file;

    Context thiscontext;
    View vista;

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
        vista = inflater.inflate(R.layout.fragment_search, container, false);

        textTittle      = vista.findViewById(R.id.texttittle);
        searchView      = vista.findViewById(R.id.search);
        mRecyclerView   = vista.findViewById(R.id.recycleview);
        relCancelar     = vista.findViewById(R.id.relativecancel);
        cancelar        = vista.findViewById(R.id.cancel);
        file            = new File(thiscontext.getFilesDir(), "myfile.txt");

        relCancelar.setVisibility(View.VISIBLE);

         mAdapter = new DataAdapter(list,thiscontext,FragmentSearch.this);
        return vista;
    }


    @SuppressLint("ClickabViewAccessibility")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TransView)activity).deleteTimer();

        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);

        classArguments = ((TransView)activity).getArguments();

        initView();
        fillList();
        readFile();

        cancelar.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentSearch.this)
                .navigate(R.id.action_fragmentSearch_to_fragmentServiceSelector));

        setKeyboard(searchView, 120,false,false, true, 1, null, vista.findViewById(R.id.keyboardAlfa));
    }

    /*
     *se crea nuevo click keyboard para el manejo del teclado solo para fragments con el envio del view y no la actividad
     */
    public void setKeyboard(EditText etTxt, int lenMax, boolean activityAmount, boolean activityPwd, boolean isAlfa, int minLen, RelativeLayout keyboardNumeric , RelativeLayout keyboardAlfa){
        keyboards = new ClickKeyboardFragment(etTxt, vista, isAlfa,((TransView)activity).getTimer(),keyboardNumeric,keyboardAlfa );
        keyboards.setActivityPwd(activityPwd);
        keyboards.setActivityMonto(activityAmount);
        keyboards.setLengthMax(lenMax,minLen);
    }

    private void initView() {
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(thiscontext);
        mAdapter = new DataAdapter(list, vista.getContext(),FragmentSearch.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void fillList() {
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
                textTittle.setText(R.string.resultado);
                if (s.toString().equals("")){
                    readFile();
                    textTittle.setText(R.string.busqueda_reciente);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filter(String text) {
        List<Companny> mAdList = checkCompanny.selectCompannyForCategory(getContext(),null);

        list.clear();
        for (Companny index : mAdList){
            if (index.getDescription().toLowerCase().contains(text.toLowerCase())){
                list.add(index);
            }
        }
        mAdapter = new DataAdapter(list, activity,FragmentSearch.this);
        mRecyclerView.setAdapter(mAdapter);
        clickAdater();
    }

    private void readFile() {
        String[] files = thiscontext.fileList();

        listfile.clear();
        if (ExistsFile(files, "myfile.txt")){
            try {
                InputStreamReader archive = new InputStreamReader(thiscontext.openFileInput("myfile.txt"));
                BufferedReader br = new BufferedReader(archive);
                String linea = br.readLine();
                while (linea != null){
                    String[] content = linea.split(";");
                    Companny obj = new Companny();
                    if (content.length > 0){
                        obj.setIdCompanny(content[0]);
                        obj.setIdCategory(content[1]);
                        obj.setDescription(content[2]);
                    }
                    listfile.add(obj);
                    linea = br.readLine();
                }
                br.close();
                archive.close();
                mAdapter = new DataAdapter(listfile, activity,FragmentSearch.this);
                mRecyclerView.setAdapter(mAdapter);
                clickAdater();
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        }else {
            System.out.println("Archivo no existe");
        }
    }

    private boolean ExistsFile(String[] files, String searchFile) {
        for (String s : files)
            if (searchFile.equals(s))
                return true;
        return false;
    }

    public void writeFile(Companny stringArrayList){

        String all = "";
        try {
            String[] files = thiscontext.fileList();
            if (ExistsFile(files, "myfile.txt")){
                try {
                    InputStreamReader archive = new InputStreamReader(thiscontext.openFileInput("myfile.txt"));
                    BufferedReader br = new BufferedReader(archive);
                    String linea = br.readLine();
                    while (linea != null){
                        all = all + linea + "\n";
                        linea = br.readLine();
                    }
                    br.close();
                    archive.close();
                } catch (Exception e) {

                }
            }

            OutputStreamWriter archive = new OutputStreamWriter(thiscontext.openFileOutput("myfile.txt", Activity.MODE_PRIVATE));
            archive.write(all);
            if (!all.contains(stringArrayList.getDescription()))
                archive.write(stringArrayList.getIdCompanny() + ";" + stringArrayList.getIdCategory() + ";" + stringArrayList.getDescription());
            archive.flush();
            archive.close();
            searchView.setText("");
        } catch (Exception e) {
            System.out.println("Error al guardar" + e.getMessage());
        }
    }

    private  void clickAdater(){
        mAdapter.setOnclickListener((view,position,obj)->{
            writeFile(obj);
            selectedAccountItem = obj;
            classArguments.setObjCompanny(selectedAccountItem);
            ((TransView)activity).setArgumentsClass(classArguments);
            NavHostFragment.findNavController(FragmentSearch.this)
                    .navigate(R.id.action_fragmentSearch_to_navigationConRango);
        });
    }
}