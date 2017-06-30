package com.example.finance.AboutListView;

public class ListItem {
    public static final int MONEY_EDIT=0;
    public static final int TYPE_EDIT = 1;
    public static final int TYPE_TEXT = 2;
    public static final int MAIN_VIEW=3;
    public static final int FINA_VIEW=4;
    public static final int TYPE_COUNT = 5;
    private String name;
    private String secondName;
    private int type;
    public ListItem(int type, String name,String secondName) {
        this.type = type;
        this.name = name;
        this.secondName=secondName;
    }
    public int getType() {
        return type;
    }
    public String getName() {
        return name;
    }
    public String getSecondName(){
        return secondName;
    }
}
