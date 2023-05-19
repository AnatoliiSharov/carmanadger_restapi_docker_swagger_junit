package ua.foxminded.asharov.cardatamanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.foxminded.asharov.cardatamanager.entity.Year;

@Repository
@EnableJpaRepositories
public interface YearRepository extends CrudRepository<Year, Long> {

    public List<Year> findByYear(Integer integer);

    public Year findFirstByOrderByYearDesc();

    public Year findFirstByOrderByYearAsc();

    public boolean existsByYear(int yearValue);

}
