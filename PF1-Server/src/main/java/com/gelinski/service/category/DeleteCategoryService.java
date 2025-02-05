package com.gelinski.service.category;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.category.DeleteCategoryEnum;
import com.gelinski.dto.request.category.Category;
import com.gelinski.dto.request.category.DeleteCategoryRequest;
import com.gelinski.dto.response.category.DeleteCategoryResponse;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DeleteCategoryService {

    public DeleteCategoryResponse deleteCategory(DeleteCategoryRequest request, String loggedUserToken) {
        if (Objects.isNull(loggedUserToken) || loggedUserToken.isEmpty()) {
            return getDeleteCategoryResponse(DeleteCategoryEnum.INVALID_TOKEN);
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
            return getDeleteCategoryResponse(DeleteCategoryEnum.UNKNOWN_ERROR);
        }

        if (!loggedUser.get().getIsAdmin()) {
            return getDeleteCategoryResponse(DeleteCategoryEnum.INVALID_TOKEN);
        }

        if (request.getCategoryIds().stream().anyMatch(String::isEmpty)) {
            return getDeleteCategoryResponse(DeleteCategoryEnum.MISSING_FIELDS);
        }

        List<Category> categories;
        try {
            Connection conn = DatabaseConfig.connect();
            CategoryRepository categoryRepository = new CategoryRepository(conn);
            categories = categoryRepository.getCategories();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (request.getCategoryIds().stream().anyMatch(categoryId -> categories.stream().noneMatch(category -> category.getId().equals(categoryId)))) {
            return getDeleteCategoryResponse(DeleteCategoryEnum.INVALID_INPUT);
        }

        request.getCategoryIds().forEach(categoryId -> {
            try {
                Connection conn = DatabaseConfig.connect();
                CategoryRepository categoryRepository = new CategoryRepository(conn);
                categoryRepository.deleteCategory(Long.parseLong(categoryId));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        return getDeleteCategoryResponse(DeleteCategoryEnum.SUCCESS);
    }

    private DeleteCategoryResponse getDeleteCategoryResponse(DeleteCategoryEnum deleteCategoryEnum) {
        DeleteCategoryResponse deleteCategoryResponse = new DeleteCategoryResponse();
        deleteCategoryResponse.setResponse(deleteCategoryEnum.getCode());
        deleteCategoryResponse.setMessage(deleteCategoryEnum.getMessage());
        return deleteCategoryResponse;
    }
}
