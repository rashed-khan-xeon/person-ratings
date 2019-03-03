package com.rashedkhan.ratings.core;


public interface ResponseListener<T> {
    void success(T response);

    void error(Throwable error);

}
