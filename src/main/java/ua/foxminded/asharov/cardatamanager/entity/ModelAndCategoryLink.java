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
@Table(name = "model_category")
@Data
@NoArgsConstructor
public class ModelAndCategoryLink {
    public static final String JUST_ID = "id";
    public static final String MODEL_ID = "model_id";
    public static final String CATEGORY_ID = "category_id";

    @EmbeddedId
    private ModelAndCategoryLinkKey id;

    @ManyToOne
    @MapsId("modelId")
    @JoinColumn(name = MODEL_ID, nullable = false)
    private Model model;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = CATEGORY_ID, nullable = false)
    private Category category;

    public ModelAndCategoryLink(Model model, Category category) {
        super();
        this.id = new ModelAndCategoryLinkKey(model.getId(), category.getId());
        this.model = model;
        this.category = category;
    }

}
