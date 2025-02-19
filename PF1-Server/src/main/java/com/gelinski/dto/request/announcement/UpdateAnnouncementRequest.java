package com.gelinski.dto.request.announcement;

import com.gelinski.dto.BaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateAnnouncementRequest extends BaseRequestDTO implements Serializable {
    private String token;
    private String id;
    private String title;
    private String text;
    private String categoryId;
}
