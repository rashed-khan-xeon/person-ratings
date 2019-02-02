package com.rashedkhan.ratings.data.repository;

import com.rashedkhan.ratings.core.ResponseListener;

import java.util.List;
import java.util.Map;

/**
 * Created by arifk on 29.12.17.
 */

public interface IHttpRepository {
    <T> void get(String path,Class<T> clazz, Map<String, String> header, ResponseListener<T> responseListener);

    <T> void getAll(String path,Class<T[]> clazz, Map<String, String> header, ResponseListener<List<T>> responseListener);

    <T> void post(String path,Class<T> clazz, String body, Map<String, String> header, ResponseListener<T> responseListener);
}
