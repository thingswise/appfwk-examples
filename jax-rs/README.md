# Prerequisites

Install and configure Java and Apache Maven.

# Building

Go to `jax-rs` and run `mvn install`.

# Running in a local development environment

* Download and unpack Apache Karaf. Let's say its directory is `$KARAF`
* Write these lines into `$KARAF/deploy/default-features.xml`:

  ```xml
  <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
  <features xmlns="http://karaf.apache.org/xmlns/features/v1.3.0" name="default-features">
    <feature name="jax-rs" version="2.0" install="auto">
      <bundle>mvn:javax.ws.rs/javax.ws.rs-api/2.0</bundle>
    </feature>
    <feature name="jax-rs-connector" version="5.3.1" install="auto">
      <bundle dependency="true">mvn:com.eclipsesource.jaxrs/jersey-min/2.22.2</bundle>
      <bundle dependency="true">mvn:com.eclipsesource.jaxrs/consumer/5.3.1</bundle>
      <bundle>mvn:com.eclipsesource.jaxrs/publisher/5.3.1</bundle>
    </feature>
    <feature name="tw-default-features" install="auto">
      <feature>war</feature>
      <feature>wrap</feature>
      <feature>scr</feature>
    </feature>
  </features>
  ```

* Copy the built KAR archive from `jax-rs/features/jaxrs-test/target/jaxrs-test-0.1.0-SNAPSHOT.kar` to
  `$KARAF/deploy`

At this point the REST service should have been automatically picked up and available
at `localhost:8181/services/test`:

```
curl localhost:8181/services/test
Hello, world!
```
