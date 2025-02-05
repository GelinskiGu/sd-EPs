package com.gelinski.service.category;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.category.CreateCategoryEnum;
import com.gelinski.dto.enums.category.UpdateCategoryEnum;
import com.gelinski.dto.request.category.Category;
import com.gelinski.dto.request.category.CreateCategoryRequest;
import com.gelinski.dto.response.category.UpdateCategoryResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UpdateCategoryService {
    public UpdateCategoryResponse updateCategory(CreateCategoryRequest request, String loggedUserToken) {
        if (Objects.isNull(loggedUserToken) || loggedUserToken.isEmpty()) {
            return getUpdateCategoryResponse(UpdateCategoryEnum.INVALID_TOKEN);
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
            return getUpdateCategoryResponse(UpdateCategoryEnum.UNKNOWN_ERROR);
        }

        if (!loggedUser.get().getIsAdmin()) {
            return getUpdateCategoryResponse(UpdateCategoryEnum.INVALID_TOKEN);
        }

        if (request.getCategories().stream().anyMatch(category -> category.getName().isEmpty() || category.getId().isEmpty())) {
            return getUpdateCategoryResponse(UpdateCategoryEnum.MISSING_FIELDS);
        }

        List<Category> categories;
        try {
            Connection conn = DatabaseConfig.connect();
            CategoryRepository categoryRepository = new CategoryRepository(conn);
            categories = categoryRepository.getCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        request.getCategories().forEach(category -> {
            Category categoryToUpdate = categories.stream().filter(c -> c.getId().equals(category.getId())).findFirst().orElse(null);
            if (Objects.isNull(categoryToUpdate)) {
                return;
            }
            try {
                Connection conn = DatabaseConfig.connect();
                CategoryRepository categoryRepository = new CategoryRepository(conn);
                String name = isNonEmpty(category.getName()) ? category.getName() : categoryToUpdate.getName();
                String description = isNonEmpty(category.getDescription()) ? category.getDescription() : categoryToUpdate.getDescription();
                categoryRepository.updateCategory(name, description, Long.valueOf(category.getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return getUpdateCategoryResponse(UpdateCategoryEnum.SUCCESS);
    }

    private UpdateCategoryResponse getUpdateCategoryResponse(UpdateCategoryEnum updateCategoryEnum) {
        UpdateCategoryResponse updateCategoryResponse = new UpdateCategoryResponse();
        updateCategoryResponse.setResponse(updateCategoryEnum.getCode());
        updateCategoryResponse.setMessage(updateCategoryEnum.getMessage());
        return updateCategoryResponse;
    }

    private boolean isNonEmpty(String value) {
        return Objects.nonNull(value) && !value.isEmpty();
    }
}
