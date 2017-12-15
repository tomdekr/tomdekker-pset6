package com.example.tom_d.moviebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tom_D on 12/13/2017.
 */

public class FavoriteList extends ArrayAdapter<FavoriteMovie> {

    private Activity context;
    private List<FavoriteMovie> movieList;

    public FavoriteList(Activity context, List<FavoriteMovie> movieList) {
        super(context, R.layout.row_layout, movieList);
        this.context = context;
        this.movieList = movieList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
        TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);

        FavoriteMovie movie = movieList.get(position);
        textViewTitle.setText(movie.getTitle());

        return listViewItem;
    }
}
