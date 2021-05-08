package com.bcp.inicializacion.configuracioncomercio;

public class Category {

    private String idCategory;
    private String description;

    public String[] fields = new String[]{
            "ID_CATEGORIA",
            "DESCRIPCION"
    };

    public void setCategory(String column, String value){
        switch (column){
            case "ID_CATEGORIA":
                setIdCategory(value);
                break;
            case "DESCRIPCION":
                setDescription(value);
                break;
            default:
                break;
        }
    }

    public void clearCategory() {
        for (String s : fields) {
            setCategory(s, "");
        }
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getFields() {
        return fields;
    }
}
