package kh.edu.istad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Include the service/controller packages because they sit outside kh.edu.istad.
@SpringBootApplication(scanBasePackages = {"kh.edu.istad", "service", "controller"})
public class PaymentApplication {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }
}
