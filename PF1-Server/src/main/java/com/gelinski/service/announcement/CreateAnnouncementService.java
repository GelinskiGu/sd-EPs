package com.gelinski.service.announcement;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.announcement.CreateAnnouncementEnum;
import com.gelinski.dto.request.announcement.CreateAnnouncementRequest;
import com.gelinski.entity.Account;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.AnnouncementRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CreateAnnouncementService {
    public BaseResponseDTO createAnnouncement(CreateAnnouncementRequest request, List<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getCreatedAnnouncementResponse(CreateAnnouncementEnum.INVALID_TOKEN);
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            return getCreatedAnnouncementResponse(CreateAnnouncementEnum.UNKNOWN_ERROR);
        }
        if (loggedUser.isEmpty()) {
            return getCreatedAnnouncementResponse(CreateAnnouncementEnum.UNKNOWN_ERROR);
        }

        if (Boolean.FALSE.equals(loggedUser.get().getIsAdmin())) {
            return getCreatedAnnouncementResponse(CreateAnnouncementEnum.INVALID_TOKEN);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            announcementRepository.createAnnouncement(request.getTitle(), request.getText(), Integer.valueOf(request.getCategoryId()));
        } catch (SQLException e) {
            return getCreatedAnnouncementResponse(CreateAnnouncementEnum.UNKNOWN_ERROR);
        }

        return getCreatedAnnouncementResponse(CreateAnnouncementEnum.ANNOUNCEMENT_CREATED_SUCCESSFULLY);
    }

    private BaseResponseDTO getCreatedAnnouncementResponse(CreateAnnouncementEnum createAnnouncementEnum) {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setResponse(createAnnouncementEnum.getCode());
        response.setMessage(createAnnouncementEnum.getMessage());
        return response;
    }
}
