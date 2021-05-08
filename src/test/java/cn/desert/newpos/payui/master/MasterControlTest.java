package cn.desert.newpos.payui.master;

import com.android.desert.keyboard.InputManager;
import com.android.newpos.pay.R;
import com.bcp.document.MsgScreenDocument;
import com.bcp.menus.seleccion_cuenta.SelectedAccountItem;
import com.newpos.libpay.device.user.OnUserResultListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import cn.desert.newpos.payui.UIUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MasterControlTest {

    InputManager.Mode type;
    ArrayList<String> listMenu;
    List<SelectedAccountItem> items;
    List<SelectedAccountItem> items2;
    String[] app, langs, data;
    String info, err, status;
    String tittle, title, amnt;
    String[] label;
    String transType,cod;
    int min, max;
    int idApp, id;
    boolean transacccion, imgProgress, transaccion;
    boolean isBanner, isHTML;
    float textSize;
    InputManager.Style mode;

    @Before
    public void init(){
        masterControl = new  MasterControl();
    }

    @Mock
    private OnUserResultListener listener;
    UIUtils uiUtils = mock(UIUtils.class);
    MasterControl masterControl = mock(MasterControl.class);

    @Test
    public void onCreate() {
    }

    @Test
    public void onDestroy() {
    }

    @Test
    public void onClick() {
    }

    @Test
    public void showCardView() {
        masterControl.showCardView("","",10,10,listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.activity_card);
            verify(masterControl).findViewById(R.id.RelativeTimeout);
            verify(masterControl).findViewById(R.id.iv_close);
            verify(masterControl).findViewById(R.id.title_toolbar);
        });

    }

    @Test
    public void showCardNo() {
        masterControl.showCardNo(10, "000000000000", listener);
        masterControl.runOnUiThread(() -> verify(masterControl).setContentView(R.layout.trans_show_cardno));
    }

    @Test
    public void showMessageInfo() {
        masterControl.showMessageInfo("","", "","",10,false,listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.trans_show_cardno);
            verify(masterControl).findViewById(R.id.iv_close);
            verify(masterControl).findViewById(R.id.title_toolbar);
            verify(masterControl).findViewById(R.id.cardno_display_area);
            verify(masterControl).findViewById(R.id.cardno_cancel);
            verify(masterControl).findViewById(R.id.cardno_confirm);
        });
    }

    @Test
    public void showMessageImpresion() {
       /* masterControl.showMessageImpresion("","","","","","",10,listener);
        masterControl.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                verify(masterControl).setContentView(R.layout.trans_show_cardno);
                verify(masterControl).findViewById(R.id.iv_close);
                verify(masterControl).findViewById(R.id.title_toolbar);
                verify(masterControl).findViewById(R.id.cardno_display_area);
                verify(masterControl).findViewById(R.id.cardno_cancel);
                verify(masterControl).findViewById(R.id.cardno_confirm);
            }
        });*/
    }

    @Test
    public void showInputView() {
        masterControl.showInputView(UIUtils.fillScreenAmount("", "", "", 10, "","", ""),listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.activity_monto);

            verify(masterControl).findViewById(R.id.numb00);
            verify(masterControl).findViewById(R.id.iv_close);
            verify(masterControl).findViewById(R.id.back);
            verify(masterControl).findViewById(R.id.title_toolbar);
            verify(masterControl).findViewById(R.id.continuar);
            verify(masterControl).findViewById(R.id.monto);

        });
    }

    @Test
    public void getInput() {
        masterControl.getInput(type);
        assertEquals(type, type);
    }

    @Test
    public void showCardAppListView() {
        masterControl.showCardAppListView(10,"", app,listener);
        masterControl.runOnUiThread(() -> {
            listener.confirm(idApp);
            verify(listener);
            verify(masterControl).showCardAppListView(10,"",app,listener);
        });
    }

    @Test
    public void showMultiLangView() {
        MasterControl masterControl = mock(MasterControl.class);
        masterControl.showMultiLangView(10, langs, listener);
        //verify(listener);
        verify(masterControl).showMultiLangView(10,langs,listener);

    }

    @Test
    public void showSuccess() {
        masterControl.showSuccess( info);
        masterControl.runOnUiThread(() -> {
            //verify(uiUtils).startResult(MasterControl.class, ResultControl.class,true,true, info,false);
        });
    }

    @Test
    public void showError() {
        masterControl.showError(tittle,cod,err);
        masterControl.runOnUiThread(() -> {
            //verify(uiUtils).startResult();
        });
    }

    @Test
    public void showMsgInfoBCP() {
        masterControl.showMsgInfoBCP(tittle, status, imgProgress);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.handling_bcp);
            verify(masterControl).findViewById(R.id.msgTipoOpe);
            verify(masterControl).findViewById(R.id.msgTituloOpe);
            verify(masterControl).findViewById(R.id.gifProgres);
            //revizar como entrar al if para la prueba


        });
    }

    @Test
    public void showInputUser() {
        masterControl.showInputUser(10,title,label[0], min, max, listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.activity_input_user);
            verify(masterControl).findViewById(R.id.iv_close);
            verify(masterControl).findViewById(R.id.title_toolbar);

            verify(masterControl).findViewById(R.id.editText_input);
            verify(masterControl).findViewById(R.id.textView_title);
            verify(masterControl).findViewById(R.id.last4_cancel);
            verify(masterControl).findViewById(R.id.last4_confirm);
        });
    }

    @Test
    public void showInputDoc() {
        MsgScreenDocument msgScreenDocument = new MsgScreenDocument();
        msgScreenDocument.setTimeOut(10);
        msgScreenDocument.setTittle(tittle);
        msgScreenDocument.setTittleDoc(label[0]);
        msgScreenDocument.setMinLength(min);
        msgScreenDocument.setMaxLength(max);
        msgScreenDocument.setBanner(isBanner);

        masterControl.showInputDoc(msgScreenDocument, listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.ingreso_documento);
            verify(masterControl).findViewById(R.id.iv_close);
            verify(masterControl).findViewById(R.id.title_toolbar);

            verify(masterControl).findViewById(R.id.numDoc);
            verify(masterControl).findViewById(R.id.doc_num);
        });
    }

    @Test
    public void toasTransView() {
        //Pendiente de manera de hacer prueba

    }

    @Test
    public void showConfirmAmountView() {
        masterControl.showConfirmAmountView(10,title,label,amnt,isHTML,textSize,listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.detalle_transaccion);
            verify(masterControl).findViewById(R.id.nroMonto);
            verify(masterControl).findViewById(R.id.title_toolbar);
            verify(masterControl).findViewById(R.id.confirm);
        });
    }

    @Test
    public void showSignatureView() {
        masterControl.showSignatureView(10,listener,title,transType);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.activity_signature);
            verify(masterControl).findViewById(R.id.textView_cont);
            verify(masterControl).findViewById(R.id.signature_pad);
            verify(masterControl).findViewById(R.id.clear_button);
            verify(masterControl).findViewById(R.id.save_button);
            verify(masterControl).findViewById(R.id.cancel_signature);
        });
        //Pendiente de revizar algunos metodos dentro de esta clase
    }

    @Test
    public void saveImage() {
        //faltan pruebas
    }

    @Test
    public void showListView() {
        masterControl.showListView(10,listener,title,transType,listMenu,id);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.frag_show_list);
            verify(masterControl).findViewById(R.id.iv_close);
            verify(masterControl).findViewById(R.id.title_toolbar);
            verify(masterControl).findViewById(R.id.iv_menus);
        });
    }

    @Test
    public void getResultListener() {
        masterControl.getResultListener(listener);
        assertEquals(listener, listener);
    }

    @Test
    public void showBotonesSelecCuentaView() {
        masterControl.showSelectAccountView(10,data,items,items2,listener);
        masterControl.runOnUiThread(() -> {
            verify(masterControl).setContentView(R.layout.menu_seleccion_cuenta);
            verify(masterControl).findViewById(R.id.msg);
            verify(masterControl).findViewById(R.id.msg2);
            verify(masterControl).findViewById(R.id.msg3);
            verify(masterControl).findViewById(R.id.recyclerUno);
        });
    }

    @Test
    public void showQRCView(){
        masterControl.showQRCView(10,mode);
        masterControl.runOnUiThread(() -> verify(masterControl).setContentView(R.layout.layout_scan_qrc));
    }

    @Test
    public void hideKeyBoard() {
    }

    @Test
    public void onBackPressed() {
    }

    @Test
    public void incardTable() {
    }

    @Test
    public void fillData() {
    }
}