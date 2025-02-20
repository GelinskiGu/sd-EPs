package com.gelinski.service.category;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.category.SubscribeToCategoryEnum;
import com.gelinski.dto.request.category.Category;
import com.gelinski.dto.request.category.SubscribeToCategoryRequest;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.AnnouncementRepository;
import com.gelinski.repository.CategoryRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class SubscribeToCategoryService {
    public BaseResponseDTO subscribe(SubscribeToCategoryRequest request, Set<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getSubscribeResponse(SubscribeToCategoryEnum.INVALID_TOKEN);
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            return getSubscribeResponse(SubscribeToCategoryEnum.UNKNOWN_ERROR);
        }
        if (loggedUser.isEmpty()) {
            return getSubscribeResponse(SubscribeToCategoryEnum.INVALID_TOKEN);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            CategoryRepository categoryRepository = new CategoryRepository(conn);
            Optional<Category> optionalAnnouncement = categoryRepository.getCategoryById(request.getCategoryId());
            if (optionalAnnouncement.isEmpty()) {
                return getSubscribeResponse(SubscribeToCategoryEnum.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return getSubscribeResponse(SubscribeToCategoryEnum.UNKNOWN_ERROR);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            announcementRepository.subscribeToCategory(String.valueOf(loggedUser.get().getId()), request.getCategoryId());
        } catch (SQLException e) {
            return getSubscribeResponse(SubscribeToCategoryEnum.UNKNOWN_ERROR);
        }

        return getSubscribeResponse(SubscribeToCategoryEnum.SUBSCRIBE_SUCCESSFUL);
    }

    private BaseResponseDTO getSubscribeResponse(SubscribeToCategoryEnum subscribeToCategoryEnum) {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setResponse(subscribeToCategoryEnum.getCode());
        response.setMessage(subscribeToCategoryEnum.getMessage());
        return response;
    }
}
