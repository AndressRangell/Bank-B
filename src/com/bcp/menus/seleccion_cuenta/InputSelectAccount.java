package com.bcp.menus.seleccion_cuenta;

import com.android.desert.keyboard.InputManager;

public class InputSelectAccount {
    private boolean resultFlag;
    private int errno;
    private SelectedAccountItem result;
    private InputManager.Style nextStyle;

    public boolean isResultFlag() {
        return this.resultFlag;
    }

    public void setResultFlag(boolean resultFlag) {
        this.resultFlag = resultFlag;
    }

    public int getErrno() {
        return this.errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public SelectedAccountItem getResult() {
        return this.result;
    }

    public void setResult(SelectedAccountItem result) {
        this.result = result;
    }

    public InputManager.Style getNextStyle() {
        return this.nextStyle;
    }

    public void setNextStyle(InputManager.Style nextStyle) {
        this.nextStyle = nextStyle;
    }
}
