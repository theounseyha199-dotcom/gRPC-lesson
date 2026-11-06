package service;

import build.buf.gen.payment.PaymentRequest;
import build.buf.gen.payment.PaymentResponse;
import build.buf.gen.payment.PaymentServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class PaymentGrpcService extends PaymentServiceGrpc.PaymentServiceImplBase {
    @Override
    public void processPayment(PaymentRequest request, StreamObserver<PaymentResponse> observer) {
        if (request.getOrderId().isBlank() || request.getAmountCents() <= 0) {
            observer.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid orderId or amountCents").asRuntimeException());
            return;
        }

        // Simulate payment for the gRPC demo; no money is transferred.
        observer.onNext(PaymentResponse.newBuilder()
                .setPaymentId("demo-" + UUID.randomUUID())
                .setSuccess(true)
                .setMessage("Demo payment successful (simulated)")
                .build());
        observer.onCompleted();
    }
}
