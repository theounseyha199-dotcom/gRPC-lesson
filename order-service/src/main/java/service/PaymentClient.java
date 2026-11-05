package service;

import com.example.common.grpc.payment.PaymentRequest;
import com.example.common.grpc.payment.PaymentResponse;
import com.example.common.grpc.payment.PaymentServiceGrpc;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class PaymentClient {

    // Generated client API, supplied as a Spring bean by PaymentConfiguration.
    private final PaymentServiceGrpc.PaymentServiceBlockingStub stub;

    public PaymentClient(
            PaymentServiceGrpc.PaymentServiceBlockingStub stub) {
        this.stub = stub;
    }

    public PaymentResponse process(String orderId, long amountCents) {
        // Build the protobuf message defined in common-grpc, using integer cents.
        PaymentRequest request = PaymentRequest.newBuilder()
                .setOrderId(orderId)
                .setAmountCents(amountCents)
                .build();

        // Performs a network RPC and blocks for its response; this is not a local method call.
        return stub.withDeadlineAfter(15, TimeUnit.SECONDS).processPayment(request);
    }
}
