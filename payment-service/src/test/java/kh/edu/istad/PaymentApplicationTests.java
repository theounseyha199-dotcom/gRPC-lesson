package kh.edu.istad;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Check bean wiring without opening the gRPC server port; this is not a network test.
@SpringBootTest(properties = "spring.grpc.server.enabled=false")
class PaymentApplicationTests {
    @Test
    void contextLoads() {
    }
}
