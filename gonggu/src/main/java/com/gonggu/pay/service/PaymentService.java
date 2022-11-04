package com.gonggu.pay.service;

import com.gonggu.pay.domain.Account;
import com.gonggu.pay.domain.Payment;
import com.gonggu.pay.domain.Transaction;
import com.gonggu.pay.domain.User;
import com.gonggu.pay.repository.AccountRepository;
import com.gonggu.pay.repository.PaymentRepository;
import com.gonggu.pay.repository.TransactionRepository;
import com.gonggu.pay.repository.UserRepository;
import com.gonggu.pay.request.PaymentCharge;
import com.gonggu.pay.request.RemitRequest;
import com.gonggu.pay.request.TransactionRequest;
import com.gonggu.pay.request.UserTemp;
import com.gonggu.pay.response.PaymentInfo;
import com.gonggu.pay.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public User findUserTemp(UserTemp userTemp){
        return userRepository.findById(userTemp.getId()).orElseThrow();
    }

    public PaymentInfo getInfo(User user) {
        Payment payment = paymentRepository.findByUser(user);
        PaymentInfo paymentInfo = new PaymentInfo(payment);

        return paymentInfo;
    }

    public void charge(PaymentCharge paymentCharge, User user) {
        Account account = accountRepository.findByUser(user);
        if(account.getBalance() < paymentCharge.getRequestCoin()) return;
        //거래 업데이트
        Payment payment = paymentRepository.findByUser(user);
        account.minusBalance(paymentCharge.getRequestCoin());
        payment.plusBalance(paymentCharge.getRequestCoin());

    }

    public void discharge(PaymentCharge paymentCharge, User user) {
        Payment payment = paymentRepository.findByUser(user);
        if(payment.getBalance() < paymentCharge.getRequestCoin()) return;
        //거래 업데이트
        Account account = accountRepository.findByUser(user);
        payment.minusBalance(paymentCharge.getRequestCoin());
        account.plusBalance(paymentCharge.getRequestCoin());
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public void remit(RemitRequest request) {
        User from = userRepository.findById(request.getFrom()).orElseThrow();
        User to = userRepository.findById(request.getTo()).orElseThrow();
        Payment fromPayment = paymentRepository.findByUser(from);
        if(fromPayment.getBalance() < request.getAmount()) return;
        //송금 로직

        Payment toPayment = paymentRepository.findByUser(to);

        fromPayment.minusBalance(request.getAmount());
        toPayment.plusBalance(request.getAmount());

        //거래내역 생성
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = Transaction.builder()
                .from(from)
                .to(to)
                .amount(request.getAmount())
                .date(now).build();
        transactionRepository.save(transaction);
    }

    public List<TransactionResponse> getMyTransaction(TransactionRequest transactionRequest, User user){
        return transactionRepository.getList(user, transactionRequest).stream()
                .map(TransactionResponse::new).collect(Collectors.toList());
    }
}
