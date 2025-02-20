package com.gelinski.service.announcement;

import com.gelinski.config.DatabaseConfig;
import com.gelinski.dto.enums.announcement.ReadAnnouncementEnum;
import com.gelinski.dto.request.announcement.ReadAnnouncementRequest;
import com.gelinski.dto.response.announcement.ReadAnnouncementResponse;
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

public class ReadAnnouncementService {
    public ReadAnnouncementResponse readAnnouncement(ReadAnnouncementRequest request, Set<String> loggedUsersToken) {
        if (Objects.isNull(loggedUsersToken) || loggedUsersToken.isEmpty() || loggedUsersToken.stream().noneMatch(loggedUser -> Objects.equals(loggedUser, request.getToken()))) {
            return getReadAnnouncementsResponse(ReadAnnouncementEnum.INVALID_TOKEN, new ReadAnnouncementResponse());
        }

        Optional<Account> loggedUser;
        try {
            Connection conn = DatabaseConfig.connect();
            AccountRepository accountRepository = new AccountRepository(conn);
            loggedUser = accountRepository.getByUser(request.getToken());
        } catch (SQLException e) {
            return getReadAnnouncementsResponse(ReadAnnouncementEnum.UNKNOWN_ERROR, new ReadAnnouncementResponse());
        }
        if (loggedUser.isEmpty()) {
            return getReadAnnouncementsResponse(ReadAnnouncementEnum.INVALID_TOKEN, new ReadAnnouncementResponse());
        }

        List<Announcement> announcements;
        try {
            Connection conn = DatabaseConfig.connect();
            AnnouncementRepository announcementRepository = new AnnouncementRepository(conn);
            if (Boolean.TRUE.equals(loggedUser.get().getIsAdmin())) {
                announcements = announcementRepository.getAnnouncements();
            } else {
                announcements = announcementRepository.getAnnouncementsUserSubscribed(loggedUser.get().getId().intValue());
            }
        } catch (SQLException e) {
            return getReadAnnouncementsResponse(ReadAnnouncementEnum.UNKNOWN_ERROR, new ReadAnnouncementResponse());
        }

        ReadAnnouncementResponse response = new ReadAnnouncementResponse();
        response.setAnnouncements(announcements);
        return getReadAnnouncementsResponse(ReadAnnouncementEnum.ANNOUNCEMENT_READ_SUCCESSFULLY, response);
    }

    private ReadAnnouncementResponse getReadAnnouncementsResponse(ReadAnnouncementEnum readAnnouncementEnum, ReadAnnouncementResponse response) {
        response.setResponse(readAnnouncementEnum.getCode());
        response.setMessage(readAnnouncementEnum.getMessage());
        return response;
    }
}
