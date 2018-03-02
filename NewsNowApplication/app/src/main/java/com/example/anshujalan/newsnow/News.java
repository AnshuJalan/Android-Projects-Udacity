package com.example.anshujalan.newsnow;

/**
 * Created by AnshuJalan on 2/4/2018.
 */

public class News
{
    //Web title
    private String mTitle;

    //Type. Eg: article or live blog
    private String mType;

    //web url
    private String mUrl;

    //constructor
    public News(String title, String type, String url)
    {
        mTitle = title;
        mType = type;
        mUrl = url;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public String getType()
    {
        return mType;
    }

    public String getUrl()
    {
        return mUrl;
    }
}
