package com.boardproject.projectboard.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {
    List<String> findAllDistinctHashtags();
}