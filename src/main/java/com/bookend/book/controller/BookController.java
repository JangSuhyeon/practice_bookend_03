package com.bookend.book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.print.Book;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/book")
public class BookController {

    @Value("${aladin.url}")
    private String ALADIN_URL;

    // 독후감 작성 화면으로
    @GetMapping("/write")
    public String goToBookWrite() {
        return "book/write";
    }

    // 알라딘 Open API를 이용하여 도서 검색
    @PostMapping(value = "/search", produces = "application/json")
    public ResponseEntity<String> searchBooks(@RequestBody HashMap<String, Object> search) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(search.get("searchKeyword"));
        // 도서 검색 결과 요청
        URI aladinUri = UriComponentsBuilder
                .fromUriString(ALADIN_URL)
                .queryParam("Query", search.get("searchKeyword"))
                .queryParam("QueryType", "Title")
                .queryParam("MaxResults", "10")
                .queryParam("start", "1")
                .queryParam("SearchTarget", "Book")
                .queryParam("output", "JS")
                .queryParam("Version", "20131101")
                .build()
                .encode(StandardCharsets.UTF_8).toUri();

        return restTemplate.getForEntity(aladinUri, String.class);
    }

}
