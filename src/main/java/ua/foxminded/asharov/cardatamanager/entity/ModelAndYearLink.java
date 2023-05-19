package ua.foxminded.asharov.cardatamanager.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "model_year")
@Data
@NoArgsConstructor
public class ModelAndYearLink {
    public static final String JUST_ID = "id";
    public static final String MODEL_ID = "model_id";
    public static final String YEAR_ID = "year_id";

    @EmbeddedId
    private ModelAndYearLinkKey id;

    @ManyToOne
    @MapsId("modelId")
    @JoinColumn(name = MODEL_ID, nullable = false)
    private Model model;

    @ManyToOne
    @MapsId("yearId")
    @JoinColumn(name = YEAR_ID, nullable = false)
    private Year year;

    public ModelAndYearLink(Model model, Year year) {
        super();
        this.id = new ModelAndYearLinkKey(model.getId(), year.getId());
        this.model = model;
        this.year = year;
    }
    
}
