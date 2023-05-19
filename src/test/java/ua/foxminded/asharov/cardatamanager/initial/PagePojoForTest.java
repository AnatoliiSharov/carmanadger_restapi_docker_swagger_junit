package ua.foxminded.asharov.cardatamanager.initial;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PagePojoForTest<CarDto> extends PageImpl<CarDto> {

    private static final long serialVersionUID = 3248189030448292002L;
    
    private List<CarDto> content;
    private int number;
    private int size;
    private int totalPages;
    private int numberOfElements;
    private long totalElements;
    private boolean last;
    private boolean first;
    
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PagePojoForTest(@JsonProperty("content") List<CarDto> content, 
            @JsonProperty("number") int number, 
            @JsonProperty("size") int size,
                    @JsonProperty("totalElements") Long totalElements, 
                    @JsonProperty("pageable") JsonNode pageable, 
                    @JsonProperty("last") boolean last,
                    @JsonProperty("totalPages") int totalPages, 
                    @JsonProperty("sort") JsonNode sort, 
                    @JsonProperty("first") boolean first,
                    @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
        this.content=content;
        this.number=number;
        this.size=size;
        this.last=last;
        this.totalPages=totalPages;
        this.first=first;
        this.numberOfElements=numberOfElements;
        this.totalElements=totalElements;
    }

}
