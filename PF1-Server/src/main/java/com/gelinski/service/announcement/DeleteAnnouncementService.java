package com.gelinski.service.announcement;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.dto.enums.announcement.DeleteAnnouncementEnum;
import com.gelinski.dto.request.announcement.DeleteAnnouncementRequest;
import com.gelinski.entity.Account;
import com.gelinski.entity.Announcement;
import com.gelinski.repository.AccountRepository;
import com.gelinski.repository.AnnouncementRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DeleteAnnouncementService {
    public BaseResponseDTO deleteAnnouncement(DeleteAnnouncementRequest request, List<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.INVALID_TOKEN);
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.UNKNOWN_ERROR);
        }
        if (loggedUser.isEmpty()) {
            return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.INVALID_TOKEN);
        }

        if (Boolean.FALSE.equals(loggedUser.get().getIsAdmin())) {
            return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.INVALID_TOKEN);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            Optional<Announcement> optionalAnnouncement = announcementRepository.getAnnouncementById(request.getId());
            if (optionalAnnouncement.isEmpty()) {
                return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.BAD_REQUEST);
            }
        } catch (SQLException e) {
            return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.UNKNOWN_ERROR);
        }

        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            announcementRepository.deleteAnnouncement(request.getId());
        } catch (SQLException e) {
            return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.UNKNOWN_ERROR);
        }

        return getDeleteAnnouncementResponse(DeleteAnnouncementEnum.ANNOUNCEMENT_DELETED_SUCCESSFULLY);
    }

    private BaseResponseDTO getDeleteAnnouncementResponse(DeleteAnnouncementEnum deleteAnnouncementEnum) {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setResponse(deleteAnnouncementEnum.getCode());
        response.setMessage(deleteAnnouncementEnum.getMessage());
        return response;
    }
}
