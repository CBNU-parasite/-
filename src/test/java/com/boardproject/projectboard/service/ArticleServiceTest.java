//package com.boardproject.projectboard.service;
//
//import com.boardproject.projectboard.domain.Article;
//import com.boardproject.projectboard.domain.UserAccount;
//import com.boardproject.projectboard.dto.ArticleDto;
//import com.boardproject.projectboard.dto.ArticleWithCommentsDto;
//import com.boardproject.projectboard.dto.UserAccountDto;
//import com.boardproject.projectboard.repository.ArticleRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.BDDMockito.*;
//
//@DisplayName("business logic - article")
//@ExtendWith(MockitoExtension.class)
//class ArticleServiceTest {
//
//    @InjectMocks private ArticleService sut;
//
//    @Mock private ArticleRepository articleRepository;
//
//    @DisplayName("No searching articles, return article list")
//    @Test
//    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
//        // Given
//        Pageable pageable = Pageable.ofSize(20);
//        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
//        // When
//        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);
//
//        // Then
//        assertThat(articles).isEmpty();
//        then(articleRepository).should().findAll(pageable);
//
//    }
//
//    @DisplayName("Viewing article, return article")
//    @Test
//    void givenArticleId_whenSearchingArticle_thenReturnsArticle() {
//        // Given
//        Long articleId = 1L;
//        Article article = createArticle();
//        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
//
//        // When
//        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId); // title, content, ID, nickname, hashtag
//
//        // Then
//        assertThat(dto)
//                .hasFieldOrPropertyWithValue("title", article.getTitle())
//                .hasFieldOrPropertyWithValue("content", article.getContent())
//                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
//        then(articleRepository).should().findById(articleId);
//
//    }
//
//    @DisplayName("article id doesn't exist, throw exception")
//    @Test
//    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
//        // Given
//        Long articleId = 0L;
//        given(articleRepository.findById(articleId)).willReturn(Optional.empty());
//
//        // When
//        Throwable t = catchThrowable(() -> sut.getArticleWithComments(articleId));
//
//        // Then
//        assertThat(t)
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
//        then(articleRepository).should().findById(articleId);
//    }
//
//    @DisplayName("input article info, save article")
//    @Test
//    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
//        // Given
//        ArticleDto dto = createArticleDto();
//        given(articleRepository.save(any(Article.class))).willReturn(createArticle());
//
//        // When
//        sut.saveArticle(dto);
//
//        // Then
//        then(articleRepository).should().save(any(Article.class));
//    }
//
//    @DisplayName("When input modified article info, update article")
//    @Test
//    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
//        // Given
//        Article article = createArticle();
//        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
//        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
//
//        // When
//        sut.updateArticle(dto);
//
//        // Then
//        assertThat(article)
//                .hasFieldOrPropertyWithValue("title", dto.title())
//                .hasFieldOrPropertyWithValue("content", dto.content())
//                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
//        then(articleRepository).should().getReferenceById(dto.id());
//    }
//
//    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
//    @Test
//    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
//        // Given
//        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
//        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);
//
//        // When
//        sut.updateArticle(dto);
//
//        // Then
//        then(articleRepository).should().getReferenceById(dto.id());
//    }
//
//
//    @DisplayName("Input Article ID, delete article")
//    @Test
//    void givenArticleId_whenDeletingArticle_thenDeleteArticle() {
//        // Given
//        Long articleId = 1L;
//        willDoNothing().given(articleRepository).deleteById(articleId);
//
//        // When
//        sut.deleteArticle(1L);
//
//        // Then
//        then(articleRepository).should().deleteById(articleId);
//    }
//
//    @DisplayName("search articles number, return articles number")
//    @Test
//    void givenNothing_whenCountingArticles_thenReturnsArticleCount() {
//        // Given
//        long expected = 0L;
//        given(articleRepository.count()).willReturn(expected);
//
//        // When
//        long actual = sut.getArticleCount();
//
//        // Then
//        assertThat(actual).isEqualTo(expected);
//        then(articleRepository).should().count();
//    }
//
//    private UserAccount createUserAccount() {
//        return UserAccount.of(
//                "uno",
//                "password",
//                "uno@email.com",
//                "Uno",
//                null
//        );
//    }
//
//    private Article createArticle() {
//        return Article.of(
//                createUserAccount(),
//                "title",
//                "content",
//                "#java"
//        );
//    }
//
//    private ArticleDto createArticleDto() {
//        return createArticleDto("title", "content", "#java");
//    }
//
//    private ArticleDto createArticleDto(String title, String content, String hashtag) {
//        return ArticleDto.of(1L,
//                createUserAccountDto(),
//                title,
//                content,
//                hashtag,
//                LocalDateTime.now(),
//                "Uno",
//                LocalDateTime.now(),
//                "Uno");
//    }
//
//    private UserAccountDto createUserAccountDto() {
//        return UserAccountDto.of(
//                1L,
//                "uno",
//                "password",
//                "uno@mail.com",
//                "Uno",
//                "This is memo",
//                LocalDateTime.now(),
//                "uno",
//                LocalDateTime.now(),
//                "uno"
//        );
//    }
//
//}
