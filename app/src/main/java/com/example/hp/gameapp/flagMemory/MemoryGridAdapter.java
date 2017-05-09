package com.example.hp.gameapp.flagMemory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

class MemoryGridAdapter extends BaseAdapter{
    private ArrayList<String> elements = new ArrayList<>();
    private HashMap<String, Integer> pathToID = new HashMap<>();
    private Context c;
    private ArrayList<Integer> states;

    MemoryGridAdapter(Context c, ArrayList<String> array, ArrayList<Integer> states){
        elements = array;
        for (String key: array) {
            if(!pathToID.containsKey(key)) pathToID.put(key, pathToID.size());
        }
        this.c = c;
        this.states = states;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public Object getItem(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlagBox imageView;

        if (convertView == null) {
            imageView = new FlagBox(c, elements.get(position), pathToID.get(elements.get(position)));
            imageView.setLayoutParams(new GridView.LayoutParams(200, 160));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if(states.get(position)==1) {
                imageView.closeFlag();
                imageView.setClicker();
            }
            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (FlagBox) convertView;
        }
        return imageView;

    }

}
