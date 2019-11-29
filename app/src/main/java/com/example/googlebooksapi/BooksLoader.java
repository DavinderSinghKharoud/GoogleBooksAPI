package com.example.googlebooksapi;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

/**
 *Loads a list of Books by using an AsyncTask to perform the
 *network request to the given URL.
 */
public class BooksLoader extends AsyncTaskLoader<List<Books>> {


    private static final String LOG_TAG=BooksLoader.class.getName();

    private String mURL;

    /**
     *Construct a new {@link BooksLoader}.
     * @param context of the activity
     * @param url to load data from
     */

    public BooksLoader(@NonNull Context context,String url) {
        super(context);
        mURL=url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     *This is on a background thread.
     */
    @Override
    public List<Books> loadInBackground() {
        if(mURL==null){
            return null;
        }

        //perform the network request, parse the response, and extract a list of earthquakes.
        List<Books> books=QueryUtils.fetchBooksData(mURL);

        return books;
    }
}
