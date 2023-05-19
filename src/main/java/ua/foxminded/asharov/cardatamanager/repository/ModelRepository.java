package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Model;
import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;

@Repository
@EnableJpaRepositories
public interface ModelRepository extends CrudRepository<Model, Long>   {

    List<Model> findByManufacturer(Manufacturer manufacturer);

    List<Model> findByNameAndManufacturer(String modelName, Manufacturer manufacturer);

    boolean existsByName(String name);

    boolean existsByNameAndManufacturer(String name, Manufacturer manufacturer);

}
