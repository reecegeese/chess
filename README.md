# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIGEAtIGckGVIEcCukB2BjSAERAEMBzAJ1IFsAoOgekdWFMuGgCVJyQlgkSnVjgQeYAFoAfOkoA3IQC5oAbQAKAeVQAVALrRGWJEOgBvAETGhuWpAtKL0CwBoLAB1IoA7gHtKACYOFq4WkDSkIODBAL50coqUMgASpLgBUJQqltaUtjT2js5unj7+QY6h4ZHRjnGp6ZkyCSCEKpS8-IKUABQdfAJCPNjIwACU8ULyrZAyRKRsAIL4Jigq5JDAAKomvbn5kBPzSyvISDIBAEYqAGIg6dA7Qsek0JcAntD7dpMKMwA8kkkL2WqyQKielBev0SgMkDQyymgi3AHVIAXeOlIAGs8ABRAAehHcoF8uBEYgkcISSIALAAGADM5gsBRQFEKTlCeMolH8Km+BWgpFRkHRnzYuNwwQscRahDhINOa2guCw4HAMJmcwWpFBZxU+DRgkhLx6uReR11+pQF2uyICAUeu2h8tm0iVYMNxsgiywwHgPVI-vglronrOdpUi0dyJD0IRmWpUxmKh4A26PCQ6uAMKEcMTSJyuwOwWKFmDAZ0vilsQp4lwwGTCiRACZ6fSWYLOeXK-Bq7W6kxGHiHumukJh6x2JwADK+Pjk0QNqSyKZIjTafSGXJdkt2MuhUpIPyBGVuPsDvB1mlJaSFrJ7mwHopHrwn8rnishq-SocP5oUzaaBwAXe4elAxdhhwAQJjdHUTi9aANm2XZzX3AorUQyNpCuW57idU1dTeT5uy1QgEL1ZVwWgI0xUEP0AyDeNdSwqiwSjB0nUY0NdXIyBFWtaiIRdPiH2baZgPHQZKCzHM80oAs0kRR9i2fApDwvH8a2vIdlypIFbxUdtOzUvIXy5LSqx0v9ZX4wTsJVNUNQUpTGiRAA5XxIUJYlSSXSlGwkukmRZNkkA5TSLB5PlH13AJfGQVVfE4SACS6GU4mYUcAnnRcpzYDg8t8f16wkQCW0fTddAMIxdifcyNNfEp31PCoQis-sbJvdc7wfbIrAwns3zKM9Kk6386wAtc-mAyCSuACCFwW6DRjgoD3QjFUUMhdD1MOcMhI43D7TuB4iLYEiviG-jKJtGiAkgKAGJDZiAzDLbzhOlQiCezYYB410Noc9iDWdZ4xOUpNDI2tNOhkuTwFzW83JUgbuzrfSgphyrjI7BrSyHbL0mK0rmGnDhoFnLoAHE7CQMrGwqxIVGq7dGHIOwWUvbqh1vFIoaLb9rMHOzprdFQxAEN6uqldbZs2o6wZQum2Rl382PuzizqdTm2SunmpXsoFPpEiG2DocScckyA4YzIZkHklGgX67ntNFuIsaba223xyxDd0uziadamBFV5ACpnOAfXDxnVyMtQtBqww9Zgf33cDuVeoF9zVOF2XM8twW7wl2ifXVmz5Ztu7hLL+jIHDiu5cOxyvrw6P6+QrmPmFDPyTdEH7rNqFIdziTU24eHM0dpHXJd4uBoD2zPcC73JATky3ZFwvg47hYG5+cnCs4AApXx7ljr3mY3JP2dTreC+XhSc7Rh-Jv-YvmYngArc-cCbvAVdtQeiViqOi+9G5L01tRTiZ97hdyFD3JextgSgJohdUgRcx7WwntJae2ZZ7O3hAvN+vM7Je2Co+Te6dt5PwHibNBKhnKaiIa7Ly4dfKQBJCAMkcdKEqAZMySw4VIrNWiryfkCCYAJSSrgFK0A0oZUqFlEcDw4G4FjkfKOogxTCCvjNFmicty1XwFAdgWCVJf2AqY3RPQgEURAa3Q0ZjejQOOu3HR7BoABF1JcLwkBhw5TgC4oAA))

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
