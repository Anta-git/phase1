# Kata 4: Check your understanding

Answer these in your own words **after** all K4 tests pass and you've run `K4Demo`.
Then ask Claude to check your answers. These are close to real interview questions.

1. In `K4Demo`, the 200-thread pool was about 20x faster than the 10-thread pool. Why didn't the
   virtual-thread version need a pool size at all?
2. `fetchAll` returns results in input order even though tasks finish in random order. What in your
   code guarantees that?
3. In Step C, what would happen to the running time if you called `future.get()` immediately after each
   `submit`? Why?
4. Why is it safe to call `resultNow()` where you called it, and what would happen if you called it
   inside the try block, right after submitting?
5. Suppose `fetchViewerCount` threw an exception for one channel. What happens with `get()`? With
   `resultNow()`? (Try it: make it throw for `"ch42"` and run the test.)
6. A teammate proposes switching a service that resizes images (pure CPU work) to virtual threads to
   make it faster. What do you tell them?
7. Your service calls a database that allows 20 connections. With virtual threads, 10,000 requests can
   now be in flight at once. What goes wrong, and what should limit concurrency instead of a thread pool?
8. In a Spring Boot 4 app, what's the one-line change to serve requests on virtual threads? (It's in
   `api/src/main/resources/application.properties`.)
9. Bonus (interview favourite): on Java 21, a virtual thread that blocks *inside a `synchronized`
   block* can't unmount from its carrier ("pinning"). Java 24 fixed this. Why does this matter for a
   team deciding between JDK 21 and JDK 25?
