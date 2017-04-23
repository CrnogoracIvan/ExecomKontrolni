package com.example.ivancrnogorac.execomkontrolni.Activities;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.ivancrnogorac.execomkontrolni.Dialog.AboutDialog;
import com.example.ivancrnogorac.execomkontrolni.Model.ArticleList;
import com.example.ivancrnogorac.execomkontrolni.Model.ORMLightHelper;
import com.example.ivancrnogorac.execomkontrolni.Model.ShoppingList;
import com.example.ivancrnogorac.execomkontrolni.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private ORMLightHelper databaseHelper;
    public static String CONTACT_KEY = "SHOPPING_LIST_KEY";

    ///Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    //Refresh metoda
    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.shoppingList_list);

        if (listview != null) {
            ArrayAdapter<ShoppingList> adapter = (ArrayAdapter<ShoppingList>) listview.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<ShoppingList> list = getDatabaseHelper().getShoppingListDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();

                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //Refresh metoda
    private void refreshItem() {
        ListView listview = (ListView) findViewById(R.id.articleList);

        if (listview != null) {
            ArrayAdapter<ArticleList> adapter = (ArrayAdapter<ArticleList>) listview.getAdapter();

            if (adapter != null) {
                try {
                    adapter.clear();
                    List<ArticleList> list = getDatabaseHelper().getArticleListDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();

                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //Metoda koja proverava da li je lista kompletirana

    private void  completedSL2(){

        try {
            List<ShoppingList> shList = getDatabaseHelper().getShoppingListDao().queryForAll();
            List<ArticleList> shItem = getDatabaseHelper().getArticleListDao().queryForAll();

            for (int i = 0; i < shList.size() ; i++) {
                Log.i("Log_Lista_kompletirna:", String.valueOf(i) + " " + shList.get(i).getCompleted_text());
               //if (shList.get(i).getCompleted_text().equals("No")){

                    for (int j = 0; j <shItem.size() ; j++) {
                        Log.i("Log_pripada_listi",shItem.get(j).getItemName() + " " + shItem.get(j).getListName().toString());
                        if (shItem.get(j).getPurchasedStatus().equals("No")) {
                            shList.get(i).setCompleted(false);
                            shList.get(i).setCompleted_text("No");
                            getDatabaseHelper().getArticleListDao().update(shItem.get(j));
                            getDatabaseHelper().getShoppingListDao().update(shList.get(i));
                            //Log.i("Da li je ili nije", String.valueOf(shItem.get(j).isPurchased()));
                            Log.i("Log_nije_kompletna",  shList.get(i).getCompleted_text());

                            break;
                        }else{
                            shList.get(i).setCompleted(true);
                            shList.get(i).setCompleted_text("Yes");
                            getDatabaseHelper().getArticleListDao().update(shItem.get(j));
                            getDatabaseHelper().getShoppingListDao().update(shList.get(i));
                            Log.i("Log_je_kompletna",  shList.get(i).getCompleted_text());

                        }
                    }
               // getDatabaseHelper().getShoppingListDao().update(shList.get(i));
                refresh();
                }

          // }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }
//--------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        refresh();
//        completedSL2();

       //modularni prikaz liste
        final ListView listView = (ListView) findViewById(R.id.shoppingList_list);
        try {
            List<ShoppingList> list = getDatabaseHelper().getShoppingListDao().queryForAll();

            ListAdapter adapter = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
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
       // completedSL2();
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

        completedSL2();
        refresh();


        super.onResume();
        try {
            Log.i("Sta je u bazi?", getDatabaseHelper().getShoppingListDao().queryForAll().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
//    @Override
//    protected void onStart(){
//        completedSL2();
//        refresh();
//        super.onStart();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
