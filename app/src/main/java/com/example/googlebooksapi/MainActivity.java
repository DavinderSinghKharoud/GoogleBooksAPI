package com.example.googlebooksapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements androidx.loader.app.LoaderManager.LoaderCallbacks<List<Books>> {


    private EditText searchQuery;

    private ListView booksListView;

    private static String Books_Request_URL = null;

    private BooksAdapter mAdapter;

    private static final int BOOKS_LOADER_ID = 1;

    private ProgressBar loadingIndicator;

    private ConnectivityManager connMgr;

    private NetworkInfo networkInfo;

    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //giving the reference to the layout
        searchQuery = (EditText) findViewById(R.id.editText);
        booksListView = (ListView) findViewById(R.id.listView);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        //Create a new Adapter that takes an empty list of books as input.
        mAdapter = new BooksAdapter(this, new ArrayList<Books>());

        booksListView.setAdapter(mAdapter);

        //Assigning the progressBar
        loadingIndicator = (ProgressBar) findViewById(R.id.simpleProgressBar);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long l) {

                Books currentBook = mAdapter.getItem(positon);

                //Convert the String URL into a URI object ( to pass into the intent constructor)
                Uri bookUri = Uri.parse(currentBook.getURL());

                //Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                startActivity(websiteIntent);


            }
        });


    }

    /**
     * This method will be called, when user click on search button
     */

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void SearchButton(View view) {

        //getting the complete url for fetch the data
        Books_Request_URL = getQuery();

        //Get a reference to the ConnectivityManager to check state of network connectivity
        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get details on the currently active default data network
        networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {

            //If the user does not write anything for search, it will call setUpViewNoBooks() method
            // to display "No books found".
            if(Books_Request_URL==null){
                setUpViewNoBooks();
                return;
            }


            //Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getSupportLoaderManager();

            //we are using loaderManger.restartLoader(), instead of loaderManager.initLoader
            //because we need to restart the loader each time the user enter some new query and search.
            loaderManager.restartLoader(BOOKS_LOADER_ID, null, this);

            //Setting the progressBar to visible
            loadingIndicator.setVisibility(View.VISIBLE);

        } else {
            //calling setUpViewNoInternet method to display no internet connection
            setUpViewNoInternet();
        }


    }


    /**
     * Return the complete URL, by combining user search hint for books
     */

    private String getQuery() {
        String userHint=searchQuery.getText().toString();
        if(!userHint.isEmpty()){
            return "https://www.googleapis.com/books/v1/volumes?q=" + userHint;
        }else {
            return null;
        }

    }


    @Override
    public Loader<List<Books>> onCreateLoader(int id, @Nullable Bundle args) {
        //Create a new loader for the given URL
        return new BooksLoader(this, Books_Request_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Books>> loader, List<Books> books) {

        //If there is a valid list of {@link Books} and Adapter is empty, then add them to the adapter
        //data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty() && mAdapter.isEmpty()) {
            mAdapter.addAll(books);
        } else if (!mAdapter.isEmpty()) {
            //else clear the adapter first to make sure delete the old data from listView
            //and add new data
            mAdapter.clear();
            mAdapter.addAll(books);
        } else {
            //calling setUpViewNoBook method to display no books found
            setUpViewNoBooks();
        }

        //Changing the progress Bar to invisible
        loadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Books>> loader) {
        //Loader reset, so we can clear out our existing data.
        Toast.makeText(this, "Reset", Toast.LENGTH_SHORT).show();
    }


    /**
     * Method used to Setting ListView as empty text view and
     * updating empty state with no connection error message
     */
    private void setUpViewNoInternet() {
        mAdapter.clear();
        mEmptyStateTextView.setText(R.string.no_internet_connection);
        booksListView.setEmptyView(mEmptyStateTextView);
    }

    /**
     * Method used to Setting ListView as empty text view and
     * updating empty state with no Books error message
     */
    private void setUpViewNoBooks() {
        mAdapter.clear();
        mEmptyStateTextView.setText(R.string.no_books);
        booksListView.setEmptyView(mEmptyStateTextView);

    }
}
