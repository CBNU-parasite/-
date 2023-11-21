package com.boardproject.projectboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public String articles(ModelMap map) {
        map.addAttribute("articles", List.of());
        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String article(@PathVariable Long articleId, ModelMap map) {
//        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));

        map.addAttribute("article", "article"); // TODO:다음에 꼭 바꿔야 할 것
        map.addAttribute("articleComments", List.of());
//        map.addAttribute("totalCount", articleService.getArticleCount());
//        map.addAttribute("searchTypeHashtag", SearchType.HASHTAG);

        return "articles/detail";
    }
}
