package com.bcp.document;

import com.bcp.rest.JSONInfo;

public class InputData {

    private boolean resultFlag;
    private int errno;
    private ClassArguments arguments;
    private boolean isBack;
    private JSONInfo jsonInfo;


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

    public ClassArguments getArguments() {
        return arguments;
    }

    public void setArguments(ClassArguments arguments) {
        this.arguments = arguments;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public JSONInfo getJsonInfo() {
        return jsonInfo;
    }

    public void setJsonInfo(JSONInfo jsonInfo) {
        this.jsonInfo = jsonInfo;
    }
}
