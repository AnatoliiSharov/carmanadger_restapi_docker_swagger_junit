package ua.foxminded.asharov.cardatamanager.entity;

import static ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLink.CATEGORY_ID;
import static ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLink.MODEL_ID;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Embeddable
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { MODEL_ID, CATEGORY_ID }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelAndCategoryLinkKey implements Serializable {

    @Column(name = MODEL_ID, nullable = false)
    private Long modelId;

    @Column(name = CATEGORY_ID)
    private Long bodyshellId;

}
