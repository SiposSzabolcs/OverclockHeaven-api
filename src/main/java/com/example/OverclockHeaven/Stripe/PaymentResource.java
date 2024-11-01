package com.example.OverclockHeaven.Stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("payment")
public class PaymentResource {

    private PaymentService paymentService;

    @Autowired
    public PaymentResource(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody PaymentDTO paymentDTO)
            throws StripeException {

        Session session = paymentService.createCheckoutSession(paymentDTO);
        String sessionJson = session.toJson();

        return new ResponseEntity<>(sessionJson, HttpStatus.OK);
    }

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<String> retrieveSession(@PathVariable String sessionId) {
        try {
            System.out.println(sessionId);
            Session session = Session.retrieve(sessionId);
            return new ResponseEntity<>(session.toJson(), HttpStatus.OK);
        } catch (StripeException e) {
            System.out.println(sessionId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List> getProducts(){
        return ResponseEntity.ok().body(paymentService.getAllPayments());
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addPayment(@RequestBody Payment payment) {
        try {
            Payment savedPayment = paymentService.addPaymentToDB(payment);
            return new ResponseEntity<>(savedPayment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add payment", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}