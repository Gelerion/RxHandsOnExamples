package com.gelerion.learning.rx.v3.lift;

import rx.Observable;
import rx.internal.operators.*;

/**
 * Created by denis.shuvalov on 10/12/2017.
 *
 * Whereas compose() transforms Observables, lift() allows transforming Subscribers. When you subscribe() to an Observable,
 * the Subscriber instance wrapping your callback travels up to the Observable it subscribed to and causes Obsevable’s create()
 * method to be invoked with our subscriber as an argument (gross simplification)
 *
 * When we subscribe() at the very bottom, a Subscriber<String> instance is created and passed to the immediate predecessor.
 * It can be “true” Observable<String> that emits events or just the result of some operator, map(Integer::toHexString)
 * in our case. map() itself does not emit events, yet it received a Subscriber that wants to receive them. What map()
 * does (through the lift() helper operator) is it transparently subscribes to its parent (reduce() in the preceding example).
 * However, it cannot pass the same Subscriber instance it received. This is because subscribe() required Subscriber<String>,
 * whereas reduce() expects Subscriber<Integer>. After all, that is what map() is doing here: transforming Integer to String.
 * So instead, map() operator creates a new artificial Subscriber<Integer> and every time this special Subscriber receives
 * anything, it applies Integer::toHexString function and notifies the downstream Subscriber<String>
 */
public class LiftingExample {

    public static void main(String[] args) {
/*        Observable
                .range(1, 1000)
                .filter(x -> x % 3 == 0)
                .distinct()
                .reduce((a, x) -> a + x)
                .map(Integer::toHexString)
                .subscribe(System.out::println);*/

        //List example: scan().takeLast(1).single()
/*        Observable
                .range(1 , 1000)
                .lift(new OperatorFilter<>(x -> x % 3 == 0))
                .lift(    OperatorDistinct.instance())
                .lift(new OperatorScan<Integer, Integer>((total, value) -> total + value))
                .lift(    OperatorTakeLastOne.instance())
                .lift(    OperatorSingle.instance())
                .lift(new OperatorMap<>(Integer::toHexString))
                .subscribe(System.out::println);*/

        //Almost all operators, excluding those working with multiple streams at once (like flatMap())
        //are implemented by means of lift()

    }

}
