package isthatkirill.hwfoursecurity.web.mapper;

import java.util.List;

/**
 * @author Kirill Emelyanov
 */

public interface Mappable<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

    List<D> toDto(List<E> entities);

}
