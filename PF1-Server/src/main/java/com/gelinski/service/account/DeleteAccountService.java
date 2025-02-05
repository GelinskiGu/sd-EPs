package com.gelinski.service.account;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.account.DeleteAccountEnum;
import com.gelinski.dto.request.DeleteAccountRequest;
import com.gelinski.dto.response.account.DeleteAccountResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class DeleteAccountService {

    public DeleteAccountResponse deleteAccount(DeleteAccountRequest request, String loggedUserToken) {
        if (request.getUser().isEmpty()) {
            request.setUser(request.getToken());
        }

        Optional<Account> optionalUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            optionalUser = accountRepository.getByUser(request.getUser());
        } catch (SQLException e) {
            return getDeleteAccountResponse(DeleteAccountEnum.ERROR_DELETE_ACCOUNT);
        }
        if (optionalUser.isEmpty()) {
            return getDeleteAccountResponse(DeleteAccountEnum.USER_NOT_FOUND);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            accountRepository.deleteAccount(optionalUser.get().getId());
        } catch (SQLException e) {
            return getDeleteAccountResponse(DeleteAccountEnum.ERROR_DELETE_ACCOUNT);
        }

        return getDeleteAccountResponse(DeleteAccountEnum.ACCOUNT_DELETED);
    }

    private DeleteAccountResponse getDeleteAccountResponse(DeleteAccountEnum deleteAccountEnum) {
        DeleteAccountResponse response = new DeleteAccountResponse();
        response.setResponse(deleteAccountEnum.getCode());
        response.setMessage(deleteAccountEnum.getMessage());
        return response;
    }
}
