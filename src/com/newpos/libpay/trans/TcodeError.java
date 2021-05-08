package com.newpos.libpay.trans;

/**
 * Created by zhouqiang on 2017/3/26.
 * @author zhouqiang
 * 交易代码集合
 */

public class TcodeError {
    public static final int T_SOCKET_ERR = 101 ;//Socket连接失败、999999999999
    public static final int T_SEND_ERR = 102 ;//发送数据失败
    public static final int T_RECEIVE_ERR = 103 ;//接收数据失败
    public static final int T_USER_CANCEL_INPUT = 104 ;//用户取消输入
    public static final int T_INVOKE_PARA_ERR = 105 ;//调用传参错误
    public static final int T_WAIT_TIMEOUT = 106 ;//等待超时
    public static final int T_SEARCH_CARD_ERR = 107 ;//寻卡错误
    public static final int T_IC_POWER_ERR = 108 ;//IC卡上电错误
    public static final int T_IC_NOT_EXIST_ERR = 109 ;//IC卡不在位
    public static final int T_USER_CANCEL_PIN_ERR = 110 ;//用户取消PIn输入
    public static final int T_PRINT_NO_LOG_ERR = 111 ;//无此交易日志
    public static final int T_NOT_FIND_TRANS = 112 ;//未查询到改交易
    public static final int T_TRANS_IS_VOIDED = 113 ;//此交易已撤销
    public static final int T_USER_CANCEL_OPERATION = 114 ;//TRANSACCION CANCELADA
    public static final int T_VOID_CARD_NOT_SAME = 115 ;//撤销交易非原交易卡
    public static final int T_PACKAGE_MAC_ERR = 116 ;//返回报文MAC校验错误
    public static final int T_PACKAGE_ILLEGAL = 117 ;//返回报文非法处理
    public static final int T_RECEIVE_REFUSE = 118 ;//拒绝
    public static final int T_SELECT_APP_ERR = 119 ;//选择应用失败
    public static final int T_READ_APP_DATA_ERR = 120 ;//读取卡片应用数据失败
    public static final int T_OFFLINE_DATAAUTH_ERR = 121 ;//脱机数据认证失败
    public static final int T_TERMINAL_ACTION_ANA_ERR = 122 ;//终端行为分析失败
    public static final int T_IC_NOT_ALLOW_SWIPE = 123 ;//IC卡不允许降级交易
    public static final int T_REVERSAL_FAIL = 124; //冲正失败，联系管理员
    public static final int T_QPBOC_ERRNO = 125; //非接读卡异常
    public static final int T_USER_CANCEL = 126; //用户取消操作
    public static final int T_PRINTER_EXCEPTION = 127; //打印机异常
    public static final int T_GEN_2_AC_FAIL = 128 ; //二次授权失败
    public static final int T_ERR_LAST_4 = 129 ; //Incorrecto
    public static final int T_NOT_ALLOW = 130 ; //Transaccion no permitida
    public static final int T_UNSUPPORT_CARD = 131 ; //Tarjeta no soportada
    public static final int T_MSG_ERR_GAS = 132 ; //Transaccion no permitida, intente con otra tarjeta
    public static final int T_ERR_INVALID_LEN = 133 ; //Longitud invalida
    public static final int T_ERR_FALLBACK = 134 ; //contFallback
    public static final int T_ERR_NO_TRANS = 135 ; //Lote vacio
    public static final int T_ERR_BATCH_FULL = 136 ; //Realice cierre para continuar
    public static final int T_ERR_EXP_DATE_APP = 137 ; //Aplicacion expirada
    public static final int T_ERR_PIN_NULL = 138 ; //No ingreso PIN
    public static final int T_ERR_DETECT_CARD_FAILED = 139 ;//Fallo al detectar la tarjeta
    public static final int T_ERR_NOT_FILE_TERMINAL = 140 ;//No existe el archivo TERMINAL
    public static final int T_ERR_NOT_FILE_PROCESSING = 141 ;//No existe el archivo PROCESSING
    public static final int T_ERR_NOT_FILE_ENTRY_POINT = 142 ;//No existe el archivo ENTRY POINT
    public static final int T_ERR_NOT_ALLOW = 143;//Ingreso manual NO permitido
    public static final int T_ERR_TIMEOUT = 144;//Tiempo de espera agotado
    public static final int T_INSERT_CARD = 145;//Inserte Tarjeta
    public static final int T_ERR_NOT_INTERNET = 146 ; //SIN ACCESO, VERIFIQUE SU CONEXION
    public static final int T_INTERNAL_SERVER_ERR = 147 ; //ERROR DE SERVIDOR
    public static final int T_ERR_BAD_REQUEST = 148 ; //SOLICITUD WS INCORRECTA
    public static final int T_ERR_NO_FOUND = 149 ; //NO ENCONTRADO
    public static final int T_ERR_CONTACT_TRANS = 150; //REALICE TRANSACCION POR CONTACTO
    public static final int T_ERR_JSON_REQ = 151 ; //NO ENCONTRADO
    public static final int T_ERR_PACK_ISO = 152 ; //ERROR AL EMPAQUETAR ISO
    public static final int T_ERR_AMNT = 153 ; //MONTO INVALIDO
    public static final int T_ERR_UNPACK_FILE = 154 ; //ERROR AL DESEMPAQUETAR ARCHIVO
    public static final int T_ERR_NOT_FILE = 155 ; //ERROR NO EXISTE ARCHIVO ZIP
    public static final int T_ERR_REMOVE_CARD = 156 ; //DEBE RETIRAR TARJETA\n PARA CONTINUAR
    public static final int T_ERR_RSP_CODE = 157 ; //TRANSACCION RECHAZADA POR EL HOST
    public static final int T_ERR_UNPACK_JSON = 158 ; //ERROR AL PARSEAR JSON
    public static final int T_ERR_URL_NULL = 159 ; //NO EXISTE URL DEFINIDA
    public static final int T_ERR_SAVE_BATCH = 160 ; //ERROR AL GUARDAR LA TRANSACCION
    public static final int T_ERR_CLEAR_REV = 161 ; //ERROR AL LIMPIAR REVERSO
    public static final int T_ERR_PRINT_DATA = 162 ; //NO EXISTEN INFORMACION PARA IMPRIMIR
    public static final int T_ERR_CERT = 163 ; //ERROR CERTIFICADO
    public static final int T_ERR_GEN_TRAN = 164 ; //SERVICIO CONSULTADO NO SE ENCUENTRA CONFIGURADO
    public static final int T_ERR_LIST_PROD      = 165; //ERROR EN LISTA DE PRODUCTOS
    public static final int T_ERR_EXECUTE_TRANS  = 166; //ERROR AL EJECUTAR TRANSACCION
    public static final int T_ERR_CONF_VOUCHER   = 167; //ERROR AL CONFIRMAR VOUCHER
    public static final int T_ERR_OBTENER_TOKEN  = 168; //ERROR AL OBTENER TOKEN
    public static final int T_ERR_VALITE_ACCOUNT = 169; //ERROR AL VALIDAR CUENTA
    public static final int T_ERR_VERIF_DEPOSI   = 170; //ERROR AL VERIFICAR DEPOSITO
    public static final int T_ERR_OBTE_DOCUMENT  = 171; //ERROR AL OBTENER DOCUMENTO
    public static final int T_ERR_VALIDATE_DOCUMENT  = 172; //ERROR AL VALIDAR DOCUMENTO
    public static final int T_ERR_REUME_TRANS    = 173; //ERROR EN RESUMEN TRANSACCION
    public static final int T_ERR_ENCRIPT_DATA = 174; //ERROR EN LA ENCRIPCION DE DATOS
    public static final int T_ERR_SAVE_KEYSTORE = 175; //ERROR EN EL GUARDADO DEL KEYSTORE
    public static final int T_ERR_READ_CERTIFICATE = 176; //ERROR LEYENDO EL CERTIFICADO
    public static final int T_ERR_PROCESING_INIT = 177; //ERROR PROCESANDO INCIALIZACION
    public static final int T_ERR_RESPONSE = 178; //RESPUESTA JSON VACIA
    public static final int T_ERR_KEY = 179;//NO EXISTEN LLAVES ERROR AL INICIALIZAR
    public static final int T_ERR_DECRYPT_DATA = 180; //ERROR EN LA DESENCRIPCION DE DATOS
    public static final int T_ERR_KEY_PRV = 181; //ERROR NO EXISTE LLAVE PRIVADA
    public static final int T_ERR_FILE_NOT_EXIST = 182; //ERROR NO EXISTE CERTIFICADO
    public static final int T_ERR_TIMEOUT_IN_DATA = 183;//TIEMPO DE ESPERA DE INGRESO DE DATOS AGOTADO

    //MANEJO DE ERRORES PARA FRAGMENT
    public static final int T_ERR_RETURN_FRAGMENT = 1001;//REGRESOS ENTRE PANTALLAS
    public static final int T_ERR_NAVI_FRAGMENT = 1001;//NAVEGACION ENTRE FRAGMENT
    public static final int T_ERR_DATA_NULL = 1002;//VALORES NULL


    public static final int T_BLOCKED_APLICATION = 2063;
    public static final int T_DECLINE_OFFLINE = 2069;
    public static final int T_UNKNOW_ERR = 999 ;//未知错误

    protected TcodeError() {
    }
}
