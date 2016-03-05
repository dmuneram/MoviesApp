package com.munera.android.moviesapp.app;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    MovieArrayAdapter adapter = null;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        adapter = new MovieArrayAdapter(getActivity(), new ArrayList<MovieInfo>());
        GridView gridViewMovie = (GridView) view.findViewById(R.id.grid_view_movie);
        gridViewMovie.setAdapter(adapter);

        gridViewMovie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieInfo item = adapter.getItem(position);
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra(MovieInfo.class.getName(), item);
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateMovieList() {
        String sortKey = getString(R.string.pref_sort_key);
        String sortVal = getString(R.string.pref_sort_default);
        String sort = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(sortKey, sortVal);

        FetchMovieListTask fetchMovieListTask = new FetchMovieListTask();
        fetchMovieListTask.execute(sort);
    }

    public class FetchMovieListTask extends AsyncTask<String, Void, List<MovieInfo>> {

        private static final String BASE_IMG_PATH = "http://image.tmdb.org/t/p/w500";
        private static final String BASE_URL =
                "http://api.themoviedb.org/3/discover/movie?api_key=824f2d24c2809df5725d43e415ebe937";
        final String LOG_TAG = "FetchMovieListTask";

        @Override
        protected void onPostExecute(List<MovieInfo> result) {
            if (result != null) {
                adapter.clear();
                adapter.addAll(result);
            }
        }

        @Override
        protected List<MovieInfo> doInBackground(String... params) {
            Log.d(LOG_TAG, "Execute task");
            if (params == null || params.length == 0){
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            List<MovieInfo> movieList = new ArrayList<MovieInfo>();
            try {
                Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
                builder.appendQueryParameter("sort_by",params[0]);
                URL url = new URL(builder.toString());
                Log.d(LOG_TAG, "URL: " + url);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = null;
                try {
                    inputStream = urlConnection.getInputStream();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieList = getMovieInfo(buffer.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            Log.d(LOG_TAG, "Execute task finish");
            return movieList;
        }

        private List<MovieInfo> getMovieInfo(String jsonStr) throws JSONException {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray movieListJson = jsonObject.getJSONArray("results");
            List<MovieInfo> pathList = new ArrayList<MovieInfo>();
            for (int i = 0; i < movieListJson.length(); i++) {
                JSONObject movieJson = movieListJson.getJSONObject(i);

                Long id = movieJson.getLong("id");
                String posterPath = BASE_IMG_PATH + movieJson.getString("poster_path");
                String overview = movieJson.getString("overview");
                String release_date = movieJson.getString("release_date");
                String original_title = movieJson.getString("original_title");
                Double vote_average = movieJson.getDouble("vote_average");
                MovieInfo movieInfo = new MovieInfo(id,posterPath,overview,release_date,original_title,vote_average);
                pathList.add(movieInfo);
            }
            return pathList;
        }
    }
}
