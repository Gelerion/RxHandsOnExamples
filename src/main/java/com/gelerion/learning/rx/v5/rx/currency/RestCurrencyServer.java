package com.gelerion.learning.rx.v5.rx.currency;

import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import java.math.BigDecimal;

/**
 * Created by denis.shuvalov on 19/12/2017.
 */
public class RestCurrencyServer {

    private static final BigDecimal RATE = new BigDecimal("1.06448");

    /**
     * curl -v localhost:8080/10.99
     * > GET /10.99 HTTP/1.1
     * > User-Agent: curl/7.35.0
     * > Host: localhost:8080'
     * > Accept:
     *  < HTTP/1.1 200 OK
     *  < transfer-encoding: chunked
     *  <
     * {"EUR": 10.99, "USD": 11.6986352}
     */
    public static void main(String[] args) {
        HttpServer
                .newServer(8080)
                .start((req, resp) -> {
                    String amountStr = req.getDecodedPath().substring(1);
                    BigDecimal amount = new BigDecimal(amountStr);
                    Observable<String> response = Observable
                            .just(amount)
                            .map(eur -> eur.multiply(RATE))
                            .map(usd ->
                                    "{\"EUR\": " + amount + ", " +
                                     "\"USD\": " + usd + "}");
                    return resp.writeString(response);
                })
                .awaitShutdown();
    }

}
