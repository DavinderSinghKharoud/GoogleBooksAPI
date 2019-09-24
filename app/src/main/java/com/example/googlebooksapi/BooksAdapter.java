package com.example.googlebooksapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BooksAdapter extends ArrayAdapter<Books> {

    /**
     * Construct a new {@link BooksAdapter}.
     *
     * @param context of the app
     * @param books   is the list of books, which is the data source of the adapter.
     */
    public BooksAdapter(Context context, List<Books> books) {
        super(context, 0, books);
    }

    /**
     * @return a list view that displays information about the book at the given position in the
     * list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if there is an existing list item view that we can reuse, otherwise
        //if convertView is null, then inflate new list item layout.

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.books_list_item, parent, false);
        }

        //Find the book at the given postion in the list of books.
        Books currentBook = getItem(position);

        //Find the ImageView with view ID imageURL
        ImageView imageURL=(ImageView) listItemView.findViewById(R.id.imageURL);

        //Parsing the imageURL from JSON
        String BookImagesUrl=currentBook.getImage();
        //Using Picasso library for fetching image from URL
        Picasso.with(getContext()).load(BookImagesUrl).into(imageURL);


        //Find the TextView with  view ID title
        TextView title=(TextView)listItemView.findViewById(R.id.title);
        //Setting the title
        title.setText(currentBook.getTitle());

        //Find the TextView with  view ID pages
        TextView pages=(TextView)listItemView.findViewById(R.id.pages);
        //Setting the pages
        pages.setText("Pages:"+currentBook.getPages());

        //Find the TextView with view ID author
        TextView author=(TextView)listItemView.findViewById(R.id.author);
        //Setting the author
        author.setText("By:"+currentBook.getAuthor());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
