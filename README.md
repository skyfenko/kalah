# What was done?
# Architecture
It was chosen to use the most simple architectural type - Multi-Layered Architecture without persistence layer since in this case it is not needed since we don't introduce data storage.
From user's standpoint, UI is communicating with back-end over REST and form-submission (in case of login flow (for simplicity))

# Structure
The project is wrapped up with Maven 3. It consists of 3 modules (domain (holds domain objects and exceptions), service (business logic), web (web API and representation)).
The main game flow is implemented through Spring-like Chain of Responsibility pattern using Ordered interface. Every turn goes through pipeline of RulesApplier classes. 
Each of them is applying the rule, which is responsible for.
All these implementations and calling them are controlled by RulesOrchestrator, which in turn is used by web controller to populate received data to UI.
All the validation, rules applying are made by this chain. 

# Security
Server supports SSL/TLS with 'want' client authentication on 8443 port. Each user is limited to have only 1 session.
Keystore is located at web/resources and used by Tomcat to establish connection between server and client.
Clients use JSESSIONID over secure cookie with CSRF token in header (against XSRF attacks) to keep them alive.

# Stack
* Java 8
* Spring Boot/MVC/Security
* Lombok
* Commons Lang/Collections
* JUnit + Spring Boot Test
* Pitest
* Thymeleaf
* Tomcat 8

# Tests 
### 100% line coverage and 97% mutation coverage
Only service layer was covered by JUnit tests using Spring Boot Test library, since web layer is a kind of dispatcher to service layer business logic. 
Line coverage and mutation coverage were verified by PiTest. 
To check PiTest reports - you need to run at root project folder 

mvn org.pitest:pitest-maven:mutationCoverage 

After running all the tests and performing all the mutations, you need to open /service/target/pit-reports/datetimestamp/index.html

# JavaDocs
Code is documented with in-depth details. You can pass through the classes to follow the flow along with reading comments

# Users
There are 2 users, who are the players:
* south/password1
* north/password2

Take a look at SpringSecurityConfig class to find out how they are defined

# How to build?
mvn clean install

# How to run the application?
Application has embedded Tomcat, so the only one thing you need is to run

java -jar web/target/web-0.0.1-SNAPSHOT.jar  

# Game
The game is started by south player by default. 
To start game:
1. Open https://localhost:8443
2. Log in with south player using one browser
3. Log in with north player using another browser
4. Press "Start Game" button and make the first move by south player
5. After south player makes a move, check "Who takes turn?" label to find out who should take a turn next
6. If north player should make a move, switch to another browser (where north is logged in), refresh page and make a move.
7. If south player should be the next, switch to first browser and make a move.
8. Play until the application notifies you that the game is over.
9. Enjoy!

![Login Form](https://image.ibb.co/e4BUC5/Screen_Shot_2017_07_28_at_12_43_48.png)
![Kalah game before game is started](https://image.ibb.co/jjWws5/Screen_Shot_2017_07_28_at_12_43_59.png)
![Kalah game](https://image.ibb.co/n8eKek/Screen_Shot_2017_07_28_at_12_36_43.png)

# Rules
1. At the beginning of the game, six stones are placed in each house.
2. Each player controls the six houses and their stones on the player's side of the board. The player's score is the number of stones in the store to their right.
3. Players take turns sowing their stones. On a turn, the player removes all stones from one of the houses under their control. 
Moving counter-clockwise, the player drops one stone in each house in turn, including the player's own store but not their opponent's.
4. If the last sown stone lands in an empty house owned by the player, and the opposite house contains stones, both the last stone and the opposite stones are captured and placed into the player's store.
5. If the last sown stone lands in the player's store, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.
6. When one player no longer has any stones in any of their houses, the game ends. The other player moves all remaining stones to their store, and the player with the most stones in their store wins.

# Further improvements
Since time is limited, not all of what I wanted I did manage to include into implementation
Below is the list of further improvements.

## Security
* mutual client-server authentication over SSL/TLS
* JWT token support to make sure consistency
* HPKP (HTTP Public Key Pinning) to make sure the client is not impersonated by attacker
* forgot password? / remember-me functionality

## Representation
* introduce Thymeleaf layout feature to the application to set up strong mark-up structure (similar to Apache Tiles page structure)
* compress JS/CSS files to improve data exchange overall network 

## Persistence
* introduce data storage to keep a history of games and be able to generate statistics, keep JWT tokens, etc
* extend user entities and keep them in DB for further usage

## Game flow
* implement custom Spring bean scope for Game to be able to support several 1-to-1 games at the same time

## Misc
* implement exception dictionary with mapping exception-to-message-shown-to-user
* introduce resource management system to support i18n, push locales files to UI to show appropriately 
* implement tests for web layer. It was skipped since web layer is a kind of dispatcher to service layer.