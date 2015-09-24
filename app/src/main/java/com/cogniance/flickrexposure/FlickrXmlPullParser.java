package com.cogniance.flickrexposure;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class FlickrXmlPullParser {

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
        XmlPullParser xmlPullParser;

        List<String> imageURLArray = new ArrayList<>();
        String idAttributeValue;
        String secretAttributValue;
        String serverAttributValue;
        String farmAttributValue;
        
        String titleAttributValue;
        String dateUploadAttributValue;
        String ownerNameAttributValue;

        try {
            factory = XmlPullParserFactory.newInstance();
            xmlPullParser = factory.newPullParser();


            xmlPullParser.setInput(new StringReader(xmlString));

            int i = 0;
            while (xmlPullParser.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("photo")) {
                    idAttributeValue = xmlPullParser.getAttributeValue(0);
                    secretAttributValue = xmlPullParser.getAttributeValue(2);
                    serverAttributValue = xmlPullParser.getAttributeValue(3);
                    farmAttributValue = xmlPullParser.getAttributeValue(4);
                    titleAttributValue = xmlPullParser.getAttributeValue(5);

                    dateUploadAttributValue = xmlPullParser.getAttributeValue(9);
                    ownerNameAttributValue = xmlPullParser.getAttributeValue(10);

                    //https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}_[size_suffixe_letter].jpg
                    imageURLArray.add("https://farm" + farmAttributValue + ".staticflickr.com/" + serverAttributValue + "/" + idAttributeValue + "_" + secretAttributValue + "_q.jpg");
                    
                    imageTitlesArray.add(titleAttributValue);
                    
                    imageExtrasAttributesArray.add(i, dateUploadAttributValue); //odd index
                    imageExtrasAttributesArray.add(i + 1, ownerNameAttributValue); //even index
                    i += 2;
                }
                xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return imageURLArray;
    }
}
