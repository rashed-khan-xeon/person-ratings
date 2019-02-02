package com.rashedkhan.ratings.core;

/**
 * Created by arifk on 27.12.17.
 */

public interface ResponseListener<T> {
    void success(T response);

    void error(Throwable error);

}
