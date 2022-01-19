package com.pktech.os.api.service;

import com.pktech.os.api.common.Payment;
import com.pktech.os.api.common.TransactionRequest;
import com.pktech.os.api.common.TransactionResponse;
import com.pktech.os.api.entity.Order;
import com.pktech.os.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    private RestTemplate template;

    @Autowired
    private OrderRepository repository;

    public TransactionResponse saveOrder(TransactionRequest request){
        String response="";
        Order order = request.getOrder();
        Payment payment = request.getPayment();
        payment.setOrderId(order.getId());
        payment.setAmount(order.getPrice());
        //rest call
       Payment paymentResponse = template.postForObject("http://PAYMENT-SERVICE/payment/doPayment",payment, Payment.class);

       response=paymentResponse.getPaymentStatus().equals("success")? "payment processing successful and order placed": "there in payment api, order added to cart";
         repository.save(order);
         return  new TransactionResponse(order, paymentResponse.getAmount(),paymentResponse.getTransactionId(),response) ;

    }
}
