package com.addjve.springdatajdbc.services;

import com.addjve.springdatajdbc.exceptions.AccountNotFoundException;
import com.addjve.springdatajdbc.model.Account;
import com.addjve.springdatajdbc.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransferService {
    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transferMoney(
            long idSender,
            long idReceiver,
            BigDecimal amount
    ) {
        // Getting info about sender and receiver's amounts.
        Account sender =
                accountRepository.findById(idSender)
                        .orElseThrow(() -> new AccountNotFoundException());
        Account receiver =
                accountRepository.findById(idReceiver)
                        .orElseThrow(() -> new AccountNotFoundException());

        // Calculating the new balances of these accounts, both withdrawing and crediting operations between accounts.
        BigDecimal senderNewAmount =
                sender.getAmount().subtract(amount);

        BigDecimal receiverNewAmount = receiver.getAmount().add(amount);

        // Changing balances in DB.
        accountRepository
                .changeAmount(idSender, senderNewAmount);

        accountRepository
                .changeAmount(idReceiver, receiverNewAmount);
    }

    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll(); // CrudRepository <- Spring Data
    }

    public List<Account> findAccountsByName(String name) {
        return accountRepository.findAccountByName(name);
    }
}
