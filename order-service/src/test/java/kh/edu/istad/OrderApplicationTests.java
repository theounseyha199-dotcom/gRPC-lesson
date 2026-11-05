package kh.edu.istad;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.grpc.client.autoconfigure.GrpcClientProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.boot.test.context.SpringBootTest;

// Checks bean wiring; this does not send a real payment RPC.
@SpringBootTest(properties = "PAYMENT_GRPC_ADDRESS=localhost:19090")
class OrderApplicationTests {
    @Autowired
    private GrpcClientProperties grpcClientProperties;

    @Test
    void paymentChannelUsesConfiguredTargetWithoutTls() {
        var channel = grpcClientProperties.getChannel().get("payment");
        assertNotNull(channel);
        assertEquals("localhost:19090", channel.getTarget());
        assertEquals(Boolean.FALSE, channel.getSsl().getEnabled());
    }
}
