# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqBgAtAB8wjRQAFwwANoACgDyZAAqALowAPQ+BrAA3gBEfZRowAC2KEM1QzBDADRDuOoA7tAcM0OLQygTwEgIWwC+mFWUFWyc3LUww6NQ41Nb80srqutQm7M7ewdHs1OVy4F0qlCoojqUEy2TAlAAFNCsjlKBkAI6pHIASjO4NEFVk8iUKnUdXsKDAAFV+vCHk8UDjCYplGpVBVjHUAGJITgwamUJkwHSWGB0yZiKqiAA8pVKTOJrLq-KgTMwwJuMtKkpUSv6qvOUE16sodQAQsAOOjMWAAKIADxU2AIBSKyHMmoNdUCTicdyGU3UwHJLx2Nqg3lueg4MGhGLUYC2Q1OBqN7BBtwUCGhFssbWAAGt0PbHc7CsV3bLPTBvcE-QHVEHprNXkMwxG6mKpjBgFmUDmYPJC2hE8m8SpNfKWaSYGgfAgELiRCoCdoFdOQNm4cqmbS9dpGaup2zyhyYAoONHt9pFxDl+VJyTVHUN324QofGB4vDgB-4kyD0SR7sjodTntG76fqqxqGpWY4oHUGTInCUAZKoc5YCmsrQXU9z9PSIZLD+n5tBAQ4nK6JRgB64ImjA4Q+n6nZNnMOxEfEJFkYCZicKYXi+AE0DsOSMScnANrSHACgwAAMhAWRlm6ZRgtQtFNK0nQ9AY6j5GgjF4eKBHLPoHwbFsXEGpcaY3DhIz6c8zY7O8nzfOR0EVNq8EwAgck8vC3nyVa8Y4h5K6AY+ZIUsqu5jOKAHMo+wFcjyl57vIQoikxN7SrKD6KnyqXAGqVmUNRS6eYhsKomo6E3iVWHFbcuExfZLFvMZzlJhRFZajRtz0b6TWPAZDltWspnmXBE6HuFM5zgu0GlbenmzvOtUwaU2EwAAchAyrFigTo6V1GCLbR3oAIx1qyjaGW20Adv0RgQGoM4QMwKB2rCI7cRwvHeH4-heCg6AxHEiRAyDsn2L4WDlkpVb1NINrSTabQ2l03RaaoOl+mxHHoORFnlJtwx46RBOuQ17lwXU-kw35ckw4F2JZXeuXTuSYAQX+2jfr++NoHFa7Hqe3K8tzgrCt2-Pk4UHlTWFeUS9eC2wWVoG-vqvWptctGUuMv7QEgABeKAcPth0unDVFq9UXpOJdwz1jdI2tuG92io92SvcwbFG6bLmdSF97TXlHAoNwb6-nzkH7pg7MiyBMgRxShjK-IrMoAr8V5VeGeqz16swBVKIodVCAYdr9W67cJfIahNXW6dfUMQMpzoL9nj-QE0LRv42A8vmMmwjAADi4oaNb1Mqbc9Sj6jGP2OKuMy5xQe9ZZNc2WTa9AlTylLbTsIx+xsvBXBoU5xzFLjwGJ8C0LQEnknYvRkvAbpdLxGy5n2fCxrsd84NUWpCM8mtryYQ2g1JUBtPz+zNhbUsx0baFzttWB2V1AzBldndW4DwYDezQG9L+8R4GB1HGVP+R46i3xQFBYBttQF1yqmhCua0dbpm3qvAmXEm621ov1P0781AzAaMMYRABJaQMxzrhGCIEBY8xVjxCQFuOyzFFjzGSKAfMyp8I-EUUMYRW1xQ-GOO3HiXd+L+A4AAdjcE4FAvp-A2mCHAMSAA2eAm5DC0JgMg6eaC1LtEXsvYYtCTEtUMTvHh68Z6b04X6CJw1NFDBicOLibkD6gJfHIFA8JklTHPmVS+-8YC5LhLQgp4pIkMnjqHdQiVvGvkMMIz+hSJSTRyg0p8YDAGFQLh5ABPMM6QM2vrP2UATYIIdAdJBfDUFnQwU7a62DWpu3bJ7SgBDVA+xIWQ768tumK2nLQ+hNcQE6mLjCUuDc2FjOgSvb+u9kHNzqII8J4opEyLkYETqHc-rWMsBHbyqxQZIASGAYF84IBgoAFIQB5GPZe-htEgHzMgmAFRyC9TqE0SkGkp4H1ot0NppNuEZLidUBJ1knmnxeVkoZMAABWiK0D3zPpnUp1CWVsqqekx+CVn51ARUitpUt0m-2OVfXp6cBkMNQaAuV7Dq6JImYbKZAdEFHQWVWC6mCGxrJbLgh62zCHEMmdM8hUq5Q9JoeKc56ZLnlRufXculcZ4cNpcMbACBgDAqgHACA3koBbAAOosAkWjbopppIKDgAAaVSZI6RdRZHyNOLq3FdFW4UKWlQmaK15oKqZUWlVUCt7IqmHmIc2qraKRQXqpwtYVlYOYsa92twezZg4CKQcsT-mWL4gDLw-rwWQtHSKRAfZYDAGwL6wgeQCj+KJQjJGKM0YY2MGtGltE25FRroEnJ3A8DwmKUtblM0QAnqgGe+pJzE51GnXgIw2g9AGBtQnBCrqWE1UGTTa5SFf33KrhWxJz6oC4LrQpSibyc0DQsb9IAA)

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
