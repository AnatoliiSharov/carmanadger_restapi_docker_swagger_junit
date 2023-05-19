package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Category;
import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLink;
import ua.foxminded.asharov.cardatamanager.entity.ModelAndCategoryLinkKey;

@Repository
@EnableJpaRepositories
public interface ModelAndCategoryLinkRepository extends CrudRepository<ModelAndCategoryLink, ModelAndCategoryLinkKey> {

    List<ModelAndCategoryLink> findAllByCategory(Category category);

    List<ModelAndCategoryLink> findByModel(Model model);

}
