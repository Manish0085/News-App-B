package com.example.News.App2.Service;

import com.example.News.App2.Reponse.NewsResponse;
import com.example.News.App2.Reponse.NewsResponse.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Value("${news.api.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    private static final String URL_TEMPLATE = "https://newsapi.org/v2/everything?q=QUERY&sortBy=popularity&apiKey=API_KEY";
    private static final String RANDOM_NEWS_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=API_KEY";



    // Fetch news based on a specific query
    public NewsResponse getNews(String query) {
        String finalAPI = URL_TEMPLATE.replace("QUERY", query).replace("API_KEY", apiKey);
        System.out.println("Final API URL: " + finalAPI);

        try {
            ResponseEntity<String> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, String.class);

            System.out.println("API Response Status Code: " + response.getStatusCode());
            System.out.println("Raw JSON Response: " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            NewsResponse newsResponse = objectMapper.readValue(response.getBody(), NewsResponse.class);

            if (newsResponse.getTotalResults() == 0 || newsResponse.getArticles() == null || newsResponse.getArticles().isEmpty()) {
                System.out.println("No articles found for query: " + query);
                return null;
            }

            return newsResponse;

        } catch (Exception e) {
            System.err.println("Error occurred while fetching news data: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Fetch random news articles
    public List<Article> getRandomNews() {
        String finalAPI = RANDOM_NEWS_URL.replace("API_KEY", apiKey);
        System.out.println("Random News API URL: " + finalAPI);

        try {
            ResponseEntity<String> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, String.class);

            System.out.println("API Response Status Code: " + response.getStatusCode());
            System.out.println("Raw JSON Response: " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            NewsResponse newsResponse = objectMapper.readValue(response.getBody(), NewsResponse.class);

            if (newsResponse.getTotalResults() == 0 || newsResponse.getArticles() == null || newsResponse.getArticles().isEmpty()) {
                System.out.println("No random articles found.");
                return Collections.emptyList();
            }

            List<Article> articles = newsResponse.getArticles();
            Collections.shuffle(articles);
            return articles.stream().limit(10).collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error occurred while fetching random news: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}



