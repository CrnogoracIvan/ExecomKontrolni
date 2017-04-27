package com.example.ivancrnogorac.execomkontrolni.activities;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivancrnogorac.execomkontrolni.model.ArticleList;
import com.example.ivancrnogorac.execomkontrolni.model.ORMLightHelper;
import com.example.ivancrnogorac.execomkontrolni.model.ShoppingList;
import com.example.ivancrnogorac.execomkontrolni.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ORMLightHelper databaseHelper;
    private ShoppingList SH;
    private EditText catchSHListName;

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
                    List<ArticleList> list = getDatabaseHelper().getArticleListDao().queryBuilder()
                            .where()
                            .eq(ArticleList.FIELD_NAME_SHLIST_NAME, SH.getId())
                            .query();

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
        int key = getIntent().getExtras().getInt(MainActivity.CONTACT_KEY);

       //Upis imena liste
        try {
            SH = getDatabaseHelper().getShoppingListDao().queryForId(key);
           //uzmi polje u koje ces upisati tekst
            catchSHListName = (EditText) findViewById(R.id.shoppingList_list_name);
            // upisi tekst
            catchSHListName.setText((SH.getShoppingListName()));

            //Prikaz itema u listi
            List<ArticleList> list = getDatabaseHelper().getArticleListDao().queryBuilder()
                    .where()
                    .eq(ArticleList.FIELD_NAME_SHLIST_NAME, SH.getId())
                    .query();

            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_item, list);
            listView.setAdapter(adapter);
            //Otvaranje itema
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArticleList ALI = (ArticleList) listView.getItemAtPosition(position);

                    Intent intent = new Intent(ListActivity.this, ArticleActivity.class);
                    intent.putExtra(CONTACT_KEY, ALI.getId());
                    startActivity(intent);
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

    @Override
    protected void onStart() {
        refresh();
        super.onStart();
    }

    // Reakcije na pritisak dugmica u meniju.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
//            //DIALOG ZA UNOS PODATAKA o novom artiklu
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
                        AL.setAmount(catchArticleListAmount.getText().toString());
                        AL.setPurchasedStatus("No");
                        AL.setListName(SH);

                        //unos zapisa u listu.
                        try {
                            getDatabaseHelper().getArticleListDao().create(AL);
                            Log.i("Sta je u bazi?", getDatabaseHelper().getArticleListDao().queryForAll().toString());

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

            //Editovanje liste
                case R.id.edit_list:
                    SH.setShoppingListName(catchSHListName.getText().toString());
                    try {
                        getDatabaseHelper().getShoppingListDao().update(SH);
                        Toast.makeText(ListActivity.this, "List name updated.", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                    //Brisanje liste.
            case R.id.delete_list:
                try {
                    getDatabaseHelper().getShoppingListDao().delete(SH);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        refresh();
        super.onResume();
        try {
            Log.i("Sta je u bazi?", getDatabaseHelper().getArticleListDao().queryForAll().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // DB oslobadjanje resursa
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}