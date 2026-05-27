package mate.academy.repository.specification;

import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(Collection<String> params);

}
