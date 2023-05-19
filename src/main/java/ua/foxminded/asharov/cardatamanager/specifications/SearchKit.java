package ua.foxminded.asharov.cardatamanager.specifications;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchKit {
    private String key;
    private SearchOperation operation;
    private String value;
}
