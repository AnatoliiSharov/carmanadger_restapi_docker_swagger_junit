package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Manufacturer;

@Repository
@EnableJpaRepositories
public interface ManufacturerRepository extends CrudRepository<Manufacturer, Long> {

    List<Manufacturer> findByName(String manufacturerName);
    
    boolean existsByName(String manufacturerName);

}
