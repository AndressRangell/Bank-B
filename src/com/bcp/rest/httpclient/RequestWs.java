package com.bcp.rest.httpclient;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bcp.rest.JsonUtil;
import com.newpos.libpay.Logger;
import com.newpos.libpay.trans.TcodeError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JHON MORANTES on 21/09/2016.
 */

/**
 * Esta clase realiza las peticiones al web service
 */
public class RequestWs {

    public static final String TAG = RequestWs.class.getSimpleName();
    private Context context;
    private String url;
    private int timeout;
    private int retVal = -1;
    private Map<String,String> headers;
    private int methodRequest = Request.Method.POST;
    private boolean TLS = false;
    private VolleyCallback volleyCallback;

    public RequestWs(Context context, String url, int timeout, boolean tls) {
        this.context = context;
        this.url = url;
        this.timeout = timeout;
        this.TLS = tls;
    }

    /**
     * Este metodo realiza el llamado de la clase volley la cual se encarga del
     * realizar las peticiones al WS.
     * @param object
     * @param callback
     */
    public void httpRequets(final Map<String, String> header, JSONObject object, final VolleyCallback callback){

        if (object==null){
            methodRequest = Request.Method.GET;
        }
        if (url==null){
            callback.onSuccessResponse(null, TcodeError.T_ERR_URL_NULL, null);
            return;
        }

        Logger.logLine(Logger.LOG_GENERAL, "httpRequets");
        Logger.logLine(Logger.LOG_GENERAL, "URL " + url);
        Map<String, String> headerLog = new HashMap<>(header);
        try {
            headerLog.remove(JsonUtil.getAPIKEY());
            headerLog.remove(JsonUtil.getSESSION());
        }catch (Exception ex){
            Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), ex.getMessage());
        }
        Logger.logLine(Logger.LOG_GENERAL, "Header " + headerLog);


        Logger.debug("URL " + url);
        Logger.debug("Header " + header);
        Logger.debug("Body " + object);

        volleyCallback = callback;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(methodRequest, url, object, response -> callback.onSuccessResponse(response, retVal, headers), errorListener){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Logger.logLine(Logger.LOG_GENERAL, "Recibiendo petición ");
                retVal = response.statusCode;
                try {
                    headers = response.headers;
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject result = null;
                    if (jsonString.length() > 0)
                        result = new JSONObject(jsonString);

                    return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException | JSONException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " RequestWs " +  e.getMessage());
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                return header;
            }
        };

        //Se indica un timeout
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        // Adding request to request queue
        MySingleton.getmInstance(context, TLS).addToRequestque(jsonObjectRequest);
    }

    public void httpRequetsStringPost(final Map<String, String> params, final VolleyCallback callback) {

        if (url==null){
            callback.onSuccessResponse(null, TcodeError.T_ERR_URL_NULL, null);
            return;
        }

        volleyCallback = callback;

        Logger.logLine(Logger.LOG_GENERAL, "httpRequetsStringPost");
        Logger.logLine(Logger.LOG_GENERAL, "URL " + url);

        Logger.debug("URL " + url);
        Logger.debug("Params " + params);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, this::validateResponse, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getParams() {
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Logger.logLine(Logger.LOG_GENERAL, "Recibiendo petición ");
                retVal = response.statusCode;
                headers = response.headers;
                return super.parseNetworkResponse(response);
            }
        };

        //Se indica un timeout
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MySingleton.getmInstance(context, TLS).addToRequestque(stringRequest);
    }

    public void httpRequetsStringPostWithOutBody(final Map<String, String> header, final VolleyCallback callback) {

        if (url==null){
            callback.onSuccessResponse(null, TcodeError.T_ERR_URL_NULL, null);
            return;
        }

        volleyCallback = callback;

        Logger.logLine(Logger.LOG_GENERAL, "httpRequetsStringPostWithOutBody");
        Logger.logLine(Logger.LOG_GENERAL, "URL " + url);
        Map<String, String> headerLog = new HashMap<>(header);
        try {
            headerLog.remove(JsonUtil.getAPIKEY());
            headerLog.remove(JsonUtil.getSESSION());
        }catch (Exception ex){
            Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), ex.getMessage());
        }
        Logger.logLine(Logger.LOG_GENERAL, "Header " + headerLog);

        Logger.debug("URL " + url);
        Logger.debug("header " + header);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, this::validateResponse, errorListener) {

            @Override
            public Map<String, String> getHeaders() {
                return header;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Logger.logLine(Logger.LOG_GENERAL, "Recibiendo petición ");
                retVal = response.statusCode;
                headers = response.headers;
                return super.parseNetworkResponse(response);
            }
        };

        //Se indica un timeout
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MySingleton.getmInstance(context, TLS).addToRequestque(stringRequest);
    }

    public void httpRequetsStringGet(final Map<String, String> header, String data, final VolleyCallback callback) {

        if (url==null){
            callback.onSuccessResponse(null, TcodeError.T_ERR_URL_NULL, null);
            return;
        }

        String uri;
        if (data == null) {
            uri = url;
        } else {
            uri = url + "?" + data;
        }


        volleyCallback = callback;

        Logger.logLine(Logger.LOG_GENERAL, "httpRequetsStringGet");
        Logger.logLine(Logger.LOG_GENERAL, "URL " + url);
        Map<String, String> headerLog = new HashMap<>(header);
        try {
            headerLog.remove(JsonUtil.getAPIKEY());
            headerLog.remove(JsonUtil.getSESSION());
        }catch (Exception ex){
            Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), ex.getMessage());
        }
        Logger.logLine(Logger.LOG_GENERAL, "Header " + headerLog);

        Logger.debug("URL " + uri);
        Logger.debug("Header " + header);
        Logger.debug("data " + data);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, this::validateResponse, errorListener) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() {
                return header;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Logger.logLine(Logger.LOG_GENERAL, "Recibiendo petición ");
                retVal = response.statusCode;
                headers = response.headers;
                return super.parseNetworkResponse(response);
            }
        };

        //Se indica un timeout
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeout, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        MySingleton.getmInstance(context, TLS).addToRequestque(stringRequest);
    }

    private void validateResponse(String response){
        try {
            if (response!=null){
                if (response.length()>0){
                    JSONObject obj = new JSONObject(response);
                    volleyCallback.onSuccessResponse(obj, retVal, headers);
                }else
                    volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_RESPONSE,headers);
            }else
                volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_RESPONSE,headers);
        } catch (JSONException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "validateResponse " + e.getMessage());
            volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_RESPONSE,null);
        }
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Logger.logLine(Logger.LOG_GENERAL, "Validando respuesta de Error");
            NetworkResponse response = error.networkResponse;

            if (error != null && error.getMessage() != null)
                Logger.logLine(Logger.LOG_GENERAL, "onErrorResponse " + error.getMessage());

            if(error.networkResponse == null && error instanceof NoConnectionError
                    && error.getMessage().contains("javax.net.ssl.SSLHandshakeException"))
            {
                volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_CERT,null);
                return;
            }
            if (error instanceof NetworkError) {
                volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_NOT_INTERNET,null);
                return;
            } else if(error instanceof TimeoutError){
                volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_TIMEOUT,null);
                return;
            }
            else if (error instanceof AuthFailureError || error instanceof ServerError) {
                try {
                    volleyCallback.onSuccessResponse(new JSONObject(new String(response.data)), TcodeError.T_INTERNAL_SERVER_ERR,response.headers);
                } catch (JSONException e) {
                    volleyCallback.onSuccessResponse(null, TcodeError.T_INTERNAL_SERVER_ERR,null);
                }
                return;
            } else if (error instanceof ParseError) {
                volleyCallback.onSuccessResponse(null, TcodeError.T_ERR_UNPACK_JSON,null);
                return;
            }

            if (response != null && response.data != null){
                switch (retVal){
                    case 500:
                        retVal = TcodeError.T_INTERNAL_SERVER_ERR;
                        break;
                    case 400:
                        retVal = TcodeError.T_ERR_BAD_REQUEST;
                        break;
                    case 404:
                        retVal = TcodeError.T_ERR_NO_FOUND;
                        break;
                    default:
                        retVal = 999;
                        break;
                }
            }else{
                retVal = TcodeError.T_ERR_UNPACK_JSON;
            }

            volleyCallback.onSuccessResponse(null, retVal,null);

        }
    };
    /**
     * En esta interface se realiza la definicion del metodo onSuccessResponse que recibe la
     * respuesta de la peticion del metodo al WS.
     */
    public interface VolleyCallback{
        void onSuccessResponse(JSONObject result, int statusCode, Map<String,String> header);
    }
}
