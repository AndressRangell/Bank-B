package com.bcp.transactions.giros.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.ClassArguments;
import com.bcp.document.MsgScreenDocument;
import com.bcp.teclado_alfanumerico.ClickKeyboardFragment;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;


public class FragmentPassword extends Fragment {

    ImageView volver;
    TextView title;
    TextView clavesecreta;
    EditText clave;
    ClassArguments arguments;
    RelativeLayout relativeKeyboardNume;
    Button btnContinuar;
    Activity activity;
    ImageView close;
    Context thiscontext;
    View vista;
    String password;
    boolean confpassword = true;
    private String MSGSCREENTIMER = "FragmentPassword";
    private ClickKeyboardFragment keyboards;
    int minEtInputuser = 4;
    Toast toast;

    TransView transView;
    MsgScreenDocument msgScreenDocument;

    public FragmentPassword(){
        //Constructor de la clase
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.thiscontext = context;
        this.activity = getActivity();
        this.transView = (TransView)getActivity();
        this.msgScreenDocument = transView.getMsgScreendocument();
    }

    @Override
    public void onDetach() {
        clearVar();
        super.onDetach();
    }

    private void clearVar() {
        volver = null;
        title = null;
        clavesecreta = null;
        clave = null;
        arguments = null;
        relativeKeyboardNume = null;
        btnContinuar = null;
        activity = null;
        close = null;
        thiscontext = null;
        vista = null;
        password = null;
        confpassword = true;
        keyboards = null;
        minEtInputuser = 0;
        toast = null;

        transView = null;
        msgScreenDocument = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_clave, container, false);

        volver = vista.findViewById(R.id.back);
        title = vista.findViewById(R.id.title_toolbar);
        clave = vista.findViewById(R.id.clavesecreta);
        clavesecreta = vista.findViewById(R.id.clave_secreta);
        relativeKeyboardNume = vista.findViewById(R.id.keyboardNumerico);
        btnContinuar = vista.findViewById(R.id.continuar);
        close = vista.findViewById(R.id.iv_close);

        clavesecreta.setTextColor(getResources().getColor(R.color.colorbutNumCuent));

        close.setVisibility(View.VISIBLE);
        volver.setVisibility(View.VISIBLE);
        return vista;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transView.deleteTimer();

        (transView).counterDownTimer(msgScreenDocument.getTimeOut(), MSGSCREENTIMER);

        toast = new Toast(getContext());

        arguments = (transView).getArguments();

        title.setText(getResources().getString(arguments.getTypetransaction() == 1 ? R.string.datosClaveGiro : R.string.claveGiroSecreta));
        btnContinuar.setText(getResources().getString(arguments.getTypetransaction() == 1 ? R.string.createPasword : R.string.confirmPasword));

        setKeyboard(clave ,8,false,true ,msgScreenDocument.isAlfa(),minEtInputuser,relativeKeyboardNume,null);

        btnContinuar.setOnClickListener(view1 -> {
            if (!longMin(clave)) {
                if(arguments.getTypetransaction() == 1){
                    if(confpassword){
                        btnContinuar.setEnabled(false);
                        password = clave.getText().toString();
                        title.setText(getResources().getString(R.string.confirmClaveGiro));
                        btnContinuar.setText(getResources().getString(R.string.confirmPasword));
                        clave.setText("");
                        setKeyboard(clave ,8,false,true ,(transView).getMsgScreendocument().isAlfa(),minEtInputuser,relativeKeyboardNume,null);
                        confpassword = false;
                    }else {
                        if(password.equals(clave.getText().toString())){

                            arguments.setArgument5(clave.getText().toString());
                            (transView).deleteTimer();
                            (transView).getListener().confirm(InputManager.Style.COMMONINPUT);
                        }else {
                            Toast.makeText(getContext(), getResources().getString(R.string.passwordConfirm), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    ((TransView)activity).deleteTimer();
                    arguments.setArgument5(clave.getText().toString());
                    (transView).setArgumentsClass(arguments);
                    (transView).getListener().confirm(InputManager.Style.COMMONINPUT);
                }
            }
        });

        volver.setOnClickListener(view1 -> {
            if(password != null){
                title.setText(getResources().getString(R.string.datosClaveGiro));
                btnContinuar.setText(getResources().getString(R.string.createPasword));
                password = null;
                confpassword = true;
                if (clave.equals(""))
                    btnContinuar.setEnabled(false);
            }else {
                if(arguments.getTypetransaction() == 1){
                    if(arguments.getTypepayment().equals("2")){
                        NavHostFragment.findNavController(FragmentPassword.this)
                                .navigate(R.id.action_fragmentPassword3_to_fragmentDetailGiro);
                    }else {
                        NavHostFragment.findNavController(FragmentPassword.this)
                                .navigate(R.id.action_fragmentPassword2_to_fragmentDetalleGiro);
                    }
                }else {
                    if(arguments.getTypepayment().equals("1")){
                        NavHostFragment.findNavController(FragmentPassword.this)
                                .navigate(R.id.action_fragmentPassword_to_fragment_datos_beneficiario);
                    }
                }
            }
        });

        close.setOnClickListener(view12 -> {
            (transView).deleteTimer();
            (transView).getListener().cancel(TcodeError.T_USER_CANCEL);
        });
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

    public boolean longMin(EditText etInputuser){
        if (keyboards.getLengthMin() > 0 )
            minEtInputuser = keyboards.getLengthMin();
        if (!etInputuser.getText().toString().equals("")){
            if (etInputuser.length() >= minEtInputuser) {
                toast.cancel();
                return false;
            } else {
                toast(getActivity(), R.drawable.ic_lg_light, getString(R.string.minCaracteres), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
            }
        }else {
            toast(getActivity(), R.drawable.ic_lg_light, getString(R.string.ingrese_dato), Toast.LENGTH_SHORT, new int[]{Gravity.CENTER, 0, 0});
        }
        return true;
    }

    public void toast(Activity activity, int ico, String str, int duration, int[] dirToast) {
        LayoutInflater inflater_3 = activity.getLayoutInflater();
        View view3 = inflater_3.inflate(R.layout.toast_transaparente,
                (ViewGroup) activity.findViewById(R.id.toast_layoutTransaparent));
        toast.setGravity(dirToast[0], dirToast[1], dirToast[2]);
        toast.setDuration(duration);
        toast.setView(view3);
        ((TextView) view3.findViewById(R.id.toastMessageDoc)).setText(str);
        toast.show();
    }
}