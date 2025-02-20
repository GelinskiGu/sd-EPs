package com.gelinski.service.account;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.account.DeleteAccountEnum;
import com.gelinski.dto.enums.account.UpdateAccountEnum;
import com.gelinski.dto.request.account.DeleteAccountRequest;
import com.gelinski.dto.response.account.DeleteAccountResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DeleteAccountService {

    public DeleteAccountResponse deleteAccount(DeleteAccountRequest request, Set<String> loggedUsersToken) {
        if (request.getUser().isEmpty()) {
            request.setUser(request.getToken());
        }

        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getDeleteAccountResponse(DeleteAccountEnum.ERROR_DELETE_ACCOUNT);
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
