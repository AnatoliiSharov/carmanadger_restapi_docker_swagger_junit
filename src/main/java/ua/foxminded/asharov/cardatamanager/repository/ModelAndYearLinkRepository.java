package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLink;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndYearLinkKey;
import ua.foxminded.asharov.cardatamanager.entity.Year;

@Repository
@EnableJpaRepositories
public interface ModelAndYearLinkRepository extends CrudRepository<ModelAndYearLink, ModelAndYearLinkKey> {

    List<ModelAndYearLink> findByModel(Model model);

    List<ModelAndYearLink> findAllByYear(Year year);

}
