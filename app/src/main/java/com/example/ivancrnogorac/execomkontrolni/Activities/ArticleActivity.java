package com.example.ivancrnogorac.execomkontrolni.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ivancrnogorac.execomkontrolni.Model.ArticleList;
import com.example.ivancrnogorac.execomkontrolni.Model.ORMLightHelper;
import com.example.ivancrnogorac.execomkontrolni.R;
import com.j256.ormlite.android.apptools.OpenHelperManager;

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





    //-----------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        int key = getIntent().getExtras().getInt(ListActivity.CONTACT_KEY);

        try {
            AL = getDatabaseHelper().getArticleListDao().queryForId(key);

            TextView catchArticleName = (TextView) findViewById(R.id.item_name);
            TextView catchArticleAmount = (TextView) findViewById(R.id.item_amount);

            catchArticleName.setText(AL.getItemName());
            catchArticleAmount.setText(AL.getAmount());

        } catch (Exception e) {
            e.printStackTrace();
        }
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
