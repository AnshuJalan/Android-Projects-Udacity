package com.example.anshujalan.newsnow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AnshuJalan on 2/4/2018.
 */

public class NewsAdapter extends ArrayAdapter<News>
{

    public NewsAdapter(Context context, ArrayList<News> news)
    {
        super(context, 0, news);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        ImageView newstypeView = (ImageView) listItemView.findViewById(R.id.news_type);
        TextView newsTitleView = (TextView) listItemView.findViewById(R.id.news_title);



        if (currentNews != null)
        {
            if(currentNews.getType().equals("article"))
            {
                newstypeView.setImageResource(R.mipmap.ic_a);
            }
            else
            {
                newstypeView.setImageResource(R.mipmap.ic_b);
            }

            newsTitleView.setText(currentNews.getTitle());
        }


        return listItemView;
    }
}
