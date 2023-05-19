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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "model", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "manufacturer_id" }) })
public class Model {
    public static final String JUST_ID = "id";
    public static final String MANUFACTURER_ID = "manufacturer_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String JUST_MODEL = "model";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "name of model cannot be empty.")
    @Size(min = 1, max = 100, message = "The length of model cannot be less than 2 and more than 100 symbols.")
    private String name;

    @ManyToOne
    @JoinColumn(name = MANUFACTURER_ID, nullable = false)
    private Manufacturer manufacturer;
    
    @OneToMany(mappedBy = JUST_MODEL, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Car> cars;

    @OneToMany(mappedBy = JUST_MODEL, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ModelAndYearLink> modelAndYearLinks;

    @OneToMany(mappedBy = JUST_MODEL, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ModelAndCategoryLink> modelAndCategoryLinks;

    public Model(Long id, String name, Manufacturer manufacturer) {
        super();
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
    }

}
