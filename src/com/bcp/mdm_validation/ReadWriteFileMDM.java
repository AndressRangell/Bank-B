package com.bcp.mdm_validation;

import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import com.newpos.libpay.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReadWriteFileMDM {

    private XmlSerializer serializer;
    private FileOutputStream fileos = null;
    private String reVerse;
    private String seTtle;
    private String initAuto;
    private String trans;

    public static final String REVERSE_ACTIVE = "1";
    public static final String TRANS_ACTIVE = "1";
    public static final String REVERSE_DEACTIVE = "0";
    public static final String TRANS_DEACTIVE = "0";
    public static final String INITAUTOACTIVE = "1";
    public static final String INITAUTODEACTIVE = "0";
    public static final String SETTLE_ACTIVE = "1";
    public static final String SETTLE_DEACTIVE = "0";
    private String msgMDM = "MdmInstall";

    private static ReadWriteFileMDM instance = null;

    protected ReadWriteFileMDM(){

    }

    public static ReadWriteFileMDM getInstance(){
        if (instance == null){
            instance = new ReadWriteFileMDM();
        }
        return instance;
    }
    private String s="/";
    public String getReverse() {
        return reVerse;
    }

    public void setReverse(String reverse) {
        reVerse = reverse;
    }

    public String getSettle() {
        return seTtle;
    }

    public void setSettle(String settle) {
        seTtle = settle;
    }

    public String getInitAuto() {
        return initAuto;
    }

    public void setInitAuto(String initAuto) {
        this.initAuto = initAuto;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public void createXML(String name) {

        serializer = Xml.newSerializer();
        File newxmlfile;
        if(Build.MODEL.equals("NEW9220")) {
            newxmlfile = new File(Environment.getExternalStorageDirectory().getPath() + s + name + ".xml");
        }
        else {
            newxmlfile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()  , name + ".xml");
        }

        try {
             Logger.debug(Logger.TAG + newxmlfile.createNewFile());
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " readEriteFileMdm " + e.getMessage());
        }

        try {

            fileos = new FileOutputStream(newxmlfile);

        } catch (FileNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error "+e.getMessage());
        }
        try {

            serializer.setOutput(fileos, "UTF-8");

        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " readWriteFileMdm 2 " + e.getMessage());
        }
    }

    public void setTag(String tag, String value) {
        if (value != null) {
            try {
                serializer.startTag(null, tag);
                serializer.text(value);
                serializer.endTag(null, tag);
            } catch (IOException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " readWriteFileMdm 3 " + e.getMessage());
            }
        }
    }

    public void writeFileMDM(String reversal, String settle, String init, String isTrans) {

        createXML("ConfigDF");

        try {
            serializer.startTag(null, "Configuration");
            serializer.startTag(null, msgMDM);

            setTag("isReversal", reversal);
            setTag("isSettle", settle);
            setTag("initAuto", init);
            setTag("isTrans",isTrans);

            serializer.endTag(null, msgMDM);
            serializer.endTag(null, "Configuration");

            serializer.endDocument();
            serializer.flush();
            fileos.close();

            readFileMDM();
        } catch (IOException e1) {
            Logger.logLine(Logger.LOG_EXECPTION, e1.getStackTrace(), " readWriteFileMdm 4 " + e1.getMessage());
        }
    }

    private void stateElement(NodeList listElements){
        String[] data = {null,null,null,null};

        for (int b = 0; b < listElements.getLength(); b++) {

            String nameEl = listElements.item(b).getNodeName();
            if (nameEl.equals("isReversal")) {
                data[0] = listElements.item(b).getTextContent();
            }
            if (nameEl.equals("isSettle")) {
                data[1] = listElements.item(b).getTextContent();
            }
            if (nameEl.equals("initAuto")) {
                data[2] = listElements.item(b).getTextContent();
            }
            if (nameEl.equals("isTrans")){
                data[3] = listElements.item(b).getTextContent();
            }
        }
        if (data[0] != null && data[1] != null && data[2] != null && data[3] != null) {
            reVerse=data[0];
            seTtle=data[1];
            initAuto=data[2];
            trans = data[3];
        } else {
            writeFileMDM(REVERSE_DEACTIVE, SETTLE_DEACTIVE,INITAUTODEACTIVE,TRANS_DEACTIVE);
        }
    }

    public boolean readFileMDM() {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),  "/ConfigDF.xml");

        if (file.exists()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(file);
                Element documentElement = document.getDocumentElement();
                NodeList sList = documentElement.getElementsByTagName(msgMDM);

                if (sList != null && sList.getLength() > 0) {
                    for (int i = 0; i < sList.getLength(); i++) {
                        Node node = sList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {

                            node.getAttributes();
                            Element e = (Element) node;
                            NodeList listElements = e.getElementsByTagNameNS(node.getNamespaceURI(), node.getLocalName());

                            //lee cada elemento del state
                            stateElement(listElements);
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " readfilemdm 5 " + e.getMessage());
                createXML("ConfigDF");
                return false;
            }
        } else {
            writeFileMDM(REVERSE_DEACTIVE, SETTLE_DEACTIVE, INITAUTODEACTIVE, TRANS_DEACTIVE);
            return true;
        }

    }

    public boolean isAutoInitActive(){
        return (reVerse.equals(ReadWriteFileMDM.REVERSE_DEACTIVE) &&
                seTtle.equals(ReadWriteFileMDM.SETTLE_DEACTIVE) &&
                initAuto.equals(ReadWriteFileMDM.INITAUTOACTIVE) &&
                trans.equals(ReadWriteFileMDM.TRANS_DEACTIVE));
    }
}
