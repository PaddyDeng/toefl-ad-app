package io.dcloud.H58E83894.utils;

import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import io.dcloud.H58E83894.ToeflApplication;

/**
 * Created by li.liu on 2017/6/14.
 */

public class Sharedprefence {
    public static final int STRING = 0;
    public static final int INT = 1;
    public static final int BOOLEAN = 2;

    public Sharedprefence() {
    }


    public static void ClearSharedPreferences(String dataBasesName) {
        SharedPreferences user = ToeflApplication.app.getSharedPreferences(dataBasesName, 0);
        SharedPreferences.Editor editor = user.edit();
        editor.clear();
        editor.commit();
    }


    public static void removeSharedPreferences(String dataBasesName, String key) {
        SharedPreferences user = ToeflApplication.app.getSharedPreferences(dataBasesName, 0);
        SharedPreferences.Editor editor = user.edit();
        editor.remove(key);
        editor.commit();
    }

    public static SharedPreferences ReadSharedPreferences(String dataBasesName) {
        SharedPreferences user = ToeflApplication.app.getSharedPreferences(dataBasesName, 0);
        return user;
    }

    public static HashMap<String, Object> getAllByBasesName(String dataBasesName) {
        SharedPreferences user = ToeflApplication.app.getSharedPreferences(dataBasesName, 0);
        HashMap map = (HashMap) user.getAll();
        return map;
    }

    public static <T> T getValueByName(String dataBasesName, String key, int type) {
        SharedPreferences user = ToeflApplication.app.getSharedPreferences(dataBasesName, 0);
        Object value = null;
        switch (type) {
            case 0:
                value = user.getString(key, "");
                break;
            case 1:
                value = Integer.valueOf(user.getInt(key, 0));
                break;
            case 2:
                value = Boolean.valueOf(user.getBoolean(key, false));
        }

        return (T) value;
    }

    public static void WriteSharedPreferences(String dataBasesName, String name, Object value) {
        if (name != null && value != null) {
            SharedPreferences user = ToeflApplication.app.getSharedPreferences(dataBasesName, 0);
            SharedPreferences.Editor editor = user.edit();
            if (value instanceof Integer) {
                editor.putInt(name, Integer.parseInt(value.toString()));
            } else if (value instanceof Long) {
                editor.putLong(name, Long.parseLong(value.toString()));
            } else if (value instanceof Boolean) {
                editor.putBoolean(name, Boolean.parseBoolean(value.toString()));
            } else if (value instanceof String) {
                editor.putString(name, value.toString());
            } else if (value instanceof Float) {
                editor.putFloat(name, Float.parseFloat(value.toString()));
            }

            editor.commit();
        }
    }

    public static void saveObject(String data, String key, Object object) {
        SharedPreferences preferences = ToeflApplication.app.getSharedPreferences(data, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream e = new ObjectOutputStream(baos);
            e.writeObject(object);
            String oAuth_Base64 = new String(Base64.encode(baos.toByteArray(), 0));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, oAuth_Base64);
            editor.commit();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

    }

    public static <T> T readObject(String data, String key) {
        SharedPreferences preferences = ToeflApplication.app.getSharedPreferences(data, 0);
        return readObject(preferences, key);
    }

    public static <T> T readObject(SharedPreferences preferences, String key) {
        Object object = null;
        String string = preferences.getString(key, "");
        if (string == "") {
            return null;
        } else {
            byte[] base64 = Base64.decode(string.getBytes(), 0);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);

            try {
                ObjectInputStream e = new ObjectInputStream(bais);
                object = e.readObject();
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            return (T) object;
        }
    }

    public static <T> HashMap<String, T> readObject(String data) {
        SharedPreferences preferences = ToeflApplication.app.getSharedPreferences(data, 0);
        return readObject(preferences);
    }

    public static <T> HashMap<String, T> readObject(SharedPreferences preferences) {
        Object object = null;
        HashMap map = (HashMap) preferences.getAll();
        HashMap datas = new HashMap();
        Iterator var5 = map.keySet().iterator();

        while (var5.hasNext()) {
            String key = (String) var5.next();
            byte[] base64 = Base64.decode(((String) map.get(key)).getBytes(), 0);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64);

            try {
                ObjectInputStream e = new ObjectInputStream(bais);
                object = e.readObject();
                datas.put(key, object);
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        return datas;
    }
}
