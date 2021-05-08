package com.bcp.inicializacion.configuracioncomercio;

public class Companny {

    private String idCompanny;
    private String idCategory;
    private String description;

    public String[] fields = new String[]{
            "ID_EMPRESA",
            "ID_CATEGORIA",
            "DESCRIPCION"
    };

    public void setCompanny(String column, String value){
        switch (column){
            case "ID_EMPRESA":
                setIdCompanny(value);
                break;
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

    public void clearCompany(){
        for (String s:fields)
            setCompanny(s,"");
    }

    public String getIdCompanny() {
        return idCompanny;
    }

    public void setIdCompanny(String idCompanny) {
        this.idCompanny = idCompanny;
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
