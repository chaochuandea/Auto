package com.zhang.db.converter;

import android.database.Cursor;

/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class StringColumnConverter implements ColumnConverter<String> {
    @Override
    public String getFiledValue(final Cursor cursor, int index) {
        return cursor.getString(index);
    }

    @Override
    public String getFiledValue(String fieldStringValue) {
        return fieldStringValue;
    }

    @Override
    public Object fieldValue2ColumnValue(String fieldValue) {
        return fieldValue;
    }

    @Override
    public String getColumnDbType() {
        return "TEXT";
    }
}
