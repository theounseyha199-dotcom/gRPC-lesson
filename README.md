# Service build conventions

For publishing the shared `.proto` contract to Buf so other developers can use
it, see [Buf sharing instructions](docs/buf-sharing.md).

Use JDK 25 and the checked-in Gradle wrapper from the repository root:

```sh
./gradlew clean build
```

This compiles the convention plugins, generates the protobuf/gRPC classes,
builds all modules, runs the service context tests, and packages both Boot jars.
The payment context test disables the network server so it does not reserve a port.

## How build-logic is shared

`settings.gradle` registers `build-logic` as an included plugin build.
`build-logic` uses `groovy-gradle-plugin` to expose its scripts as plugin IDs:

| Plugin | Purpose | Consumers |
| --- | --- | --- |
| `java-common-conventions` | Java 25, Maven Central, group/version, JUnit | Root, shared modules, Spring conventions |
| `spring-common-conventions` | Java conventions, Spring Boot 4.1.1 BOM, annotation processors, validation, Spring tests | common-grpc, Boot convention |
| `spring-boot-common-conventions` | Spring conventions and executable Boot packaging | order-service, payment-service |

External Gradle plugin dependencies are declared in `build-logic/build.gradle`.
The Spring BOM version is in `spring-common-conventions.gradle`; keep it aligned
with the Boot Gradle plugin. The gRPC and protobuf compiler versions come from
that BOM. Shared libraries do not apply the executable Boot convention.

To add another service, include it in root `settings.gradle` and apply:

```groovy
plugins {
    id 'spring-boot-common-conventions'
}
```

Add its application entry point and service-specific dependencies in that module.

## Current service skeletons

`common-grpc/src/main/proto/payment.proto` is the shared contract. Both services
depend on `:share-common:common-grpc`; generated sources stay under `build/`.
Payment exposes `ProcessPayment` over gRPC. Order Service calls it when you send
`POST /api/orders`. Valid requests return a simulated successful payment with a
`demo-` payment ID and `PAID` status; no money is transferred or external API called.
Invalid input returns HTTP 400, and gRPC transport failures return HTTP 502.

Start each service in a separate terminal:

```sh
./gradlew :payment-service:bootRun
./gradlew :order-service:bootRun
```

Payment uses gRPC port 9090. Order connects to `localhost:9090` by default;
set `PAYMENT_GRPC_ADDRESS` to override it.

```sh
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"orderId":"demo-order-1","amountCents":1500}'
```
