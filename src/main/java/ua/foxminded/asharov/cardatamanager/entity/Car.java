package ua.foxminded.asharov.cardatamanager.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "car")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    public static final String OBJECT_ID = "object_id";
    public static final String MANUFACTURER_ID = "manufacturer_id";
    public static final String YEAR_ID = "year_id";
    public static final String MODEL_ID = "model_id";
    public static final String CATEGORY_ID = "category_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = OBJECT_ID, nullable = false, unique = true)
    @NotEmpty(message = "objectId of car cannot be empty")
    @Size(min = 10, max = 10, message = "The objectId of name cannot be different than 10 symbols.")
    private String objectId;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = MANUFACTURER_ID, nullable = false)
    private Manufacturer manufacturer;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = YEAR_ID, nullable = false)
    private Year year;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = MODEL_ID, nullable = false)
    private Model model;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = CATEGORY_ID, nullable = false)
    private Category category;

}
