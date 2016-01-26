package de.eknoes.speakerslist;

import spark.ResponseTransformer;
import com.google.gson.*;

/**
 * speakerslist
 * Created by soenke on 21.01.16.
 */
public class JsonUtil {


    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}