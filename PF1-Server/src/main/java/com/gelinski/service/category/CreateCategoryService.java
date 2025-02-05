package com.gelinski.service.category;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.category.CreateCategoryEnum;
import com.gelinski.dto.request.category.CreateCategoryRequest;
import com.gelinski.dto.response.category.CreateCategoryResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class CreateCategoryService {
    public CreateCategoryResponse createCategory(CreateCategoryRequest request, String loggedUserToken) {
        if (Objects.isNull(loggedUserToken) || loggedUserToken.isEmpty()) {
            return getCreatedCategoryResponse(CreateCategoryEnum.INVALID_TOKEN);
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
            return getCreatedCategoryResponse(CreateCategoryEnum.UNKNOWN_ERROR);
        }

        if (!loggedUser.get().getIsAdmin()) {
            return getCreatedCategoryResponse(CreateCategoryEnum.INVALID_TOKEN);
        }

        if (request.getCategories().stream().anyMatch(category -> category.getName().isEmpty())) {
            return getCreatedCategoryResponse(CreateCategoryEnum.MISSING_FIELDS);
        }

        request.getCategories().forEach(category -> {
            try {
                Connection conn = DatabaseConfig.connect();
                CategoryRepository categoryRepository = new CategoryRepository(conn);
                categoryRepository.createCategory(category.getName(), category.getDescription());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return getCreatedCategoryResponse(CreateCategoryEnum.CATEGORY_CREATED_SUCCESSFULLY);
    }

    private CreateCategoryResponse getCreatedCategoryResponse(CreateCategoryEnum createCategoryEnum) {
        CreateCategoryResponse createCategoryResponse = new CreateCategoryResponse();
        createCategoryResponse.setResponse(createCategoryEnum.getCode());
        createCategoryResponse.setMessage(createCategoryEnum.getMessage());
        return createCategoryResponse;
    }

}
