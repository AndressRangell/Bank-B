package com.bcp.transactions.callbacks;

import com.bcp.inicializacion.configuracioncomercio.URL;
import java.util.List;
import java.util.Map;

public interface WaitTransFragment {
    //obtener el sessionId del primer api para llenar el header
    void getSessionId(String idSession);
    //devolver el header actualizado en los fragments
    Map<String,String> headerTrans(boolean isSession);
    //incrementar el consecutivo de tx por consumo de api
    void setOpnNumber();
    //realizar consulta a la bd y traer la lista de url
    List<URL> getListUrl(String nameTrans);
    //funcion para manejo de montos por tx
    String[] getAmount();
}
