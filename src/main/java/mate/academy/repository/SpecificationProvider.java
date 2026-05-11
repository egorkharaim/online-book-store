package mate.academy.repository;

import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(Collection<String> params);

}
