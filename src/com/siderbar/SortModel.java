package com.siderbar;

import com.bcp.inicializacion.configuracioncomercio.Companny;

import java.io.Serializable;

public class SortModel implements Serializable {
    private Companny name;
    private String sortLetters;

    public SortModel(Companny name, String sortLetters, boolean isChecked) {
        super();
        this.name = name;
        this.sortLetters = sortLetters;
        this.isChecked = isChecked;
    }

    public SortModel() {
        super();
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private boolean isChecked;

    public Companny getName() {
        return name;
    }

    public void setName(Companny name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
