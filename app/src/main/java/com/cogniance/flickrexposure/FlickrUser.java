package com.cogniance.flickrexposure;

public class FlickrUser {

    public static final String apiKey = "cb7c831c5707798e8f9d522d81f9887a";
//    public static final String userID = "54410418@N08"; //www.flickr.com/people/nasa_images/ : User not found
    public static final String userID = "37070332@N08"; //www.flickr.com/people/zavadsky/
    public static final String flickrURL = "https://api.flickr.com/services/rest/?method=flickr.people.getPhotosOf&user_id=" + userID + "&api_key=" + apiKey + "&per_page=500" + "&extras=date_upload,owner_name";
    //https://www.flickr.com/services/api/flickr.people.getPhotosOf.html
}
