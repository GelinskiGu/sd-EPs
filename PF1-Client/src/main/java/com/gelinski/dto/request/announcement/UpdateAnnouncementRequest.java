package com.gelinski.dto.request.announcement;

import com.gelinski.dto.request.BaseRequestDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UpdateAnnouncementRequest extends BaseRequestDTO implements Serializable {
    private String token;
    private String id;
    private String title;
    private String text;
    private String categoryId;
}
