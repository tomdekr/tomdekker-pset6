package com.example.tom_d.moviebase;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


/**
 * Created by Tom_D on 11/30/2017.
 */

public class MovieAdapter extends CursorAdapter {


    public MovieAdapter(Context context, Cursor cursor) {
        super(context, cursor, R.layout.content_movie_adapter);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.content_movie_adapter, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView title = view.findViewById(R.id.title);
        title.setText(cursor.getString(cursor.getColumnIndex("name")));

    }
}