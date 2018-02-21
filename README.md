# ReactiveXHVACDebounceExample
A ReactiveX learning exercise using the Debounce operator.

I created this ReactiveX project to learn how ReactiveX can simplify parallel processes. 

Imagine an app that needs to send updated temperatures to a connected thermostat. We want to avoid spamming the thermostat with extra commands if the user overshoots their target. This can be accomplished with a practice know as debouncing (ignore erroneous input by ensuring a minimum amount of time between inputs before considering a new input valid). This has its origin in electrical circuits: https://en.wikipedia.org/wiki/Switch#Contact_bounce

The parallel processes here are:
1. Accepting and responding to user input
2. Keeping track of time between input to debounce the commands sent to the thermostat

ReactiveX observables make this easy with a built-in debounce operator.
