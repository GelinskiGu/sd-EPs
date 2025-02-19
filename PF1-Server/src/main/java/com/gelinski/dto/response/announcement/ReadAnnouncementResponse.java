package com.gelinski.dto.response.announcement;

import com.gelinski.dto.BaseResponseDTO;
import com.gelinski.entity.Announcement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReadAnnouncementResponse extends BaseResponseDTO implements Serializable {
    List<Announcement> announcements;
}
