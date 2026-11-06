package kh.edu.istad;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Start the server on an available port to catch SDK/runtime incompatibilities.
@SpringBootTest(properties = "spring.grpc.server.port=0")
class PaymentApplicationTests {
    @Test
    void contextLoads() {
    }
}
