package com.example.ivancrnogorac.execomkontrolni.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by Ivan Crnogorac on 4/13/2017.
 */

@DatabaseTable(tableName = ShoppingList.TABLE_NAME_NAME)
public class ShoppingList {

    public static final String FIELD_NAME_ID = "id";
    public static final String TABLE_NAME_NAME = "name";
    public static final String FIELD_NAME_COMPLETED = "completed";
    public static final String TABLE_NAME_ARTICLES = "articles";

    @DatabaseField (columnName = FIELD_NAME_ID, generatedId = true)
    private int id;

    @DatabaseField (columnName = TABLE_NAME_NAME)
    private String shoppingListName;

    @DatabaseField (columnName = FIELD_NAME_COMPLETED)
    private boolean completed = false;

    @ForeignCollectionField(columnName = ShoppingList.TABLE_NAME_ARTICLES, eager = true)
    private ForeignCollection<ArticleList> articles;


    public ShoppingList(String shoppingListName) {
        this.shoppingListName = shoppingListName;
    }


    public ShoppingList() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getShoppingListName() {
        return shoppingListName;
    }

    public void setShoppingListName(String shoppingListName) {
        this.shoppingListName = shoppingListName;
    }


    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


    public ForeignCollection<ArticleList> getArticles() {
        return articles;
    }

    public void setArticles(ForeignCollection<ArticleList> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "Shopping List no: " + id + " - " + shoppingListName + "." ;
    }
}
