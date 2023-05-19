package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Category;

@Repository
@EnableJpaRepositories
public interface CategoryRepository extends CrudRepository<Category, Long> {

    public List<Category> findByName(String categoryName);

    public boolean existsByName(String categoryName);

}
