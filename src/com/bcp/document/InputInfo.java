package com.bcp.document;

import com.android.desert.keyboard.InputManager;

public class InputInfo {
    private boolean resultFlag;
    private int errno;
    private String result;
    private InputManager.Style nextStyle;
    private boolean isBack;

    public InputInfo() {
    }

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

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public InputManager.Style getNextStyle() {
        return this.nextStyle;
    }

    public void setNextStyle(InputManager.Style nextStyle) {
        this.nextStyle = nextStyle;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }
}
