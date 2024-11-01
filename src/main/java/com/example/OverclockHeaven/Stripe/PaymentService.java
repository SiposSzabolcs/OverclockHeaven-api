package com.example.OverclockHeaven.Stripe;

import com.example.OverclockHeaven.Products.Product;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.financialconnections.SessionRetrieveParams;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, @Value("${STRIPE_API_KEY}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
    }

    public List<Payment> getAllPayments(){
        return paymentRepository.findAll();
    }

    public PaymentIntent createPaymentIntent(PaymentDTO PaymentDTO) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", PaymentDTO.getAmount());
        params.put("currency", PaymentDTO.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }

    public Session createCheckoutSession(PaymentDTO paymentDTO) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", java.util.List.of("card"));

        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("price_data", Map.of(
                "currency", paymentDTO.getCurrency(),
                "product_data", Map.of(
                        "name", "Product Name",
                        "description", "Product Description"
                ),
                "unit_amount", paymentDTO.getAmount()
        ));
        lineItem.put("quantity", 1);
        params.put("line_items", java.util.List.of(lineItem));

        params.put("mode", "payment");
        params.put("success_url", "http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}");
        params.put("cancel_url", "http://localhost:4200/cancel");

        return Session.create(params);
    }

    public ResponseEntity<String> stripePayment(String userEmail) throws Exception {
        Payment payment = paymentRepository.findByUserEmail(userEmail);

        if (payment == null) {
            throw new Exception("Payment information is missing" + "\n" + userEmail);
        }
        payment.setAmount(100.00);
        paymentRepository.save(payment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Payment addPaymentToDB(Payment payment){
        return paymentRepository.save(payment);
    };
}
