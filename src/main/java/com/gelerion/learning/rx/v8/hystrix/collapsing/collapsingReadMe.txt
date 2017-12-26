One of the most advanced features of Hystrix is the batching requests. Imagine that you are making several small
downstream requests throughout the course of handling one single upstream request. For example, suppose that you are
displaying a list of books, and for each book you must ask an external system for its rating:

    Observable<Book> allBooks() { /* ... */ }
    Observable<Rating> fetchRating(Book book) { /* ... */ }

The allBooks() method returns a stream of Books that we want to process, whereas fetchRating() retrieves a Rating for
each and every Book. Naive implementation would simply iterate over all books and retrieve Rating one after another.
Fortunately, running subtasks asynchronously in RxJava is very simple:

    Observable<Rating> ratings = allBooks()
            .flatMap(this::fetchRating);

The diagrams that follow compare calling fetchRatings() sequentially versus using flatMap(). The phases are send for
transferring request, proc for server-side processing, and recv for transferring the response.
This works great and generally we see a satisfying performance. All fetchRating() invocations are executed concurrently
and greatly improve latency. However if you consider that each invocation of fetchRating() implies a fixed amount of
network latency, calling it for dozens of books seems wasteful. Making one batch request for all books and receiving
one response with all ratings sounds much more productive

Notice that all phases: sending, processing, and receiving, are slightly slower. All of them either transfer or process
more data, so this is understandable. Therefore, the total latency is actually higher compared to multiple small requests.
The improvement is questionable. But you must look at a bigger picture.

Although the latency of an individual request increased, system throughput is probably greatly improved. The number of
concurrent connections we can perform, network throughput and JVM threads are limited and scarce resources.
If the dependency you request has limited throughput, it is easy to saturate it with relatively few transactions
that take advantage of concurrency. A selfishly utilized flatMap() improves latency of single request but can degrade
performance of all other requests by saturating resources. Therefore, we might want to sacrifice a little bit of latency
in order to achieve much better overall throughput without generating too much load on downstream dependencies.
In the end, the latency is actually improved, as well: requests are more fair in sharing resources, so the latency
is more predictable.