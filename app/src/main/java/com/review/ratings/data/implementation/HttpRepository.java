package com.review.ratings.data.implementation;

import android.content.Context;

import com.android.volley.Request;
import com.review.ratings.core.GsonRequest;
import com.review.ratings.core.ResponseListener;
import com.review.ratings.core.RtClients;
import com.review.ratings.data.repository.IHttpRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 29.12.17.
 */

public class HttpRepository implements IHttpRepository {
    private Context con;

    public HttpRepository(Context context) {
        this.con = context;
    }


    @Override
    public <T> void get(String path, Class<T> clazz, Map<String, String> header, ResponseListener<T> responseListener) {
        GsonRequest<T> request = new GsonRequest<T>(Request.Method.GET, path, clazz, header, response -> responseListener.success((T) response), responseListener::error);
        RtClients.getInstance().getRequestQueue(con).add(request);
    }

    @Override
    public <T> void getAll(String path, Class<T[]> clazz, Map<String, String> header, ResponseListener<List<T>> responseListener) {
        GsonRequest<T[]> request = new GsonRequest<T[]>(Request.Method.GET, path, clazz, header, response -> responseListener.success(Arrays.asList(response)), responseListener::error);
        RtClients.getInstance().getRequestQueue(con).add(request);
    }

    @Override
    public <T> void post(String path, Class<T> clazz, String body, Map<String, String> header, ResponseListener<T> responseListener) {
        GsonRequest<T> request = new GsonRequest<T>(Request.Method.POST, path, clazz, header, responseListener::success, responseListener::error, body);
        RtClients.getInstance().getRequestQueue(con).add(request);
    }

}
