# Share and call the payment contract

The root `buf.yaml` uses the existing schemas in
`share-common/common-grpc/src/main/proto`. Python code is not required.

## Publish schemas

The module is configured to publish to `buf.build/seyhacg/seyhagrpc`.
Your account must have write access to that repository.
From the project root:

```sh
buf registry login buf.build
buf lint
buf build
buf push --label v1.0.0
```

Other developers can download the published schemas:

```sh
buf export buf.build/seyhacg/seyhagrpc:v1.0.0 --output ./vendor/payment
```

Private repository consumers must log in with an account that has access.
Use the commit identifier returned by `buf push` instead of a label when an
immutable schema version is needed; labels can move.

## Send a gRPC request

Buf hosts schemas. Your running Payment Service handles requests.
Start the Java server in one terminal:

```sh
./gradlew :payment-service:bootRun
```

In another terminal, send the sample request using the commented script:

```sh
bash scripts/request-payment.sh
```

This calls `payment.PaymentService/ProcessPayment` on `localhost:9090` with
order `demo-order-1` and 1500 cents. The response contains a simulated payment ID,
success flag, and message. No real money is transferred.

To customize the request, save this as `request.json`:

```json
{"orderId": "my-order-2", "amountCents": "2500"}
```

```sh
bash scripts/request-payment.sh ./request.json
```

Protobuf JSON represents int64 values such as amountCents as strings.
After publishing, use the registry schema with the same server:

```sh
PAYMENT_PROTO_SCHEMA=buf.build/seyhacg/seyhagrpc:v1.0.0 \
  bash scripts/request-payment.sh
```

Set PAYMENT_GRPC_URL to a different HTTP or HTTPS base URL for a remote server.
The existing Order Service also sends a gRPC request through PaymentClient when
you call `POST /api/orders`, as described in the root README.

## Check future schema changes

```sh
buf breaking --against buf.build/seyhacg/seyhagrpc:v1.0.0
```

Keep existing field numbers stable and reserve removed names and numbers.
The lint exceptions in buf.yaml preserve the existing package and message names.

Reference: https://buf.build/docs/bsr/module/publish/
