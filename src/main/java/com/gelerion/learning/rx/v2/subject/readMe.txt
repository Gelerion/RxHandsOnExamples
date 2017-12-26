Subjects:
 * AsyncSubject
    Remembers last emitted value and pushes it to subscribers when onComplete() is called. As long as AsyncSubject
    has not completed, events except the last one are discarded.

 * BehaviorSubject
   Pushes all events emitted after subscription happened, just like PublishSubject. However, first it emits the most
   recent event that occurred just before subscription. This allows Subscriber to be immediately notified about the
   state of the stream. For example, Subject may represent the current temperature broadcasted every minute. When a
   client subscribes, he will receive the last seen temperature immediately rather than waiting several seconds for
   the next event. But the same Subscriber is not interested in historical temperatures, only the last one. If no
   events have yet been emitted, a special default event is pushed first (if provided).

 * ReplaySubject
   The most interesting type of Subject that caches events pushed through the entire history. If someone subscribes,
   first he receives a batch of missed (cached) events and only later events in real-time. By default, all events
   since the creation of this Subject are cached. This can be become dangerous if the stream is infinite or very
   long (see “Memory Consumption and Leaks”). In that case, there are overloaded versions of ReplaySubject that keep
   only the following:
       Configurable number of events in memory (createWithSize())
       Configurable time window of most recent events (createWithTime())
       Or even constraint both size and time (whichever limit is reached first) with createWithTimeAndSize()