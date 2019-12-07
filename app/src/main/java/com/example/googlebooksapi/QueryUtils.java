package com.example.googlebooksapi;

//Helper methods to request and receive books data from GoogleBooksAPI

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    /**
     * Method used to return the list of {@link Books} objects.
     */

    public static List<Books> fetchBooksData(String requestURL) {
        //Create URL object
        URL url = createUrl(requestURL);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making the HTTP request.", e);
        }

        //Extract relevant field from the JSON response and create a list of{@link Books}
        List<Books> books = extractFeatureFromJSON(jsonResponse);

        // Return the list of {@link Books}
        return books;

    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return empty
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If the request was successful (response code 200),
            //then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem retreiving json results of books.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converting the {@param inputStream} in to String.
     *
     * @return the JSON response from the server
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }

        }
        return output.toString();
    }

    /**
     * @param stringUrl
     * @return new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    private static List<Books> extractFeatureFromJSON(String booksJSON) {
        //If the JSON response is empty or null, then return null
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }

        //Create an empty ArrayLis, so that we can add books to it.
        List<Books> books = new ArrayList<>();

        try {

            //Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for (int index = 0; index < booksArray.length(); index++) {

                //Get a single book at specific index within the list of books
                JSONObject currentBook = booksArray.getJSONObject(index);

                JSONObject info = currentBook.getJSONObject("volumeInfo");

                // Extract the value for the key called "title"
                String tempTitle=info.getString("title");

                String title="";
                if(tempTitle.length() > 25) {
                    title = tempTitle.substring(0, 25) + "...";
                }
                else{
                    title=tempTitle;
                }



                String author;
                String extraAuthors="";

                //if there are no authors it will set author to Not Specified
                try {

                    JSONArray authors = info.getJSONArray("authors");

                    //if there are more than one authors then, we can add "etc" at the end of author
                    if(authors.length()>1){
                        extraAuthors="etc.";
                    }

                    //and saving the name of author
                    author = authors.getString(0)+extraAuthors;

                }catch (Exception e){
                    author="Not Specified";
                }

                // Extract the value for the key called "pageCount"
                String pages;
                try {
                    pages = String.valueOf(info.getInt("pageCount"));

                }catch (Exception e){
                    pages="Not Specified";
                }


                String price;
                try {
                    JSONObject saleInfo=currentBook.getJSONObject("saleInfo");
                    price=saleInfo.getString("saleability");
                    if(price.equals("NOT_FOR_SALE")){
                        price="Not For Sale";
                    }else if(price.equals("FOR_SALE")){
                        price="For Sale";
                    }
                }catch (Exception e){
                    price="Not Specified";
                }

                JSONObject imageLink = info.getJSONObject("imageLinks");
                String imageURL = imageLink.getString("smallThumbnail");

                // Extract the value for the key called "infoLink"
                String infoURL = info.getString("infoLink");

                Books book = new Books(title, author, pages, imageURL, infoURL,price);

                books.add(book);


            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }
}
