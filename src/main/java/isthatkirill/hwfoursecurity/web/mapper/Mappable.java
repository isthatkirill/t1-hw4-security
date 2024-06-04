package isthatkirill.hwfoursecurity.web.mapper;

import org.mapstruct.Mapper;

/**
 * @author Kirill Emelyanov
 */

@Mapper(componentModel = "spring")
public interface Mappable<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

}
