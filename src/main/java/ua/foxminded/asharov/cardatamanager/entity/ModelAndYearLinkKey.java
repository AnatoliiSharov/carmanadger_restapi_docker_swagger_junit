package ua.foxminded.asharov.cardatamanager.entity;

import static ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLink.MODEL_ID;
import static ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLink.YEAR_ID;

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
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { MODEL_ID, YEAR_ID }) })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelAndYearLinkKey implements Serializable {

    @Column(name = MODEL_ID, nullable = false)
    private Long modelId;

    @Column(name = YEAR_ID, nullable = false)
    private Long yearId;

}
