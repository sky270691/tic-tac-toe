# How To Run

- run `./mvnw clean install -pl app spring-boot:run -DskipTests` on linux or `mvnw.cmd clean install -pl app spring-boot:run -DskipTests` on windows
- open `http://localhost:8080`
- generate your game and choose from 2 modes online mode or Single Device Mode and determine the board size between 3 - 12
- in single device mode you play only in one window / device
- in Online Mode, share the invitation code you get to your friend
- Your friend will be able to access the game by input the invitation code in root page (`http://localhost:8080`)

### Tech stack
- Java Springboot framework
- JPA
- H2 database
- Vaadin

### Notes
For the development process, I try to implement hexagonal architecture by utilizing maven multi project build, although it's not necessary to do that, but I try to increase the complexity