package com.example.OverclockHeaven.Stripe;

import lombok.Data;

@Data
public class PaymentDTO {
    private int amount;
    private String currency;
    private String receiptEmail;
}
