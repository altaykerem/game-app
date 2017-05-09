package com.example.hp.gameapp.flagMemory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.example.hp.gameapp.R;

import java.io.IOException;
import java.io.InputStream;

public class FlagBox extends android.support.v7.widget.AppCompatImageView{
    private int flagID;
    private Bitmap flag;
    private int closedResource;
    private boolean isOpen = true;

    public FlagBox(Context c, String path, int flagID) {
        super(c);
        this.flagID = flagID;
        AssetManager manager = c.getAssets();
        InputStream streamImage = null;
        try {streamImage = manager.open(path);
        } catch (IOException e) {e.printStackTrace();}
        flag = BitmapFactory.decodeStream(streamImage);
        this.setImageBitmap(flag);
        this.closedResource = R.drawable.question_mark;
    }

    public void setClicker(){
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFlag();
            }
        });
    }

    public void stopListener(){
        this.setOnClickListener(null);
    }

    public void closeFlag(){
        this.setImageResource(closedResource);
        isOpen = false;
    }
    public void openFlag(){
        this.setImageBitmap(flag);
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getFlagID() {
        return flagID;
    }
}
