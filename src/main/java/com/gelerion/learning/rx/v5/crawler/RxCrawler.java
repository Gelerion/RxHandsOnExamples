package com.gelerion.learning.rx.v5.crawler;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;

import java.net.URL;

/**
 * Created by denis.shuvalov on 19/12/2017.
 *
 * As opposed to callback-based reactive APIs, RxNetty plays very nicely with other Observables, you can easily
 * parallelize, combine, and split work. For example, imagine that you have a stream of URLs to which you must
 * connect and consume data in real time. This stream can be fixed (built from a simple List<URL>) or dynamic,
 * with new URLs appearing all the time. If you want a steady stream of packets flowing through all of these
 * sources, you can simply flatMap() over them
 */
public class RxCrawler {

    public static void main(String[] args) {
        /*
        This is a slightly contrived example because it mixes together ByteBuf messages from different sources,
        but you get the idea. For each URL in the upstream Observable, we produce an asynchronous stream of ByteByf
        instances from that URL. If you want to first transform incoming data, perhaps by combining chunks of data
        into a single event—you can do this easily, for example with reduce(). Here is the upshot: you can easily
        have tens of thousands of open HTTP connections, idle or receiving data. The limiting factor is no longer
        memory, but the processing power of your CPU and network bandwidth. JVM does not need to consume gigabytes
        of memory to process a reasonable number of transactions.
        */

        Observable<URL> sources = Observable.empty();

        Observable<ByteBuf> packets = sources
                .flatMap(url -> HttpClient
                        .newClient(url.getHost(), url.getPort())
                        .createGet(url.getPath()))
                .flatMap(HttpClientResponse::getContent);

        //Even if you carefully remove the blocking HTTP, communication, synchronous code can appear in the
        //most surprising places. It is a pitfall of the equals() method in java.net.URL that it makes a network request
    }

}
