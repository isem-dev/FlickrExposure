package com.cogniance.flickrexposure;

public class FlickrUser {

    public static final String apiKey = "cb7c831c5707798e8f9d522d81f9887a";
    public static final String userID = "28634332@N05"; //https://www.flickr.com/photos/nasamarshall/
    public static final String flickrURL = "https://api.flickr.com/services/rest/?method=flickr.people.getPublicPhotos&user_id=" + userID + "&api_key=" + apiKey + "&per_page=500" + "&extras=date_upload,owner_name";
//    public static final String flickrURL = "https://api.flickr.com/services/rest/?method=flickr.people.getPhotosOf&user_id=" + userID + "&api_key=" + apiKey + "&per_page=500" + "&extras=date_upload,owner_name";
}
