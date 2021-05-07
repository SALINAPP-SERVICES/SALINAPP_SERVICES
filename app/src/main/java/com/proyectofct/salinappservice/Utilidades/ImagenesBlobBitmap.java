package com.proyectofct.salinappservice.Utilidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.sql.Blob;
import java.sql.SQLException;

public class ImagenesBlobBitmap {

    //método que convierte un blob a un Bitmap
    public static Bitmap blob_to_bitmap(Blob b, int width, int height){
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height,config);
        try{
            int blobLength = (int) b.length();
            byte[] blobAsBytes = b.getBytes(1, blobLength);
            bitmap = BitmapFactory.decodeByteArray(blobAsBytes, 0, blobAsBytes.length);
        }catch (Exception e){
            return null;
        }
        return bitmap;
    }

    //método que convierte un blob a byte[]
    public static byte[] blob_to_bytes(Blob b){
        int blobLength = 0;
        byte[] blobAsBytes = new byte[0];
        try {
            blobLength = (int) b.length();
            blobAsBytes = b.getBytes(1, blobLength);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return blobAsBytes;
    }

    //método que convierte byte[] a bitmap
    public static Bitmap bytes_to_bitmap(byte[] b){
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(200, 200,config);
        try{
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e){
        }
        return bitmap;
    }

}
