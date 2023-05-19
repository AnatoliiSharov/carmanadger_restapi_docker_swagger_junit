package ua.foxminded.asharov.cardatamanager.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    public static final String JUST_CATEGORY = "category";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "name of category cannot be empty.")
    @Size(min = 2, max = 100, message = "The length of category cannot be less than 2 and more than 100 symbols.")
    private String name;
    
    @OneToMany(mappedBy = JUST_CATEGORY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Car> cars;
    
    @OneToMany(mappedBy = JUST_CATEGORY, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ModelAndCategoryLink> modelAndCategoryLinks;

    public Category(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
       
    public Category(String name) {
        super();
        this.name = name;
    }
    
}
