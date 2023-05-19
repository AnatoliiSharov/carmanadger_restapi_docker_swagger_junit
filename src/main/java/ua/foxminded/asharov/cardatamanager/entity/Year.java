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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "year")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Year {

    public static final String JUST_YEAR = "year";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Min(value = 1889, message = "Manufactur years cannot be less than 1889 and more than 2050.")
    @Max(value = 2050, message = "Manufactur years cannot be less than 1889 and more than 2050.")
    private Integer year;

    @OneToMany(mappedBy = JUST_YEAR, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Car> cars;

    @OneToMany(mappedBy = JUST_YEAR, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ModelAndYearLink> modelAndYearLinks;

    public Year(Integer year) {
        super();
        this.year = year;
    }

    public Year(Long id, int year) {
        super();
        this.id = id;
        this.year = year;
    }

}
