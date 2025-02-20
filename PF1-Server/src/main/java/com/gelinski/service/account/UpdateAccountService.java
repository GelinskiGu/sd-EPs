package com.gelinski.service.account;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.account.UpdateAccountEnum;
import com.gelinski.dto.enums.announcement.DeleteAnnouncementEnum;
import com.gelinski.dto.request.account.UpdateAccountRequest;
import com.gelinski.dto.response.account.UpdateAccountResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class UpdateAccountService {

    public UpdateAccountResponse updateAccount(UpdateAccountRequest updateAccountRequest, Set<String> loggedUsersToken) {
        if (updateAccountRequest.getUser().isEmpty()) {
            updateAccountRequest.setUser(updateAccountRequest.getToken());
        }

        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, updateAccountRequest.getToken()))) {
            return getUpdateAccountResponse(UpdateAccountEnum.ERROR_UPDATE_ACCOUNT);
        }

        String loggedUserToken = loggedUsersToken.stream().filter(loggedUser -> Objects.equals(loggedUser, updateAccountRequest.getToken())).findFirst().orElse(null);
        if (Objects.isNull(updateAccountRequest.getToken()) || updateAccountRequest.getToken().isEmpty() || !Objects.equals(loggedUserToken, updateAccountRequest.getToken())) {
            return getUpdateAccountResponse(UpdateAccountEnum.INVALID_OR_EMPTY_TOKEN);
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(updateAccountRequest.getToken());
        } catch (SQLException e) {
            return getUpdateAccountResponse(UpdateAccountEnum.ERROR_UPDATE_ACCOUNT);
        }
        if (loggedUser.isEmpty()) {
            return getUpdateAccountResponse(UpdateAccountEnum.ERROR_UPDATE_ACCOUNT);
        }

        if (!Objects.equals(updateAccountRequest.getUser(), updateAccountRequest.getToken()) && !loggedUser.get().getIsAdmin()) {
            return getUpdateAccountResponse(UpdateAccountEnum.INVALID_PERMISSION);
        }

        Optional<Account> optionalUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            optionalUser = accountRepository.getByUser(updateAccountRequest.getUser());
        } catch (SQLException e) {
            return getUpdateAccountResponse(UpdateAccountEnum.ERROR_UPDATE_ACCOUNT);
        }
        if (optionalUser.isEmpty()) {
            return getUpdateAccountResponse(UpdateAccountEnum.USER_NOT_FOUND);
        }

        Account account = optionalUser.get();
        account.setName(isNonEmpty(updateAccountRequest.getName()) ? updateAccountRequest.getName() : account.getName());
        account.setUser(isNonEmpty(updateAccountRequest.getUser()) ? updateAccountRequest.getUser() : account.getUser());
        account.setPassword(isNonEmpty(updateAccountRequest.getPassword()) ? updateAccountRequest.getPassword() : account.getPassword());

        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            accountRepository.updateAccount(account.getName(), account.getUser(), account.getPassword(), account.getId());
            return getUpdateAccountResponse(UpdateAccountEnum.SUCCESS);
        } catch (SQLException e) {
            return getUpdateAccountResponse(UpdateAccountEnum.ERROR_UPDATE_ACCOUNT);
        }
    }

    private boolean isNonEmpty(String value) {
        return Objects.nonNull(value) && !value.isEmpty();
    }

    private UpdateAccountResponse getUpdateAccountResponse(UpdateAccountEnum updateAccountEnum) {
        UpdateAccountResponse updateAccountResponse = new UpdateAccountResponse();
        updateAccountResponse.setResponse(updateAccountEnum.getCode());
        updateAccountResponse.setMessage(updateAccountEnum.getMessage());
        return updateAccountResponse;
    }
}
