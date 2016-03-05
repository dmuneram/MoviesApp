package com.munera.android.moviesapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(MovieInfo.class.getName())){
            MovieInfo info = intent.getParcelableExtra(MovieInfo.class.getName());
            ImageView imageView = (ImageView) rootView.findViewById(R.id.poster_imageview);
            Picasso.with(getContext()).load(info.posterPath).into(imageView);
            TextView overview = (TextView) rootView.findViewById(R.id.overview_textview);
            overview.setText(info.overview);
            TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date_textview);
            releaseDate.setText(info.release_date);
            TextView title = (TextView) rootView.findViewById(R.id.original_title_textview);
            title.setText(info.original_title);
            TextView voteAvg = (TextView) rootView.findViewById(R.id.vote_average_textview);
            voteAvg.setText(Double.toString(info.vote_average));
        }
        return rootView;
    }
}
