package com.gelinski.service.announcement;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.announcement.UpdateAnnouncementEnum;
import com.gelinski.dto.request.announcement.UpdateAnnouncementRequest;
import com.gelinski.entity.Account;
import com.gelinski.entity.Announcement;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.AnnouncementRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class UpdateAnnouncementService {
    public BaseResponseDTO updateAnnouncement(UpdateAnnouncementRequest request, Set<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.INVALID_TOKEN);
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.UNKNOWN_ERROR);
        }
        if (loggedUser.isEmpty()) {
            return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.INVALID_TOKEN);
        }

        if (Boolean.FALSE.equals(loggedUser.get().getIsAdmin())) {
            return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.INVALID_TOKEN);
        }

        Announcement announcement = null;
        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            Optional<Announcement> optionalAnnouncement = announcementRepository.getAnnouncementById(request.getId());
            if (optionalAnnouncement.isEmpty()) {
                return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.BAD_REQUEST);
            }
            announcement = optionalAnnouncement.get();
        } catch (SQLException e) {
            return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.UNKNOWN_ERROR);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);

            String title = isNonEmpty(request.getTitle()) ? request.getTitle() : announcement.getTitle();
            String text = isNonEmpty(request.getText()) ? request.getText() : announcement.getText();
            String categoryId = isNonEmpty(request.getCategoryId()) ? request.getCategoryId() : String.valueOf(announcement.getCategoryId());
            announcementRepository.updateAnnouncement(title, text, request.getId(), categoryId);
        } catch (SQLException e) {
            return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.UNKNOWN_ERROR);
        }

        return getUpdateAnnouncementResponse(UpdateAnnouncementEnum.ANNOUNCEMENT_UPDATED_SUCCESSFULLY);
    }

    private BaseResponseDTO getUpdateAnnouncementResponse(UpdateAnnouncementEnum updateAnnouncementEnum) {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setResponse(updateAnnouncementEnum.getCode());
        response.setMessage(updateAnnouncementEnum.getMessage());
        return response;
    }

    private boolean isNonEmpty(String value) {
        return Objects.nonNull(value) && !value.isEmpty();
    }
}
