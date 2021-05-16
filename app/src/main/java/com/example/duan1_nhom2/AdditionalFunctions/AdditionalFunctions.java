package com.example.duan1_nhom2.AdditionalFunctions;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.widget.TextView;
import android.widget.Toast;

public class AdditionalFunctions {
    public static boolean isStringEmpty(Context context, String... strings) {
        for (String i : strings) {
            if (i.isEmpty()) {
                Toast.makeText(context, "Không được để trống ô nhập!", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
    public static boolean isStringEmpty(String... strings) {
        for (String i : strings) {
            if (i.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmailValid(Context context, String... emails) {
        String regex = "\\w+@\\w+(.\\w+){1,2}";
        for (String i : emails) {
            if (!i.matches(regex)) {
                Toast.makeText(context, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public static boolean isPasswordValid(Context context, String... passwords) {
        for (String i : passwords) {
            if (i.length() < 7) {
                Toast.makeText(context, "Mật khẩu phải dài hơn 6 kí tự!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String changeTimeFormat(int endDuration) {
        int totalMin = Math.abs((endDuration / 60000));
        int totalSecond = (endDuration % 60000) / 1000;
        if (totalSecond < 10) {
            return totalMin + ":0" + totalSecond;
        } else {
            return totalMin + ":" + totalSecond;
        }
    }
    public static void changeTextInMilisecond(final TextView view, String replaceText, final String resetText){
        view.setText(replaceText);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setText(resetText);
            }
        }, 3000);
    }
    public static String getAudioDuration(Uri uri, Context context){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context,uri);
        int milisecond = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        String thoiLuong = changeTimeFormat(milisecond);
        return thoiLuong;
    }
    public static String getFileExtension(Context context, Uri uri){
        String fileName = getFileName(context, uri);
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
