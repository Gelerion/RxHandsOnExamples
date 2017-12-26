subscribeOn() allows choosing which Scheduler will be used to invoke OnSubscribe (lambda expression inside create()).
Therefore, any code inside create() is pushed to a different thread—for example, to avoid blocking the main thread.
Conversely, observeOn() controls which Scheduler is used to invoke downstream Subscribers occurring after observeOn().
For example, calling create() happens in the io() Scheduler (via subscribeOn(io())) to avoid blocking the user interface.
However, updating the user interface widgets must happen in the UI thread (both Swing and Android have this constraint),
so we use observeOn() for example with AndroidSchedulers.mainThread() before operators or subscribers changing UI.
This way we can use one Scheduler to handle create() and all operators up to the first observeOn(), but other(s)
to apply transformations.