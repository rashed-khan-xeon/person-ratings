package com.review.ratings.core;

import android.content.Context;
import android.media.Image;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.review.ratings.util.LruBitmapCache;

import java.util.Date;

/**
 * Created by arifk on 27.12.17.
 */

public class RtClients {

    private static RtClients rtClients = null;
    private RequestQueue requestQueue = null;
    private Gson gson = null;
    GsonRequest request = null;
    private ImageLoader imageLoader = null;

    private RtClients() {
    }

    public static RtClients getInstance() {

        if (rtClients == null) {
            rtClients = new RtClients();
        }

        return rtClients;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public ImageLoader getImageLoader(Context context) {
        getRequestQueue(context);
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new LruBitmapCache());
        }
        return imageLoader;
    }

    public Gson getGson() {
        if (gson != null) {
            return gson;
        }
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateSerializer())
                .registerTypeAdapter(Date.class, new DateDeserializer());
        return gsonBuilder.create();

    }
}
