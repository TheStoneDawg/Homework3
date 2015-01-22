package com.example.samresendez.projecttake2;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.lang.String;

import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxPath;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends ListActivity {
    private ArrayList<String> StringList2;
    public static final int LIST_REQUEST_CODE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        StringList2 = intent.getStringArrayListExtra(MainActivity.MESSAGE);
        Log.e("did it work???!", "test");
        setListAdapter(new ListAdapter() {
            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return StringList2.size();
            }

            @Override
            public Object getItem(int position) {
                return StringList2.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View rows = convertView;
                if (rows == null) {
                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    rows = layoutInflater.inflate(R.layout.rows, parent, false);
                }

                String medSchool = StringList2.get(position);
                TextView textViewIntermediary = (TextView) rows.findViewById(R.id.rows);
                textViewIntermediary.setText(medSchool);
                return rows;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });
    }
    @Override
    protected void onListItemClick(ListView list,View view,int position,long id) {
        TextView textView = (TextView) view.findViewById(R.id.rows);
        String pathString = textView.getText().toString();
        Intent swagIntent = new Intent();
        swagIntent.putExtra("String Name",pathString);
        startActivityForResult(swagIntent,LIST_REQUEST_CODE);
        finish();
    }
}
