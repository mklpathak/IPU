package com.jesushghar.uss.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mukul on 11-04-2016.
 */
public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Date ConvertToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage().toString());
        }
        return convertedDate;
    }
}

