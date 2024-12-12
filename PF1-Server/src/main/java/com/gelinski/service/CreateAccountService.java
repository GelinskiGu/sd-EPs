package com.gelinski.service;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.CreateAccountResponsesEnum;
import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.response.CreateAccountResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
public class CreateAccountService {
    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        if (Boolean.TRUE.equals(request.fieldsMissing())) {
            CreateAccountResponsesEnum fieldsMissing = CreateAccountResponsesEnum.FIELDS_MISSING;
            return getAccountCreateResponse(fieldsMissing);
        }

        if (Boolean.TRUE.equals(request.invalidInformation())) {
            CreateAccountResponsesEnum invalidFields = CreateAccountResponsesEnum.INVALID_INFORMATION;
            return getAccountCreateResponse(invalidFields);
        }

        Optional<Account> optionalAccount;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            optionalAccount = accountRepository.getByUser(request.getUser());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (optionalAccount.isPresent()) {
            CreateAccountResponsesEnum userAlreadyExists = CreateAccountResponsesEnum.USER_ALREADY_EXISTS;
            return getAccountCreateResponse(userAlreadyExists);
        }

        try {
            System.out.println("Saving account user: " + request.getUser());
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);

            accountRepository.createAccount(request.getName(), request.getUser(), request.getPassword());
            return getAccountCreateResponse(CreateAccountResponsesEnum.SUCCESSFUL_ACCOUNT_CREATION);
        } catch (Exception e) {
            e.printStackTrace();
            return getAccountCreateResponse(CreateAccountResponsesEnum.ERROR_CREATE_ACCOUNT);
        }
    }

    private static CreateAccountResponse getAccountCreateResponse(CreateAccountResponsesEnum createAccountResponsesEnum) {
        CreateAccountResponse response = new CreateAccountResponse();
        response.setResponse(createAccountResponsesEnum.getCode());
        response.setMessage(createAccountResponsesEnum.getMessage());
        return response;
    }
}
