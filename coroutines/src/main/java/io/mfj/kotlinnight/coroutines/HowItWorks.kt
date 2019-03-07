package io.mfj.kotlinnight.coroutines

// How do we cooperate so it can switch processes?

// The compiler turns every block between suspension points into a Continuation.
// It keeps track of where in a suspension function progress is,
// so it can suspend and switch to another coroutine and then come back
// and find the continuation and restart.
// Instead of a thread context switch, it is running a switch statement.

// See:
// Deep Dive into Coroutines on JVM by RomanElizarov
// https://www.youtube.com/watch?v=YrrUCSi72E8