package com.trip.search.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(indexName= ArticleEs.INDEX_NAME)
public class ArticleEs {
    public static final String INDEX_NAME = "article";

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content_head;

    @Field(type = FieldType.Text)
    private String content_tail;

    @Field(type = FieldType.Integer)
    private Integer day;

    @Field(type = FieldType.Text)
    private List<String> content_body;

    @Field(type = FieldType.Keyword)
    private List<String> route;

    @Field(type = FieldType.Keyword)
    private String cover_url;

}
