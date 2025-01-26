package com.example.News.App2.Controller;

import com.example.News.App2.Reponse.NewsResponse;
import com.example.News.App2.Service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/news")
@CrossOrigin(origins = "*")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/{query}")
    public ResponseEntity<?> getNews(@PathVariable String query) {
        NewsResponse newsResponse = newsService.getNews(query);

        if (newsResponse == null || newsResponse.getArticles() == null || newsResponse.getArticles().isEmpty()) {
            System.out.println("No news data found for query: " + query);
            return new ResponseEntity<>("No news data found.", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> responseData = formatNewsArticles(newsResponse.getArticles());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomNews() {
        List<NewsResponse.Article> randomArticles = newsService.getRandomNews();

        if (randomArticles.isEmpty()) {
            System.out.println("No random news data available.");
            return new ResponseEntity<>("No random news data available.", HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> responseData = formatNewsArticles(randomArticles);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    private List<Map<String, Object>> formatNewsArticles(List<NewsResponse.Article> articles) {
        List<Map<String, Object>> formattedArticles = new ArrayList<>();
        for (NewsResponse.Article article : articles) {
            Map<String, Object> articleData = new HashMap<>();
            articleData.put("title", article.getTitle());
            articleData.put("description", article.getDescription());
            articleData.put("url", article.getUrl());
            articleData.put("urlToImage", article.getUrlToImage());
            articleData.put("publishedAt", article.getPublishedAt());
            articleData.put("author", article.getAuthor());
            if (article.getSource() != null) {
                articleData.put("sourceName", article.getSource().getName());
            }
            formattedArticles.add(articleData);
        }
        return formattedArticles;
    }
}



