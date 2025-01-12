# modelserver [![Build Status](https://travis-ci.org/eclipsesource/modelserver.svg?branch=master)](https://travis-ci.org/eclipsesource/modelserver)
## Build the modelserver
To build and test the components as well as building the modelserver as standalone JAR execute the following maven goal in the root directory:
```bash
mvn clean install
```


## Run the modelserver
### Run the modelserver in an IDE
To run the example modelserver within an IDE, run the main method of [ExampleServerLauncher.java](https://github.com/eclipsesource/modelserver/blob/master/examples/com.eclipsesource.modelserver.example/src/main/java/com/eclipsesource/modelserver/example/ExampleServerLauncher.java) as a Java Application, located in the module `com.eclipsesource.modelserver.example`.


### Run the modelserver standalone JAR
To run the model server standalone JAR, run this command in your terminal:
```bash
cd  examples/com.eclipsesource.modelserver.example/target/
java -jar com.eclipsesource.modelserver.example-X.X.X-SNAPSHOT-standalone.jar
```

### Usage
```
usage: java -jar com.eclipsesource.modelserver.example-X.X.X-SNAPSHOT-standalone.jar
       [-e] [-h] [-p <arg>] [-r <arg>]

options:
 -e,--errorsOnly   Only log errors
 -h,--help         Display usage information about ModelServer
 -p,--port <arg>   Set server port, otherwise default port 8081 is used
 -r,--root <arg>   Set workspace root
```

## Use the modelserver API
If the modelserver is up and running, you can access the modelserver API via `http://localhost:8081/api/v1/*`.

The following table shows the current HTTP endpoints: 

|Category|Description|HTTP method|Path|Input
|-|-|:-:|-|-
|__Models__|Get all available models in the workspace|__GET__|`/models`| -
| |Get model|__GET__|`/models`|query parameter: `[?modeluri=...[&format=...]]`
| |Create new model|__POST__|`/models`|query parameter: `?modeluri=...[&format=...]` <br> application/json
| |Update model|__PATCH__|`/models`|query parameter: `?modeluri=...[&format=...]` <br> application/json
| |Delete model|__DELETE__|`/models`|query parameter: `?modeluri=...`
| |Save|__GET__|`/save`|query parameter: `?modeluri=...`
| |Execute commands|__GET__|`/edit`|query parameter: `?modeluri=...`
| |Get all available model URIs in the workspace|__GET__|`/modeluris`| -
|__JSON schema__ |Get JSON schema of a model|__GET__|`/schema`|query parameter: `?modeluri=...`
|__Server actions__|Ping server|__GET__|`/api/v1/server/ping`| -
| |Update server configuration|__PUT__|`/api/v1/server/configure`|application/json

<br>

The query parameter `?modeluri=` accepts files in the loaded workspace as well as absolute file paths.

<br>

Subscriptions are implemented via websockets `ws://localhost:8081/api/v1/*`.

The following table shows the current WS endpoints: 

|Description|Path|Input|Returns
|-|-|-|-
|Subscribe to model changes|`/subscribe`|query parameter: `?modeluri=...[&format=...]`|`sessionId`

## Java client API

The modelserver project features a Java-based client API that eases integration with the model server.
The interface declaration looks as follows

```Java
public interface ModelServerClientApiV1 {

    CompletableFuture<Response<String>> get(String modelUri);

    CompletableFuture<Response<A>> get(String modelUri, String format);

    CompletableFuture<Response<List<String>>> getAll();

    CompletableFuture<Response<Boolean>> delete(String modelUri);

    CompletableFuture<Response<String>> update(String modelUri, String updatedModel);

    CompletableFuture<Response<A>> update(String modelUri, A updatedModel, String format);

    CompletableFuture<Response<Boolean>> save(String modelUri);

    CompletableFuture<Response<String>> getSchema(String modelUri);

    CompletableFuture<Response<Boolean>> configure(ServerConfiguration configuration);

    CompletableFuture<Response<Boolean>> ping();
    
    CompletableFuture<Response<Boolean>> edit(String modelUri, CCommand command, String format);

    void subscribe(String modelUri, SubscriptionListener subscriptionListener, String format);

    boolean unsubscribe(String modelUri);
    
    EditingContext edit();
    
    boolean close(EditingContext editingContext);
}

```


### REST API Example

```Java
// You can customize the underlying okhttp instance by passing it in as a 1st parameter 
ModelServerClient client = new ModelServerClient("http://localhost:8081/api/v1/");

// perform simple GET
client.get("SuperBrewer3000.json")
      .thenAccept(response -> System.out.println(response.body()));

// perform same GET, but expect XMI format      
client.get("SuperBrewer3000.json&format=xmi")
      .thenAccept(response -> System.out.println(response.body()));
      
// perform POST
client.update("SuperBrewer3000.json", "{ \"data\": <payload> }")
      .thenAccept(response -> System.out.println(response.body()));

// perform POST with XMI format
client.update("SuperBrewer3000.json&format=xmi", client.encode(brewingUnit, "xmi"))
  .thenAccept(response -> {
    client.get("SuperBrewer3000.json&format=xmi").thenAccept(resp -> {
      System.out.println(client.decode(resp.body(), "xmi"));
    });
  });
}
```

### Subscriptions Example

If you want to be notified about any changes happening on a certain model, 
you can subscribe with a `SubscriptionListener`.

```Java
ModelServerClient client = new ModelServerClient("http://localhost:8081/api/v1/");
String subscriptionId = "SuperBrewer3000.json&format=xmi";
client.subscribe(subscriptionId, new SubscriptionListener() {
  @Override
  public void onOpen(Response<String> response) {
    System.out.println("connected: " + response.getMessage());
  }

  @Override
  public void onMessage(String response) {
    System.out.println("message received: " + response);
  }

  @Override
  public void onClosing(int code, @NotNull String reason) {
    System.out.println("Closing");
  }

  @Override
  public void onFailure(Throwable t) {
    System.out.println("Failed: ");
    t.printStackTrace();
  }

  @Override
  public void onClosed(int code, @NotNull String reason) {
    System.out.println("Connected closed");
  }

  @Override
  public void onFailure(Throwable t, Response<String> response) {
    System.out.println("Failed: " + response);
  }
});
client.unsubscribe(subscriptionId);
```


## Code Coverage

Latest Code Coverage report can be found here: `com.eclipsesource.modelserver.codecoverage/jacoco/index.html`

The code coverage report is generated with [JaCoCo](https://www.eclemma.org/jacoco/) and is integrated in the Maven build. In the package `com.eclispesource.modelserver.codecoverage` all code coverages are aggregated into one report.

For now, only the main overall result per module is uploaded to the repository here. When executing the Maven build executed locally, the detailled results are computed and can be investigated in more detail.

