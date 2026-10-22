# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqBgAtAB8wjRQAFwwANoACgDyZAAqALowAPQ+BrAA3gBEfZRowAC2KEM1QzBDADRDuOoA7tAcM0OLQygTwEgIWwC+mFWUFWyc3LUww6NQ41Nb80srqutQm7M7ewdHs1OVy4F0qlCoojqUEy2TAlAAFNCsjlKBkAI6pHIASjO4NEFVk8iUKnUdXsKDAAFV+vCHk8UDjCYplGpVBVjHUAGJITgwamUJkwHSWGB0yZiKqiAA8pVKTOJrLq-KgTMwwJuMtKkpUdQyyLhUAyqh8CCw5ygmvVlDqCgQ0OAHEsbWAAGt0ABRAAeKmwBAKRWQ5k15rqgScwTuQym6mA5JeO3dUG8tzFUxgwDtKAdIvkbrQWyGp21KE18pZpJgaBNCFxIhUBO0CorIHtcOVTNp-SZjMb5bZ5Q5MAUHA4fK72lrEJUpd7JNUSvH8kn+PKZbndRbWbhCh8YHi8OAu-i3cwa9Z7J0NpHQ6PquLM6JfZtt4nVotsuLuphKMNahNZvBShLXYEFbnufp6XjJZDz3NoIDzE4AxKMBg0A25wicJxI1TaZZleIYYPiOCEMBMxOFMLxfACaB2HJGJOTgd1pDgBQYAAGQgLJCmKcwKhDBoWnaLpugMdR8jQbCIPFKDln0D4Ni2UjzUuECbjqcCxmkvCdneT5vkQt8+LxHUYAQTieXhMyuPRTEwBxYsG0fdcYHJKkaRwnsnPPAdLxgbleXbbQhRFHDl2nWUzwrKsEBrN9ULrFA6mimtzWA65rRgAA5CBlS9H0-W4wMMHi6pQycABGSNo1UWNcLmBMk2gOoHiMCA1ErCBmBQT1YQLItjJLCLZ0VMcBQnBzV2G5tWxQHc9wPF95E85k5wvK9Rzm49xoGh8VpGzbVTij8Bq-fVUT-U1JyA2U33UkYpOebToKPYj0ELJCg2O6gMowrCNMeLT6ue2D4Le0j0A4CjvD8fwvBQdAYjiRI4YRjj7F8LAeLKMFvtueppHdNj3Tad1hNE1RxLuQjXrQfrcZU9KwOp0HabVVTQU-UzOIxyzud3Gy1DssKUEcvaK1cg7tAWkG82Wpt+0HfyNsW4BgvTF6WeF3b5aS6s2cZkrIUrPXUpu9nbkpcYj2gJAAC8UA4PKUF9cSPuKr7SpgMNKuGarapkxNk2a-oYGyDrmEIm37f0977yGryK2hMAfEeYXRZ1owUG4bcj2lraltPKaFd86Qs4pQxJaXOO5SLqEKRTwojq1E6YCThurvfUpbtb+vU6xlCPZ+zC7lOCGoao-xoVHfxsB5F12NhGAAHFxQ0fujNxup6iX4nhPscUqY1vM6eqBnQPU5nj-10CN6nRLTNhSzYQF7E08mhP5xcilK+APOablvsa0-I8mVnuQUwp1Yy3QFreOYtP7JWvhqD2RsEGmy7ubJUVs9xRwdk7F2-p+4lQyt7KqrJ-ZPSGIHJqooQ5hzQJ1SB8QcExxPtKWBGc26pwmpFT+rkV7Rj-izABq0fJchAS5VeatL7QOrjwuo-CUCHXNobEyepYTnWNJdNB3dhj72jDMBoujxQAElpAzHKuEYIgQFjzFWPEJAbYHp1UWPMZIoAXTKkgj8GxQw9EoEyuKH4xxTiEMHuhYeRj9Fb0iSgUx5jLHWNsfYxxmlHrbB8W4kAHinEFh8X4gJUwgmj3Ip4aGAQOAAHY3BOBQFhfw7pghwEYgANngDNZeB83ZgFvhlJorROg9D8ZGfJgMfHSPzEpNCZ81LDPFAUuqYyj5g3eoZHGd8NwzXhJuOQKAX5C24bXL+YAf6CNloXD+QClY3jAUFCB4yYE1w-rrGKiDrrNwSs8lKaE0rnz5FgphUA7a4O9M7AqXSiG3BIb7MhcYKFUJTLQ1Q4dGHML6g8uRPdk5cIGunJ8MBtlwgUfCEZUxhHeUHHAdpQyIEkolDtdheKFFKINsg1R34DRGn-B3H5MzhgKLiTUCxVj3qhPeZ7X6kZ+VmMFQk96Y9SkT0sFnMyqxEZIASGAJVMUICqoAFIQB5B0tM-hMkui6TACo5A0Jb0aJSAZ681kZW6EM4Y2AEDACVVAOAEAzJQC2AAdRYMYkm3QABCbEFBwAANIuN8SY6VMAhWJIIksiZscpnlB0csd1nrvW+oDUGkN4bI0xryfGgxSbFlQLTUCc2t8jYACsDVoHhE2nkez7I4vfnAsk38VanPQGS9QlzxE-ykam9FhyEFN05qg75ZtGaYMjoC6OeCwWiv4lCqMMK6r4XhcHSgockX0IjtbFdDs0WyMOZwwoBynkwDbWgBQqg83QHhG6j1lBX1QCHcXOocB4goCyUe-FProAgeAFQf4uhuCTvvdO5RrL75ztxjyjKCjnR5jXa7Dd1qvbhlITGWFQNKGNVuBme0joYC5mWemhK2s8U3rfhix9RK-GmN-UA-VhrqUinY9IODPbMXtxnS3Jj2iMHCb7kVAeYqh5-WKZDBVMMvAerVRq1TIpEBZlgMAbAbrCB5AKDALpPS8YEyJiTYSxgO7TIygMWtjN60mRANwPA8JO0JVxc5VzOmmR6AMB585cCgHabwEYbQAW6X0YZc5JjomPlSdvfO9Bi6kvgrCXUCVDmyKQyAA)

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
