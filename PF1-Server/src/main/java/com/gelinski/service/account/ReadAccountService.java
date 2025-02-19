package com.gelinski.service.account;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.account.ReadAccountEnum;
import com.gelinski.dto.enums.announcement.DeleteAnnouncementEnum;
import com.gelinski.dto.request.account.ReadAccountRequest;
import com.gelinski.dto.response.account.ReadAccountResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ReadAccountService {

    public ReadAccountResponse readAccount(ReadAccountRequest readAccountRequest, List<String> loggedUsersToken) {
        if (readAccountRequest.getUser().isEmpty()) {
            readAccountRequest.setUser(readAccountRequest.getToken());
        }

        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, readAccountRequest.getToken()))) {
            return getReadAccountResponse(ReadAccountEnum.ERROR_READ_ACCOUNT, new ReadAccountResponse());
        }
        String loggedUserToken = loggedUsersToken.stream().filter(loggedUser -> Objects.equals(loggedUser, readAccountRequest.getToken())).findFirst().orElse(null);

        if (Objects.isNull(readAccountRequest.getToken()) || readAccountRequest.getToken().isEmpty() || !Objects.equals(loggedUserToken, readAccountRequest.getToken())) {
            return getReadAccountResponse(ReadAccountEnum.INVALID_OR_EMPTY_TOKEN, new ReadAccountResponse());
        }
        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(readAccountRequest.getToken());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (loggedUser.isEmpty()) {
            return getReadAccountResponse(ReadAccountEnum.ERROR_READ_ACCOUNT, new ReadAccountResponse());
        }

        if (!Objects.equals(readAccountRequest.getUser(), readAccountRequest.getToken()) && !loggedUser.get().getIsAdmin()) {
            return getReadAccountResponse(ReadAccountEnum.INVALID_PERMISSION, new ReadAccountResponse());
        }

        Optional<Account> optionalUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            optionalUser = accountRepository.getByUser(readAccountRequest.getUser());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (optionalUser.isEmpty()) {
            return getReadAccountResponse(ReadAccountEnum.USER_NOT_FOUND, new ReadAccountResponse());
        }

        ReadAccountResponse readAccountResponse = new ReadAccountResponse();
        readAccountResponse.setName(optionalUser.get().getName());
        readAccountResponse.setUser(optionalUser.get().getUser());
        readAccountResponse.setPassword(optionalUser.get().getPassword());
        if (loggedUser.get().getIsAdmin()) {
            return getReadAccountResponse(ReadAccountEnum.SUCCESSFUL_READ_ACCOUNT_ADMIN_USER, readAccountResponse);
        }

        return getReadAccountResponse(ReadAccountEnum.SUCCESSFUL_READ_ACCOUNT_NORMAL_USER, readAccountResponse);
    }

    private ReadAccountResponse getReadAccountResponse(ReadAccountEnum readAccountEnum, ReadAccountResponse readAccountResponse) {
        readAccountResponse.setResponse(readAccountEnum.getCode());
        readAccountResponse.setMessage(readAccountEnum.getMessage());
        return readAccountResponse;
    }
}
