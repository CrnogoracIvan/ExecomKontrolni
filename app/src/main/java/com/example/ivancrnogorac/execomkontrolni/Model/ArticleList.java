package com.example.ivancrnogorac.execomkontrolni.Model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Ivan Crnogorac on 4/13/2017.
 */

public class ArticleList {

    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_ITEM = "Item name";
    public static final String FIELD_NAME_AMOUNT = "Amount";
    public static final String FIELD_NAME_LIST_NAME = "List";


    @DatabaseField (columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField (columnName = FIELD_NAME_ITEM)
    private String itemName;

    @DatabaseField (columnName = FIELD_NAME_AMOUNT)
    private int amount;

    @DatabaseField (columnName = FIELD_NAME_LIST_NAME, foreign = true, foreignAutoRefresh = true)
    private ShoppingList listName;


    private boolean inCart;



    public ArticleList() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public ShoppingList getListName() {
        return listName;
    }

    public void setListName(ShoppingList listName) {
        this.listName = listName;
    }

    @Override
    public String toString() {
        return "Item: " + itemName + ", amount: " + amount;
    }
}
