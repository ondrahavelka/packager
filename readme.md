# BSC Sample Application

Application is written by design provided in exam interview.

Maybe you will ask - why Spring Boot? Two reasons: 
1) Scheduler is simple to write in SB app.
2) I misread "Data should be kept in memory (please don’t introduce any database engines). " where I undestood to not implement
database "server" engine together with app and just use in memory DB (hsqsl or h2) which I promptly did before rereading 
assignment, hence SB is cool choice for that.

Note: If I can have a request - please provide feedback on code even, well, especially if you are not satisfied with whatever is 
written here, it will be welcomed and most appreciated. 
# Understanding the Spring BSC packager application 


## Running packager locally
BSC postal is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). 
Add at least one parametr with path to file with packages, second parameter with fees is optional.
If no parameter is presented, exception is thrown and program is terminated. 
<b>It is by default expected that first parameter is package initial load and second parameter is fees!</b>
You can build a jar file and run it from the command line:


```
cd bsc/postal
./mvn package
java -jar target/packager-0.0.1-SNAPSHOT.jar C:/packages.txt C:/fees.txt 
```


Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```
##Control 
After start you can control this application with several command line commands:
sum - print sum of weight (and price of packages) of all postal codes
packages - print all packages with postal code 
fees - print all fees provided in second option or Dobby error
quit - will end the application run

"A B" - user enters line consisting of weight (A) of package and destination postal code (B) with specified limitation:
<weight: positive number, >0, maximal 3 decimal places, . (dot) as decimal separator><space><postal code: fixed 5 digits> 

Once per minute - write output to console, each line consists of postal code and total weight of all packages for that postal code

## Invalid states
I did not take this task really seriously since it is just interview check for coding - so for invalid inputs there are exceptions:

Codes are inherited from https://discworld.fandom.com/wiki/Hex

InitialLoadException - <b>"Error At Address: 14, Treacle Mine Road, Ankh-Morpork" </b>
Provided arguments are wrong: 
1) Given line is null (either from file, or from console too)
2) After parsing line there are not two arguments in line "12/15431" is invalid, f.e. (either from file, or from console too)

InvalidUserInputException - <b>"+++Whoops! Here comes the cheese! +++"</b>
Provided data are wrong: 
1) First parameter after split is not a valid number (different than numeric character, etc.)(either from file, or from console too)
2) Second parameter after split is not a valid number (different than numeric character, etc.)(either from file, or from console too)
3) Postal code in line is not of size 5 or is not numeric (different than numeric character, etc.)(either from file, or from console too)

InvalidUserInputException - <b>"+++Wahhhhhhh! Mine!+++"</b>
Provided data is negative (we count only with positive weight, positive price)

UnknownFileTypeException - <b>"I am sorry. It is hard to convey five-dimensional ideas in a language evolved to scream defiance at the monkeys in the next tree"</b>
Packager counts with two different types of arguments - packages file with initial load and fees file with initial prices. 
If there is different parameter (third one), this exception is thrown

## Note to unit testing
I have just wrote several unit test for model creation (esceptional states and valid states) and basic app run.
I did not want to waste time on unit test to check output, since a lot of app logic just prints output.
Since app is not complex, most of functiona logic was tested by running the app during the development.
## Working with BSC postal in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 11 or newer.
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)

### Steps:

1) On the command line
```
git clone https://github.com/ondrahavelka/packager.git
```
2) Inside Eclipse or STS
```
File -> Import -> Maven -> Existing Maven project
```

3) Inside IntelliJ IDEA

In the main menu, choose `File -> Open` and select the packager [pom.xml](pom.xml). Click on the `Open` button.

A run configuration named `PackagerApplication` should have been created for you if you're using a recent Ultimate
version. Otherwise, run the application by right clicking on the `PackagerApplication` main class and choosing
`Run 'PackagerApplication'`. But don't forget to add parameters.

4) Enjoy application within your favorite command line