package com.example.ivancrnogorac.execomkontrolni.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ivancrnogorac.execomkontrolni.dialog.AboutDialog;
import com.example.ivancrnogorac.execomkontrolni.model.ArticleList;
import com.example.ivancrnogorac.execomkontrolni.model.ORMLightHelper;
import com.example.ivancrnogorac.execomkontrolni.model.ShoppingList;
import com.example.ivancrnogorac.execomkontrolni.R;
import com.example.ivancrnogorac.execomkontrolni.adapter.ShoppinListAdapter;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private ORMLightHelper databaseHelper;
    public static String CONTACT_KEY = "SHOPPING_LIST_KEY";
    private List<ShoppingList> mainList;
    private ShoppinListAdapter adapter;

    ///Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    // Refresh metoda
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.shoppingList_list);

        if (listview != null) {
            ShoppinListAdapter adapter = (ShoppinListAdapter) listview.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<ShoppingList> list = getDatabaseHelper().getShoppingListDao().queryForAll();
                    adapter.updateAdapter((ArrayList<ShoppingList>)list);

                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public enum Completed {
        COMPLETED("Yes"),
        NOT_COMPLETED("No");

        private String text;

        Completed(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    //Modifikovana metoda koja proverava da li je lista kompletirana
    private void completedSL() {
        for (ShoppingList main : mainList) {
            for (ArticleList items : main.getArticles()) {
                if (!items.isPurchased()) {
                    main.setCompleted_text(Completed.NOT_COMPLETED.toString());
                    break;
                } else {
                    main.setCompleted_text(Completed.COMPLETED.toString());
                    break;
                }
            }
            try {
                getDatabaseHelper().getShoppingListDao().update(main);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
//--------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       //modularni prikaz liste
        final ListView listView = (ListView) findViewById(R.id.shoppingList_list);
        try {

            mainList = getDatabaseHelper().getShoppingListDao().queryForAll();
            adapter = new ShoppinListAdapter(MainActivity.this, (ArrayList<ShoppingList>)mainList);

            completedSL();

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ShoppingList SH = (ShoppingList) listView.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                    intent.putExtra(CONTACT_KEY, SH.getId());
                    startActivity(intent);
                }
            });
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        refresh();
    }
    //kreiranje menija/toolbara
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Reakcije na pritisak dugmica u meniju.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
//            //DIALOG ZA UNOS PODATAKA o novoj listi
            case R.id.add_new_shoppingList:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_list_dialog_layout);

                //akcije koja se desava kada se dugme klikne
                Button add = (Button) dialog.findViewById(R.id.create_list);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText catchShoppingListName = (EditText) dialog.findViewById(R.id.shoppingList_name);

                        ShoppingList SL = new ShoppingList();
                        SL.setShoppingListName(catchShoppingListName.getText().toString());
                        SL.setCompleted_text("No");

                      //unos zapisa u listu.
                        try {
                            getDatabaseHelper().getShoppingListDao().create(SL);
                            Log.i("Log_Sta je u bazi?", getDatabaseHelper().getShoppingListDao().queryForAll().toString());

                        } catch (java.sql.SQLException e) {
                            e.printStackTrace();
                        }

                        //REFRESH liste
                        refresh();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            //About dialog
            case R.id.about_app_dialog:
                AlertDialog alertDialog = new AboutDialog(this).prepareDialog();
                alertDialog.show();
                break;
      }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {

        refresh();
        completedSL();
        super.onResume();
        try {
            Log.i("Sta je u bazi?", getDatabaseHelper().getShoppingListDao().queryForAll().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}