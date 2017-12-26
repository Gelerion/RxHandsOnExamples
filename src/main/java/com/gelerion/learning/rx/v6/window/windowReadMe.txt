When working with buffer() we build List instances over and over. Why do we build these intermediate
Lists rather then somehow consume events on the fly? This is where the window() operator becomes useful.
You should prefer window() over buffer() if possible because the latter is less predictable in terms of memory usage.