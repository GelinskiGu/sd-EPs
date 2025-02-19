package com.gelinski.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Announcement {
    private String id;
    private String title;
    private String text;
    private String date;
    private String categoryId;
}
