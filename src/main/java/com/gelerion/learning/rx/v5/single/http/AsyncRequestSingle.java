package com.gelerion.learning.rx.v5.single.http;


import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import rx.Single;
import rx.SingleSubscriber;

import java.io.IOException;


/**
 * Created by denis.shuvalov on 20/12/2017.
 */
public class AsyncRequestSingle {

    public static void main(String[] args) {
        Single<String> example = fetch("www.example.com")
                .flatMap(AsyncRequestSingle::body);

        String value = example.toBlocking().value();
    }

    public static Single<Response> fetch(String url) {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        return Single.create(subscriber ->
                httpClient
                        .prepareGet(url)
                        .execute(handle(subscriber)));
    }

    private static AsyncCompletionHandler<Response> handle(SingleSubscriber<? super Response> subscriber) {
        return new AsyncCompletionHandler<>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                //not onNext!
                subscriber.onSuccess(response);
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                subscriber.onError(t);
            }
        };
    }

    //Same functionality as body():
    static Single<String> body(Response response) {
        return Single.fromCallable(response::getResponseBody);
    }

    static Single<String> bodyOld(Response response) {
        return Single.create(subscriber -> {
            try {
                subscriber.onSuccess(response.getResponseBody());
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }
}
