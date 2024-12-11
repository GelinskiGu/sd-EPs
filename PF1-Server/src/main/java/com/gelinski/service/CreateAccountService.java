package com.gelinski.service;

import com.gelinski.dto.enums.CreateAccountResponsesEnum;
import com.gelinski.dto.enums.LoginResponsesEnum;
import com.gelinski.dto.request.CreateAccountRequest;
import com.gelinski.dto.response.CreateAccountResponse;
import com.gelinski.dto.response.LoginResponse;
import com.gelinski.entity.Account;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CreateAccountService {
    private final DatabaseService databaseService;

    public CreateAccountResponse createAccount(CreateAccountRequest request) {
        if (Boolean.TRUE.equals(request.fieldsMissing())) {
            CreateAccountResponsesEnum fieldsMissing = CreateAccountResponsesEnum.FIELDS_MISSING;
            return getAccountCreateResponse(fieldsMissing);
        }

        if (Boolean.TRUE.equals(request.invalidInformation())) {
            CreateAccountResponsesEnum invalidFields = CreateAccountResponsesEnum.INVALID_INFORMATION;
            return getAccountCreateResponse(invalidFields);
        }


        Optional<Account> optionalAccount = databaseService.findByUser(request.getUser());
        if (optionalAccount.isPresent()) {
            CreateAccountResponsesEnum userAlreadyExists = CreateAccountResponsesEnum.USER_ALREADY_EXISTS;
            return getAccountCreateResponse(userAlreadyExists);
        }

        try {
            System.out.println("Saving account user: " + request.getUser());
            databaseService.saveAccount(request);
            return getAccountCreateResponse(CreateAccountResponsesEnum.SUCCESSFUL_ACCOUNT_CREATION);
        } catch (Exception e) {
            e.printStackTrace();
            return getAccountCreateResponse(CreateAccountResponsesEnum.ERROR_CREATE_ACCOUNT);
        }
    }

    private static CreateAccountResponse getAccountCreateResponse(CreateAccountResponsesEnum createAccountResponsesEnum) {
        CreateAccountResponse response = new CreateAccountResponse();
        response.setCode(createAccountResponsesEnum.getCode());
        response.setMessage(createAccountResponsesEnum.getMessage());
        return response;
    }
}
