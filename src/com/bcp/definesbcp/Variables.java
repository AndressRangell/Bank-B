package com.bcp.definesbcp;

import android.content.Context;

public class Variables {

    private String version = "";
    private int contFallback = 0;
    private String idAcquirer;
    private boolean ctlSign = false;
    private String holderName;
    private Context mcontext;
    private boolean multiAccount = false;
    public static final int FALLBACK = 3;
    public static final int NO_FALLBACK = 0;
    public static final int TOTAL_BATCH = 500;
    public static final int MASTERKEYIDX = 0;
    public static final int WORKINGKEYIDX = 0;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getContFallback() {
        return contFallback;
    }

    public void setContFallback(int contFallback) {
        this.contFallback = contFallback;
    }

    public String getIdAcquirer() {
        return idAcquirer;
    }

    public void setIdAcquirer(String idAcquirer) {
        this.idAcquirer = idAcquirer;
    }

    public boolean isCtlSign() {
        return ctlSign;
    }

    public void setCtlSign(boolean ctlSign) {
        this.ctlSign = ctlSign;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    public boolean isMultiAccount() {
        return multiAccount;
    }

    public void setMultiAccount(boolean multiAccount) {
        this.multiAccount = multiAccount;
    }
}
