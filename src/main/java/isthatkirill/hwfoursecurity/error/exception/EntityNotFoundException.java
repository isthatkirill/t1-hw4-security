package isthatkirill.hwfoursecurity.error.exception;

/**
 * @author Kirill Emelyanov
 */

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> entityClass, Long entityId) {
        super("Entity " + entityClass.getSimpleName() + " not found. Id=" + entityId);
    }

    public EntityNotFoundException(Class<?> entityClass, String entityName) {
        super("Entity " + entityClass.getSimpleName() + " not found. " + entityName);
    }

}