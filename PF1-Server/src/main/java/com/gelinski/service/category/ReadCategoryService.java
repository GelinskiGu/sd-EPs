package com.gelinski.service.category;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.category.ReadCategoryEnum;
import com.gelinski.dto.request.category.Category;
import com.gelinski.dto.request.category.ReadCategoryRequest;
import com.gelinski.dto.response.category.ReadCategoryResponse;
import com.gelinski.entity.Account;
import com.gelinski.entity.AccountCategory;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.AnnouncementRepository;
import com.gelinski.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ReadCategoryService {

    public ReadCategoryResponse readCategories(ReadCategoryRequest request, Set<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getReadAccountResponse(ReadCategoryEnum.INVALID_TOKEN, new ReadCategoryResponse());
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (loggedUser.isEmpty()) {
            return getReadAccountResponse(ReadCategoryEnum.UNKNOWN_ERROR, new ReadCategoryResponse());
        }

        List<Category> categories;
        try {
            Connection conn = DatabaseConfig.connect();
            CategoryRepository categoryRepository = new CategoryRepository(conn);
            categories = categoryRepository.getCategories();
        } catch (SQLException e) {
            return getReadAccountResponse(ReadCategoryEnum.UNKNOWN_ERROR, new ReadCategoryResponse());
        }

        try {
            categories.forEach(category -> {
                try {
                    Connection conn = DatabaseConfig.connect();
                    AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
                    Optional<AccountCategory> optionalAccountCategory = announcementRepository.getAccountCategory(loggedUser.get().getId().toString(), category.getId());
                    if (optionalAccountCategory.isPresent()) {
                        category.setSubscribed("true");
                    } else {
                        category.setSubscribed("false");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            return getReadAccountResponse(ReadCategoryEnum.UNKNOWN_ERROR, new ReadCategoryResponse());
        }

        ReadCategoryResponse response = new ReadCategoryResponse();
        response.setCategories(categories);
        return getReadAccountResponse(ReadCategoryEnum.SUCCESS, response);
    }

    private ReadCategoryResponse getReadAccountResponse(ReadCategoryEnum readAccountEnum, ReadCategoryResponse response) {
        response.setResponse(readAccountEnum.getCode());
        response.setMessage(readAccountEnum.getMessage());
        return response;
    }
}
