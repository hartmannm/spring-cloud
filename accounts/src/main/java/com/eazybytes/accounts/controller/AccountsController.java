package com.eazybytes.accounts.controller;

import com.eazybytes.accounts.model.Accounts;
import com.eazybytes.accounts.model.Customer;
import com.eazybytes.accounts.repository.AccountsRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository accountsRepository;

    @PostMapping("/myAccount")
    @Timed(value = "getAccountDetails.time", description = "Time taken to return Account Details")
    public Accounts getAccountDetails(@RequestBody Customer customer) {
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
        if (accounts != null) {
            return accounts;
        } else {
            return null;
        }
    }

}
