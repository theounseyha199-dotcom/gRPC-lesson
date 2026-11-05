#!/usr/bin/env bash
# Call the running Java Payment Service over gRPC with the shared schema.
set -euo pipefail

# Find the project root regardless of the terminal's current directory.
project_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
# Override this URL to call another server; use https for a TLS endpoint.
payment_url="${PAYMENT_GRPC_URL:-http://localhost:9090}"
# After publishing, this can be buf.build/YOUR_OWNER/payment:v1.0.0.
schema="${PAYMENT_PROTO_SCHEMA:-$project_root}"

# Optional argument: a JSON file. Otherwise send the sample request.
if [[ $# -gt 1 ]]; then
  echo "Usage: bash scripts/request-payment.sh [request.json]" >&2
  exit 2
fi
request_data='{"orderId":"demo-order-1","amountCents":"1500"}'
if [[ $# -eq 1 ]]; then
  if [[ ! -f "$1" ]]; then
    echo "Request file does not exist: $1" >&2
    exit 2
  fi
  request_data="@$1"
fi

# --schema loads the contract without requiring server reflection.
# --protocol grpc selects gRPC rather than the default Connect protocol.
# Buf converts the JSON request into Protobuf bytes for the network call.
# The URL path is the proto package + service name + method name.
exec buf curl \
  --schema "$schema" \
  --protocol grpc \
  --connect-timeout 5 \
  --timeout 20s \
  --data "$request_data" \
  "${payment_url%/}/payment.PaymentService/ProcessPayment"
