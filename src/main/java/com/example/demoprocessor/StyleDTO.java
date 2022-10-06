
package com.example.demoprocessor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StyleDTO {
    private String brand;
    private String market;
    private String channel;
    private String styleId;
    private String description;

    public StyleDTO(String brand, String market, String channel, String styleId, String description) {
        this.brand = brand;
        this.market = market;
        this.channel = channel;
        this.styleId = styleId;
        this.description = description;
    }
    public static StyleDTO fromJSON(String json){
        Gson g=new Gson();
        return g.fromJson(json,StyleDTO.class);
    }
    public String toJSON(){
        Gson g=new Gson();
        return g.toJson(this);
    }
}
