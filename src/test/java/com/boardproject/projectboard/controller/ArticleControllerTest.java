package com.boardproject.projectboard.controller;

import com.boardproject.projectboard.config.SecurityConfig;
import com.boardproject.projectboard.dto.ArticleWithCommentsDto;
import com.boardproject.projectboard.dto.UserAccountDto;
import com.boardproject.projectboard.service.ArticleService;
import com.boardproject.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View controller")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean
    private ArticleService articleService;

    @MockBean private PaginationService paginationService;

    ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

//    @Disabled("developing")
    @DisplayName("[view][GET] board detail list page- correct call")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
//                .andExpect(model().attributeExists("searchTypes"))
//                .andExpect(model().attribute("searchTypeHashtag", SearchType.HASHTAG));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

//    @Disabled("developing")
    @DisplayName("[view][GET] board detail list page- correct call")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnsArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        Long totalCount = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount()).willReturn(totalCount);

        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attribute("totalCount", totalCount));
        then(articleService).should().getArticle(articleId);
        then(articleService).should().getArticleCount();
    }

    @Disabled("developing")
    @DisplayName("[view][GET] article list - correct call")
    @Test
    void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Given
//        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
//        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
//                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles/search"));
//                .andExpect(model().attributeExists("paginationBarNumbers"))
//                .andExpect(model().attributeExists("searchTypes"))
//                .andExpect(model().attribute("searchTypeHashtag", SearchType.HASHTAG));
//        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
//        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @Disabled("developing")
    @DisplayName("[view][GET] board hashtag search page - correct call")
    @Test
    void givenNothing_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView() throws Exception {
        // Given
//        List<String> hashtags = List.of("#java", "#spring", "#boot");
//        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class))).willReturn(Page.empty());
//        given(articleService.getHashtags()).willReturn(hashtags);
//        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(1, 2, 3, 4, 5));

        // When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"));
//                .andExpect(model().attribute("articles", Page.empty()))
//                .andExpect(model().attribute("hashtags", hashtags))
//                .andExpect(model().attributeExists("paginationBarNumbers"))
//                .andExpect(model().attribute("searchType", SearchType.HASHTAG));
//        then(articleService).should().searchArticlesViaHashtag(eq(null), any(Pageable.class));
//        then(articleService).should().getHashtags();
//        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }


    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "uno",
                "pw",
                "uno@email.com",
                "memo",
                "memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }
}