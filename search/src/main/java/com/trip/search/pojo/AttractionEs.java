//package com.trip.search.pojo;
//
//import lombok.Data;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
//
//@Data
//@Document(indexName= AttractionEs.INDEX_NAME)
//public class AttractionEs {
//    public static final String INDEX_NAME = "destination";
//
//    @Id
//    @Field(type = FieldType.Long)
//    private Long id;
//
//    @Field(type = FieldType.Keyword)
//    private String attraction;
//
//    @Field(type = FieldType.Text)
//    private String detail;
//
//}
