package com.example.ivancrnogorac.execomkontrolni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ivancrnogorac.execomkontrolni.model.ShoppingList;
import com.example.ivancrnogorac.execomkontrolni.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Ivan Crnogorac on 4/26/2017.
 */

public class ShoppinListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ShoppingList>SHList;

    public ShoppinListAdapter(Context context, ArrayList<ShoppingList> SHList) {
        this.context = context;
        this.SHList = SHList;
    }

    @Override
    public int getCount() {
        return SHList.size();
    }

    @Override
    public Object getItem(int position) {
        return SHList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        sortByCompletion();

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.shopping_list_adapter_layout,parent,false);
        }

        TextView SHListName = (TextView) convertView.findViewById(R.id.tv_shListName);
        SHListName.setText(SHList.get(position).getShoppingListName());

        TextView SHListCompleted = (TextView) convertView.findViewById(R.id.tv_completed);
        SHListCompleted.setText(SHList.get(position).getCompleted_text());

        return convertView;
    }

    public void updateAdapter (ArrayList<ShoppingList> list){
        this.SHList = list;
        notifyDataSetChanged();
    }

    public void clear (){
        SHList.clear();
    }

    public void sortByCompletion(){
        Collections.sort(SHList, new Comparator<ShoppingList>() {
            @Override
            public int compare(ShoppingList o1, ShoppingList o2) {
                return o1.getCompleted_text().compareTo(o2.getCompleted_text());
            }
        });
        notifyDataSetChanged();
    }
}
