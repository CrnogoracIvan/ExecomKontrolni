package com.example.ivancrnogorac.execomkontrolni.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivancrnogorac.execomkontrolni.Model.ArticleList;
import com.example.ivancrnogorac.execomkontrolni.Model.ORMLightHelper;
import com.example.ivancrnogorac.execomkontrolni.Model.ShoppingList;
import com.example.ivancrnogorac.execomkontrolni.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {
    private ArticleList AL;
    private ORMLightHelper databaseHelper;

    //Metoda koja komunicira sa bazom podataka
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

    //-----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        int key = getIntent().getExtras().getInt(ListActivity.CONTACT_KEY);
        final CheckBox c = (CheckBox) findViewById(R.id.item_checkbox);

        try {
            AL = getDatabaseHelper().getArticleListDao().queryForId(key);

            TextView catchArticleName = (TextView) findViewById(R.id.item_name);
            TextView catchArticleAmount = (TextView) findViewById(R.id.item_amount);

            catchArticleName.setText(AL.getItemName());
            catchArticleAmount.setText(AL.getAmount());
            c.setChecked(AL.isPurchased());

            //Provera pri ucitavanju da li je kupljeno ili ne. Ako jeste pored checkbox-a upistai yes.
            if (AL.isPurchased()) {
                c.setText("Yes");
            } else {
                c.setText("No");
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (c.isChecked()) {
                    c.setText("Yes");
                    c.setChecked(true);
                    try {
                        AL.setPurchased(true);
                        AL.setPurchasedStatus("Yes");
                        getDatabaseHelper().getArticleListDao().update(AL);
                        Log.i("Sta je u bazi", getDatabaseHelper().getArticleListDao().queryForAll().toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    c.setText("No");
                    c.setChecked(false);
                    try {
                        AL.setPurchased(false);
                        AL.setPurchasedStatus("No");
                        getDatabaseHelper().getArticleListDao().update(AL);
                        Log.i("Sta je u bazi", getDatabaseHelper().getArticleListDao().queryForAll().toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Save and edit button
        Button button = (Button) findViewById(R.id.btn_edit_item);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                EditText catchArticleName = (EditText) findViewById(R.id.item_name);
                EditText catchArticleAmount = (EditText) findViewById(R.id.item_amount);

                AL.setItemName(catchArticleName.getText().toString());
                AL.setAmount(catchArticleAmount.getText().toString());

                try {
                    getDatabaseHelper().getArticleListDao().update(AL);
                    Toast.makeText(ArticleActivity.this, "Item detail updated.", Toast.LENGTH_SHORT).show();
                    refresh();
                    finish();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Brisanje item-a.
            case R.id.delete_item:
                try {
                    getDatabaseHelper().getArticleListDao().delete(AL);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
               // refreshItem();
                refresh();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // nakon rada sa bazo podataka potrebno je obavezno
        // osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
