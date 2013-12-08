package com.zhang.json.parser.deserializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

import com.zhang.json.JSONException;
import com.zhang.json.parser.DefaultJSONParser;
import com.zhang.json.parser.JSONToken;

public class DateFormatDeserializer extends AbstractDateDeserializer implements ObjectDeserializer {

    public final static DateFormatDeserializer instance = new DateFormatDeserializer();

    @SuppressWarnings("unchecked")
    protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
        
        if (val == null) {
            return null;
        }

        if (val instanceof String) {
            String strVal = (String) val;
            if (strVal.length() == 0) {
                return null;
            }
            
            return (T) new SimpleDateFormat(strVal);
        }

        throw new JSONException("parse error");
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
