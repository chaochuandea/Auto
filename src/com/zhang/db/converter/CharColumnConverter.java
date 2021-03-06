package com.zhang.db.converter;

import android.database.Cursor;
import android.text.TextUtils;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class CharColumnConverter implements ColumnConverter<Character> {
    @Override
    public Character getFiledValue(final Cursor cursor, int index) {
        return (char) cursor.getInt(index);
    }

    @Override
    public Character getFiledValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) return null;
        return fieldStringValue.charAt(0);
    }

    @Override
    public Object fieldValue2ColumnValue(Character fieldValue) {
        if (fieldValue == null) return null;
        return (int) fieldValue;
    }

    @Override
    public String getColumnDbType() {
        return "INTEGER";
    }
}
