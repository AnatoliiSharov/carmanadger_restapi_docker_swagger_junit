package ua.foxminded.asharov.cardatamanager.specifications;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.foxminded.asharov.cardatamanager.dto.SearchObject;
import ua.foxminded.asharov.cardatamanager.entity.Car;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CarSpecificationBuilder {

    private SearchObject searchObject;

    public Specification<Car> toSpecification(SearchObject searchObject) {
        List<Specification<Car>> specifications = new ArrayList<>();

        if (searchObject.getCategory() != null) {
            specifications.add(buildSpecFragment(fillCarSpecification(searchObject.getCategory(),
                    SearchGoal.CATEGORY.searchGoal, SearchOperation.EQUALITY)));
        }

        if (searchObject.getManufacturer() != null) {
            specifications.add(buildSpecFragment(fillCarSpecification(searchObject.getManufacturer(),
                    SearchGoal.MANUFACTURER.searchGoal, SearchOperation.EQUALITY)));
        }

        if (searchObject.getModel() != null) {
            specifications.add(buildSpecFragment(fillCarSpecification(searchObject.getModel(),
                    SearchGoal.MODEL.searchGoal, SearchOperation.EQUALITY)));
        }

        if (searchObject.getMaxYear() != null) {
            specifications.add(buildSpecFragment(fillCarSpecification(new String[] { searchObject.getMaxYear() },
                    SearchGoal.YEAR.searchGoal, SearchOperation.LESS_THAN)));
        }

        if (searchObject.getMinYear() != null) {
            specifications.add(buildSpecFragment(fillCarSpecification(new String[] { searchObject.getMinYear() },
                    SearchGoal.YEAR.searchGoal, SearchOperation.GREATER_THAN)));
        }
        return buildSpec(specifications);
    }

    private Specification<Car> buildSpec(List<Specification<Car>> specifications) {
        Specification<Car> result = specifications.get(0);

        for (int i = 0; i < specifications.size(); i++) {
            result = Specification.where(result).and(specifications.get(i));
        }
        return result;
    }

    private Specification<Car> buildSpecFragment(List<CarSpecification> specifications) {
        Specification<Car> result = specifications.get(0);

        for (int i = 0; i < specifications.size(); i++) {
            result = Specification.where(result).or(specifications.get(i));
        }
        return result;
    }

    private List<CarSpecification> fillCarSpecification(String[] searchable, String searchGoal,
            SearchOperation searchOperator) {
        List<CarSpecification> specifications = new ArrayList<>();

        Stream.of(searchable).forEach(each -> specifications.add(new CarSpecification(
                SearchKit.builder().key(searchGoal).operation(searchOperator).value(each).build())));
        return specifications;
    }

}
