package com.munera.android.moviesapp.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Diego Munera on 28/02/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<MovieInfo> {

    private static final String LOG_TAG = MovieArrayAdapter.class.getSimpleName();

    public MovieArrayAdapter(Activity context, List<MovieInfo> movieList) {
        super(context,0,movieList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        MovieInfo movieInfo = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }
        ImageView iconView = (ImageView) convertView.findViewById(R.id.grid_item_movie_imageview);
        Picasso.with(getContext()).load(movieInfo.posterPath).into(iconView);

        return convertView;
    }

}
