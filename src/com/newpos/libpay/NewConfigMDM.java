package com.newpos.libpay;

import android.os.Environment;
import android.util.Log;

import com.bcp.mdm_validation.ReadWriteFileMDM;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewConfigMDM {
    public static NewConfigMDM instance;
    private static int POS_LOG_GENERAL = 0;
    private static int POS_LOG_EXCEPTION = 1;
    private static String logGeneral;
    private static String logException;


    public String getLogGeneral() {
        return logGeneral;
    }

    public String getLogException() {
        return logException;
    }

    /**
     *
     * @return
     *          0 : Exitoso
     *         -1 : Fallido
     *         -2 : No existe archivo
     */
    private static int readFile(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),  "/NewConfigMDM.xml");
        String contenidoLogs = "";
        String elementosLogs []  = new String[1];
        if (file.exists()){
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                Document document = documentBuilder.parse(file);
                document.getDocumentElement().normalize();
                NodeList listaLogs = document.getElementsByTagName("Configuration");
                for (int temp = 0; temp < listaLogs.getLength(); temp++) {
                    Node nodo = listaLogs.item(temp);
                    if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) nodo;
                        contenidoLogs = element.getElementsByTagName("Logs").item(0).getTextContent();
                        elementosLogs = contenidoLogs.split(",");

                        if(elementosLogs[POS_LOG_GENERAL]!=null){
                            logGeneral = elementosLogs[POS_LOG_GENERAL];

                            if(elementosLogs[POS_LOG_EXCEPTION]!=null){
                                logException = elementosLogs[POS_LOG_EXCEPTION];
                                return 0;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
            return -1;
        }else {
            Log.e("Exception", "No se encontró el archivo");
            return -2;
        }
    }

    public static NewConfigMDM getInstanceNewConfigMDM(){
        if (instance == null){
            instance = new NewConfigMDM();
        }
        readFile();
        return instance;
    }
}
