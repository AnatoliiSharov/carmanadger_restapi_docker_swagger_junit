package ua.foxminded.asharov.cardatamanager.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ua.foxminded.asharov.cardatamanager.entity.Car;
import ua.foxminded.asharov.cardatamanager.exception.ResourceNotFoundException;

@Component
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@Builder
public class CarSpecification implements Specification<Car> {

    private SearchKit searchKit;

    @Override
    public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        switch (searchKit.getOperation()) {
        case EQUALITY:
            return criteriaBuilder.equal(buildExpression(root, searchKit.getKey()), searchKit.getValue());
        case NEGATION:
            return criteriaBuilder.notEqual(buildExpression(root, searchKit.getKey()),
                    searchKit.getValue());
        case GREATER_THAN:
            return criteriaBuilder.greaterThan(buildExpression(root, searchKit.getKey()),
                    Integer.parseInt(searchKit.getValue()));
        case LESS_THAN:
            return criteriaBuilder.lessThan(buildExpression(root, searchKit.getKey()),
                    Integer.parseInt(searchKit.getValue()));
        default:
            throw new ResourceNotFoundException("CarSpecification ununderstood operator", "searchKit =" + searchKit);
        }
    }

    private <T, Y> Path<Y> buildExpression(Root<T> root, String path) {
        String[] split = path.split("\\.", -1);
        Path<Object> objectPath = null;
        for (String part : split) {
            if (objectPath == null) {
                objectPath = root.<Object>get(part);
            } else {
                objectPath = objectPath.get(part);
            }
        }
        return (Path<Y>) objectPath;
    }
}
