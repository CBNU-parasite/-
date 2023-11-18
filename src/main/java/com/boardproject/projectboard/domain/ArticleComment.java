package com.boardproject.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleComment {
    private Long id;
    private String article;
    private String content;

    private LocalDateTime createAt;
    private String createBy;
    private LocalDateTime modifyAt;
    private String modifyBy;
}
