# Kata 4: Check your understanding

Answer these in your own words **after** all K4 tests pass and you've run `K4Demo`.
Then ask Claude to check your answers. These are close to real interview questions.

1. In `K4Demo`, the 200-thread pool was about 20x faster than the 10-thread pool. Why didn't the
   virtual-thread version need a pool size at all?
    - This is due to the fact that virtual threads unmount while waiting on I/O, removing the need to specify a pool
      size.
2. `fetchAll` returns results in input order even though tasks finish in random order. What in your
   code guarantees that?
    - This is because order is preserved in the List object.
3. In Step C, what would happen to the running time if you called `future.get()` immediately after each
   `submit`? Why?
    - You'd be waiting on each task to finish before submitting the next one, skyrocketing the execution time.
4. Why is it safe to call `resultNow()` where you called it, and what would happen if you called it
   inside the try block, right after submitting?
    - Same as the previous question - you'd be calling the results immediately after submitting, forcing you to wait
      between each task and skyrocketing execution time.
5. Suppose `fetchViewerCount` threw an exception for one channel. What happens with `get()`? With
   `resultNow()`? (Try it: make it throw for `"ch42"` and run the test.)
    - I'm unsure of the exact behavior, but it appears to continue running for the rest of the tasks.
6. A teammate proposes switching a service that resizes images (pure CPU work) to virtual threads to
   make it faster. What do you tell them?
    - It's extremely unlikely that swapping to virtual threads would improve performance in that situation, as it's
      almost certainly entirely CPU bound.
7. Your service calls a database that allows 20 connections. With virtual threads, 10,000 requests can
   now be in flight at once. What goes wrong, and what should limit concurrency instead of a thread pool?
    - I'm unsure of exactly what would go wrong in this situation. My assumption is that we would hog all available
      database connections, denying anything else from connecting while we are using it.
8. In a Spring Boot 4 app, what's the one-line change to serve requests on virtual threads? (It's in
   `api/src/main/resources/application.properties`.)
    - A line needs to be added to enable virtual threads.
9. Bonus (interview favourite): on Java 21, a virtual thread that blocks *inside a `synchronized`
   block* can't unmount from its carrier ("pinning"). Java 24 fixed this. Why does this matter for a
   team deciding between JDK 21 and JDK 25?
    - It's behavior that could lead to errors if anyone on the team is unaware of the behavior. We should use JDK 25 to
      completely sidestep this issue before it even happens.
