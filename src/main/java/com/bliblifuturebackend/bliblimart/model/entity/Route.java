package com.bliblifuturebackend.bliblimart.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "route")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route extends BaseEntity{

    @Field(name = "route")
    private List<String> route;

    @Field(name = "starting_mark")
    private String startingmark;

    @Field(name = "target_mark")
    private String targetMark;

}
