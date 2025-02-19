package com.gelinski.service.category;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.category.UnsubscribeToCategoryEnum;
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

public class UnsubscribeToCategoryService {
    public BaseResponseDTO unsubscribe(SubscribeToCategoryRequest request, List<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getUnsubscribeResponse(UnsubscribeToCategoryEnum.INVALID_TOKEN);
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            return getUnsubscribeResponse(UnsubscribeToCategoryEnum.UNKNOWN_ERROR);
        }
        if (loggedUser.isEmpty()) {
            return getUnsubscribeResponse(UnsubscribeToCategoryEnum.INVALID_TOKEN);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            CategoryRepository categoryRepository = new CategoryRepository(conn);
            Optional<Category> optionalAnnouncement = categoryRepository.getCategoryById(request.getCategoryId());
            if (optionalAnnouncement.isEmpty()) {
                return getUnsubscribeResponse(UnsubscribeToCategoryEnum.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return getUnsubscribeResponse(UnsubscribeToCategoryEnum.UNKNOWN_ERROR);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            announcementRepository.unsubscribeToCategory(String.valueOf(loggedUser.get().getId()), request.getCategoryId());
        } catch (SQLException e) {
            return getUnsubscribeResponse(UnsubscribeToCategoryEnum.UNKNOWN_ERROR);
        }

        return getUnsubscribeResponse(UnsubscribeToCategoryEnum.UNSUBSCRIBE_SUCCESSFUL);
    }

    private BaseResponseDTO getUnsubscribeResponse(UnsubscribeToCategoryEnum unsubscribeToCategoryEnum) {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setResponse(unsubscribeToCategoryEnum.getCode());
        response.setMessage(unsubscribeToCategoryEnum.getMessage());
        return response;
    }
}
