package com.gelerion.learning.rx.v6.window;

/**
 * Created by denis.shuvalov on 21/12/2017.
 *
 * debounce() (alias: throttleWithTimeout()) discards all of the events that are shortly followed by another event.
 */
public class ThrottlingWithTimeout {

    public static void main(String[] args) {
//        Observable<BigDecimal> prices = tradingPlatform.pricesOf("NFLX");
//        Observable<BigDecimal> debounced = prices.debounce(100, MILLISECONDS);

/*      In other words, if a given event is not followed by another event within a time window, that event is emitted.
        In the preceding example, the prices stream pushes prices of "NFLX" stock every time they change.
        Prices sometimes change very frequently, dozens of times per second. For each price change we would
        like to run some computation that takes a significant amount of time to complete. However, if a new price
        arrives, the result of this computation is irrelevant; it must begin from scratch with this new price.
        Therefore, we would like to discard events if they are followed (suppressed by) a new event shortly after.
        */

//        prices
//                .debounce(x -> {
//                    boolean goodPrice = x.compareTo(BigDecimal.valueOf(150)) > 0;
//                    return Observable
//                            .empty()
//                            .delay(goodPrice? 10 : 100, MILLISECONDS);
//                })

/*        debounce() waits a little bit (100 milliseconds in the preceding example) just in case second event appears
        later on. This process repeats itself so that if a second event appears in less than 100 milliseconds from
        the first one, RxJava will postpone its emission, hoping for the third one to appear. This time, again, you
        have an option to flexibly control for how long to wait on a per-event basis. For example, you might want to
        ignore stock price changes if they are followed by an update in less than 100 milliseconds. However, if the
        price goes above $150, we would like to forward such an update downstream much faster without hesitation.
        Maybe because some types of events need to be handled straight away; for example, because they are great
        market opportunities. you can implement this easily by using an overloaded version of debounce()*/
    }
}
