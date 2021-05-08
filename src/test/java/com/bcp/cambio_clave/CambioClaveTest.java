package com.bcp.cambio_clave;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.newpos.pay.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CambioClaveTest {

    CambioClave cambioClave = new CambioClave();

    @Mock
    CambioClave cambioClaveMock = mock(CambioClave.class);
    Bundle bundle;
    EditText editText,editTextActual,contra,nuevaContra;
    boolean all;
    String tipoCambio;


    @Before
    public void Init(){
        cambioClave =  new CambioClave();
    }

    @Test
    public void onCreate() {
// Falta completar
 
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                cambioClaveMock.onCreate(bundle);
                verify(cambioClaveMock).onCreate(bundle);
                verify(cambioClaveMock).setContentView(R.layout.cambio_clave);

                verify(cambioClaveMock).findViewById(R.id.title_toolbar);
                verify(cambioClaveMock).findViewById(R.id.title_toolbar);
                verify(cambioClaveMock).findViewById(R.id.contra);

                verify(cambioClaveMock).nuevaContra.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                //modificable
                verify(cambioClaveMock).findViewById(InputType.TYPE_CLASS_NUMBER);
                verify(cambioClaveMock).findViewById(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                if (tipoCambio.equals("CAMBIO CLAVE ACTUAL")) {
                    verify(cambioClaveMock).contra.setEnabled(true);
                }
                return null; //cuando el Metodo es null, devuelve  null7
            }
        }).when(cambioClaveMock).onCreate(bundle);
    }

    @Test
    public void borrarDatosEditText() {
        CambioClave cambioClaveMock = mock(CambioClave.class);
        editText = editTextActual;
        cambioClaveMock.borrarDatosEditText(editText,all);
        verify(cambioClaveMock).borrarDatosEditText(editText,all);
    }
}