package com.bliblifuturebackend.bliblimart.model.entity;

import com.bliblifuturebackend.bliblimart.model.response.MarkResponse;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = false)
@Document(collection = "mark")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mark extends BaseEntity{

    @Field(name = "name")
    private String name;

    @Field(name = "path")
    private String path;

    @Field(name = "x_coordinate")
    private int x;

    @Field(name = "y_coordinate")
    private int y;

    public MarkResponse createResponse(){
        return createResponse(this, new MarkResponse());
    }
}
