package com.example.googlebooksapi;

public class Books {

    //Title of the book
    private String mTitle;

    //Author of the book
    private String mAuthor;

    //Pages of the book
    private String mPages;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    //Image of the book
    private String mImage;

    private String price;

    //URL link to get more information of the book
    private String mURL;

    /**
     * Constructs a new {@link Books} object.
     *
     * @param Title  is the Title of the book.
     * @param Author is the Author of the book.
     * @param Pages  is the count of pages of book.
     * @param Image  is the URL to get the image of the book.
     * @param URL    is the URL to get more info about the book.
     */
    public Books(String Title, String Author, String Pages, String Image, String URL,String price) {
        this.mTitle = Title;
        this.mAuthor = Author;
        this.mPages = Pages;
        this.mImage = Image;
        this.mURL = URL;
        this.price=price;
    }

    //Returns the title of the book
    public String getTitle() {
        return mTitle;
    }

    //Returns the Author of the book
    public String getAuthor() {
        return mAuthor;
    }

    //Returns the Pages of the book
    public String getPages() { return mPages; }

    //Returns the ImageLink of the book
    public String getImage() {
        return mImage;
    }

    //Returns the URL to get more info about the book
    public String getURL() {
        return mURL;
    }

}
