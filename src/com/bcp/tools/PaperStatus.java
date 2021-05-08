package com.bcp.tools;

import com.pos.device.printer.Printer;

public class PaperStatus {
    private int ret;
    Printer printer = Printer.getInstance();

    public int getRet() {
        ret = printer.getStatus();
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
