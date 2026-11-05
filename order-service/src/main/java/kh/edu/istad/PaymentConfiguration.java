package kh.edu.istad;

import com.example.common.grpc.payment.PaymentServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class PaymentConfiguration {
    // Reuse the generated blocking stub through constructor injection in PaymentClient.
    @Bean
    PaymentServiceGrpc.PaymentServiceBlockingStub paymentStub(GrpcChannelFactory channels) {
        // 'payment' selects spring.grpc.client.channel.payment.* in application.yaml.
        return PaymentServiceGrpc.newBlockingStub(channels.createChannel("payment"));
    }
}
