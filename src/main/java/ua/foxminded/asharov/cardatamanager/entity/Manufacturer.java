package ua.foxminded.asharov.cardatamanager.entity;

import java.util.List;

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
@Table(name = "manufacturer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Manufacturer {
    public static final String JUST_MANUFACTURER = "manufacturer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "name of maker cannot be empty")
    @Size(min = 2, max = 100, message = "The length of name cannot be less than 2 and more than 100 symbols.")
    private String name;

    @OneToMany(mappedBy = JUST_MANUFACTURER, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Model> models;

    @OneToMany(mappedBy = JUST_MANUFACTURER, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Car> cars;

    public Manufacturer(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Manufacturer(String name) {
        this.id = null;
        this.name = name;
    }

}
