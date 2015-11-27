package com.cogniance.flickrexposure;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class FlickrXmlPullParser {

    private final String LOG_TAG = this.getClass().getName();

    private List<String> imageTitlesArray = new ArrayList<>();

    private List<String> imageExtrasAttributesArray = new ArrayList<>();

    public List<String> getImageTitlesArray() {
        return imageTitlesArray;
    }

    public List<String> getImageExtrasAttributesArray() {
        return imageExtrasAttributesArray;
    }

    public List<String> parse(String xmlString) {

        XmlPullParserFactory factory;
        XmlPullParser parser;

        List<String> imageURLArray = new ArrayList<>();
        String idAttributeValue;
        String secretAttributValue;
        String serverAttributValue;
        String farmAttributValue;
        
        String titleAttributValue;
        String dateUploadAttributValue;
        String ownerNameAttributValue;

        final String PHOTO = "photo";
        final String ID = "id";
        final String SECRET = "secret";
        final String SERVER = "server";
        final String FARM = "farm";
        final String TITLE = "title";
        final String DATE_UPLOAD = "dateupload";
        final String OWNER_NAME= "ownername";
        final String EMPTY_STRING = "";

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(new StringReader(xmlString));

            int i = 0;
            while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (parser.getEventType() == XmlPullParser.START_TAG && PHOTO.equals(parser.getName())) {
                    parser.getNamespace();
                    idAttributeValue = parser.getAttributeValue(EMPTY_STRING, ID);
                    secretAttributValue = parser.getAttributeValue(EMPTY_STRING, SECRET);
                    serverAttributValue = parser.getAttributeValue(EMPTY_STRING, SERVER);
                    farmAttributValue = parser.getAttributeValue(EMPTY_STRING, FARM);
                    titleAttributValue = parser.getAttributeValue(EMPTY_STRING, TITLE);

                    dateUploadAttributValue = parser.getAttributeValue(EMPTY_STRING, DATE_UPLOAD);
                    ownerNameAttributValue = parser.getAttributeValue(EMPTY_STRING, OWNER_NAME);

                    //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[size_suffixe_letter].jpg
                    imageURLArray.add("https://farm" + farmAttributValue + ".staticflickr.com/" + serverAttributValue + "/" + idAttributeValue + "_" + secretAttributValue + "_q.jpg");

                    imageTitlesArray.add(titleAttributValue);

                    imageExtrasAttributesArray.add(i, dateUploadAttributValue); //odd index
                    imageExtrasAttributesArray.add(i + 1, ownerNameAttributValue); //even index
                    i += 2;
                }
                parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        return imageURLArray;
    }
}
