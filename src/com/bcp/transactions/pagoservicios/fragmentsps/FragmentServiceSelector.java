package com.bcp.transactions.pagoservicios.fragmentsps;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.inicializacion.configuracioncomercio.Category;
import com.bcp.inicializacion.configuracioncomercio.Companny;
import com.bcp.menus.seleccion_cuenta.SeleccionCuentaAdaptadorItem;
import com.bcp.menus.seleccion_cuenta.SeleccionServicio;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.newpos.libpay.Logger;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.search.DataAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;


public class FragmentServiceSelector extends Fragment {

    View vista;
    EditText search;
    TextView tittle;
    ImageView volver;
    ImageView close;
    //ACTIVITY Y CONTEX
    Activity activity;
    Context thisContext;
    RecyclerView rvServicios;
    RecyclerView rvCategorias;
    SelectedAccountItem accountServicios;
    ClassArguments classArguments;
    List<SelectedAccountItem> arrayServicios = new ArrayList<>();
    List<SelectedAccountItem> arrayCategoria = new ArrayList<>();
    private static final String MSGSCREENTIMER = "FragmentServiceSelector";


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.thisContext = context;
        this.activity = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       vista = inflater.inflate(R.layout.fragment_service_selector, container, false);

        rvServicios = vista.findViewById(R.id.rvServicios);
        rvCategorias = vista.findViewById(R.id.recyCategorias);
        search = vista.findViewById(R.id.search);
        tittle  = vista.findViewById(R.id.title_toolbar);
        volver = vista.findViewById(R.id.back);
        close = vista.findViewById(R.id.iv_close);

        volver.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        tittle.setVisibility(View.VISIBLE);
        tittle.setText(getResources().getString(R.string.titleTypeService));
       return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TransView)activity).deleteTimer();

        ((TransView)activity).counterDownTimer(((TransView)activity).getMsgScreendocument().getTimeOut(), MSGSCREENTIMER);

        classArguments = ((TransView)activity).getArguments();


        search.setOnClickListener(v -> NavHostFragment.findNavController(FragmentServiceSelector.this)
                .navigate(R.id.action_fragmentServiceSelector_to_fragmentSearch2));

        volver.setOnClickListener(view13 -> NavHostFragment.findNavController(FragmentServiceSelector.this)
                .navigate(R.id.action_fragmentServiceSelector_to_fragmentTypePayment2));

        close.setOnClickListener(view14 -> {
            ((TransView)activity).deleteTimer();
            ((TransView) activity).getListener().cancel(TcodeError.T_USER_CANCEL);
        });

        drawMenuHardcode(readFile());
        drawMenuCategoria(polarisUtil.getListCategory());

    }

    public void drawMenuHardcode(List<Companny> args) {

        if(args == null )
            return;

        for (int i = 0; i < args.size(); i++) {

            SelectedAccountItem selectedItem = new SelectedAccountItem();
            selectedItem.setCurrencyCode(args.get(i).getIdCategory());
            selectedItem.setCurrencyDescription(args.get(i).getIdCompanny());
            selectedItem.setFamilyCode("");
            selectedItem.setProductDescription(args.get(i).getDescription());
            arrayServicios.add(selectedItem);
        }

        rvServicios.setLayoutManager(new LinearLayoutManager(thisContext, LinearLayoutManager.HORIZONTAL, false));
        SeleccionServicio cuentaAdaptadorItem = new SeleccionServicio(arrayServicios, getContext());
        rvServicios.setAdapter(cuentaAdaptadorItem);

        cuentaAdaptadorItem.setOnClickListener((view, obj, position) -> {
            accountServicios = obj;

            Companny selectCompanny = new Companny();
            selectCompanny.setIdCompanny(accountServicios.getCurrencyDescription());
            selectCompanny.setDescription(accountServicios.getProductDescription());
            selectCompanny.setIdCategory(accountServicios.getCurrencyCode());

            classArguments.setObjCompanny(selectCompanny);

            NavHostFragment.findNavController(FragmentServiceSelector.this)
                    .navigate(R.id.action_fragmentServiceSelector_to_navigationConRango);

        });
    }

    public void drawMenuCategoria(List<Category>args) {

        for (int i = 0; i < args.size(); i++) {

            SelectedAccountItem selectedItemCateg = new SelectedAccountItem();
            selectedItemCateg.setCurrencyCode(args.get(i).getIdCategory());
            selectedItemCateg.setCurrencyDescription("");
            selectedItemCateg.setFamilyCode("");
            selectedItemCateg.setProductDescription(args.get(i).getDescription());
            arrayCategoria.add(selectedItemCateg);
        }

        rvCategorias.setLayoutManager(new GridLayoutManager(getContext(), 2));
        SeleccionCuentaAdaptadorItem cuentaAdaptadorItem = new SeleccionCuentaAdaptadorItem(arrayCategoria, getContext());
        rvCategorias.setAdapter(cuentaAdaptadorItem);

        cuentaAdaptadorItem.setOnClickListener((view, obj, position) ->{
            accountServicios = obj;

            classArguments.setSelectedAccountItem(accountServicios);
            ((TransView)activity).setArgumentsClass(classArguments);

            NavHostFragment.findNavController(FragmentServiceSelector.this)
                    .navigate(R.id.action_fragmentServiceSelector_to_fragmentList2);

        });
    }

    private List<Companny> readFile() {
        String[] files = getContext().fileList();
        List<Companny> listfile = new ArrayList<>();

        if (ExistsFile(files, "myfile.txt")){
            try {
                InputStreamReader archive = new InputStreamReader(getContext().openFileInput("myfile.txt"));
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
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        }else {
            System.out.println("Archivo no existe");
        }
        return listfile;
    }

    private boolean ExistsFile(String[] files, String searchFile) {
        for (String s : files)
            if (searchFile.equals(s))
                return true;
        return false;
    }
}