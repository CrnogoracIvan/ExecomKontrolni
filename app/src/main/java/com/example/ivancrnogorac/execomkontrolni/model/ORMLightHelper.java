package com.example.ivancrnogorac.execomkontrolni.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;


public class ORMLightHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME    = "DataBase.db";
    private static final int    DATABASE_VERSION = 1;

    private Dao<ShoppingList, Integer> mShoppingListDao = null;
    private Dao<ArticleList, Integer> mArticleListDao = null;
    

    public ORMLightHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, ShoppingList.class);
            TableUtils.createTable(connectionSource, ArticleList.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, ShoppingList.class, true);
            TableUtils.dropTable(connectionSource, ArticleList.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<ShoppingList, Integer> getShoppingListDao() throws SQLException {
        if (mShoppingListDao == null) {
            mShoppingListDao = getDao(ShoppingList.class);
        }

        return mShoppingListDao;
    }

    public Dao<ArticleList, Integer> getArticleListDao() throws SQLException {
        if (mArticleListDao == null) {
            mArticleListDao = getDao(ArticleList.class);
        }

        return mArticleListDao;
    }

    //obavezno prilikom zatvarnaj rada sa bazom osloboditi resurse
    @Override
    public void close() {
        mShoppingListDao = null;
        mArticleListDao = null;

        super.close();
    }
}
