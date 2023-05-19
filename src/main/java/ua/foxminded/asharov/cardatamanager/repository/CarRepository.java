package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.entity.Category;
import ua.foxminded.asharov.cardatamanager.entity.Year;

@Repository
@EnableJpaRepositories
public interface CarRepository
        extends PagingAndSortingRepository<Car, Long>, JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {

    List<Car> findAllByYear(Year year);

    List<Car> findAllByCategory(Category category);

    boolean existsByObjectId(String objectIdName);

    List<Car> findByObjectId(String objectId);

    boolean existsByManufacturerIdAndModelIdAndYearIdAndCategoryId(Long manufacturerId, Long modelId, Long yearId,
            Long categoryId);

}
