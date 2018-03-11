# RandomCountGenerator


## randomInt():
Method that generates a random int between 1 and 5 with a distribution
probability as follow:
 - 1 - 50%
 - 2 - 25%
 - 3 - 15%
 - 4 - 5%
 - 5 - 5%


The method will save the last 100 numbers generated.
It will send each generated number to a queue shared with the Writer Thread.

## frequencyMap():
This function will return the frequency over the last 100 number generated
for each number.


# WriterThread:

Thread polling from the number generated queue and log them in a file.
Make sure that logs are saved chronologically.

# GeneratorThread

Thread used to run the RandomCountGenerator

