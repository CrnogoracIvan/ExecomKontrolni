package com.example.ivancrnogorac.execomkontrolni.Activities;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ivancrnogorac.execomkontrolni.Model.ArticleList;
import com.example.ivancrnogorac.execomkontrolni.Model.ORMLightHelper;
import com.example.ivancrnogorac.execomkontrolni.Model.ShoppingList;
import com.example.ivancrnogorac.execomkontrolni.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private ShoppingList a;

    public static String CONTACT_KEY = "ITEM_KEY";

    ///Metoda koja komunicira sa bazom podataka
    public ORMLightHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, ORMLightHelper.class);
        }
        return databaseHelper;
    }

    //Refresh metoda
    private void refresh() {
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

//--------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        //Uzima polja iz baze i ubacuje ih u polja u aktivitiju

        final ListView listView = (ListView) findViewById(R.id.articleList);

        // /JAKO BITAN RED KODA - Na ovaj nacin se ubacuje tacno onaj glumac na kojeg smo kliknuli!
        int key = getIntent().getExtras().getInt(MainActivity.CONTACT_KEY);

        try {
            a = getDatabaseHelper().getShoppingListDao().queryForId(key);

        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {

            List<ArticleList> list = getDatabaseHelper().getArticleListDao().queryBuilder()
                    .where()
                    .eq(ArticleList.FIELD_NAME_LIST_NAME, a.getId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArticleList AL = (ArticleList) listView.getItemAtPosition(position);
                    //Toast.makeText(PripremaDetail.this, m.getmName()+" "+m.getmGenre()+" "+m.getmYear(), Toast.LENGTH_SHORT).show();

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

   }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Reakcije na pritisak dugmica u meniju.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

//            //DIALOG ZA UNOS PODATAKA o novoj listi
            case R.id.add_new_item:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_item_dialog_layout);

                //akcije koja se desava kada se dugme klikne
                Button add = (Button) dialog.findViewById(R.id.btn_add_item_in_cart);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText catchArticleListName= (EditText) dialog.findViewById(R.id.item_name);
                        EditText catchArticleListAmount= (EditText) dialog.findViewById(R.id.item_amount);

                        ArticleList AL = new ArticleList();
                        AL.setItemName(catchArticleListName.getText().toString());
                        AL.setAmount(Integer.parseInt(catchArticleListAmount.getText().toString()));

                        //unos zapisa u listu.
                        try {
                            getDatabaseHelper().getArticleListDao().create(AL);

                            //REFRESH liste
                            refresh();

                        } catch (java.sql.SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });
                dialog.show();
                break;
            //About dialog

//            case R.id.about:
//
//                AlertDialog alertDialog = new oAppDialog(this).prepareDialog();
//                alertDialog.show();
//                break;

//            //Brisanje liste.
//            case R.id.delete_shoppingList:
//                getDatabaseHelper().getShoppingListDao().delete(SL);
//                finish();
//                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh();

    }

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
