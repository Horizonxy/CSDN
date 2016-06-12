package com.horizon.csdn.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public class SmallPicInfo implements Serializable {
    public String data;
    public int left;
    public int top;
    public int width;
    public int height;
    public int position;
    public byte[] bmp;

    public SmallPicInfo(String data, int left, int top, int width, int height, int position, Bitmap bmp) {
        this.data = data;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.position = position;
        if(bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            this.bmp = baos.toByteArray();
        }
    }


}