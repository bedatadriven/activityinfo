package org.activityinfo.server.entity.change;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.activityinfo.server.database.hibernate.entity.Deleteable;
import org.activityinfo.server.entity.auth.Authorization;
import org.activityinfo.server.entity.auth.AuthorizationHandler;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Authorizes, validates, and effects a ChangeRequest.
 *
 */
public class ChangeHandler {

    private static final Logger LOGGER = Logger.getLogger(ChangeHandler.class.getName());

    private final Provider<EntityManager> entityManager;
    private final Validator validator;
    
    @Inject
    public ChangeHandler(Provider<EntityManager> entityManager, Validator validator) {
        super();
        this.entityManager = entityManager;
        this.validator = validator;
    }

    public void execute(ChangeRequest request) {
        Change change = forRequest(request);
        change.execute();
    }
    
    private Change forRequest(ChangeRequest request) {
        for(EntityType<?> type : entityManager.get().getMetamodel().getEntities()) {
            if(type.getName().equalsIgnoreCase(request.getEntityType())) {
                return new Change(type, request);
            }
        }
        throw new ChangeException(ChangeFailureType.UNKNOWN_ENTITY_TYPE);
     }
 
    private class Change<T> {
        
        private final EntityType<T> entityType;
        private final ChangeRequest request;
        
        public Change(EntityType<T> entityType, ChangeRequest request) {
            this.entityType = entityType;
            this.request = request;
        }

        public void execute() {
            switch(request.getChangeType()) {
            case CREATE:
                create(request);
                break;
            case UPDATE:
                update(request);
                break;
            case DELETE:
                delete(request);
                break;
            }
        }
        
        private void create(ChangeRequest request) {
            throw new ChangeException(ChangeFailureType.SERVER_FAULT);
        }

        private void delete(ChangeRequest request) {
            T entity = locate();
            authorize(entity);
            
            if(entity instanceof Deleteable) {
                ((Deleteable) entity).delete();
            }
        }
        
        public void update(ChangeRequest request) {
            T entity = locate();
            applyChanges(entity);
        }
        
        /**
         * Applies the list of updated properties to the entity
         */
        private void applyChanges(T entity) {
            for(String propertyName : request.getUpdatedProperties()) {
                
                // Get the metamodel for this entity's property
                Attribute<? super T, ?> attribute = attributeForPropertyName(propertyName);
                
                // Verify that this property may be updated by the user
                verifyUpdateToPropertyIsAllowed(attribute, propertyName);
         
                // Go ahead and update the value
                updateProperty(entity, attribute, propertyName);
            }
        }

        private void verifyUpdateToPropertyIsAllowed(Attribute<? super T, ?> attribute, String propertyName) {
            AnnotatedElement member = (AnnotatedElement) attribute.getJavaMember();
            AllowUserUpdate allowed = member.getAnnotation(AllowUserUpdate.class);
            if(allowed == null) {
                throw new ChangeException(ChangeFailureType.PROPERTY_NOT_UPDATABLE, propertyName);
            }
        }

        private void updateProperty(T entity, Attribute<? super T, ?> attribute, String propertyName) {
            if(attribute.getPersistentAttributeType() == PersistentAttributeType.BASIC) {
                
                updateBasicProperty(entity, attribute, propertyName);
            }
        }

        private void updateBasicProperty(T entity, Attribute<? super T, ?> attribute, String propertyName) {

            Object newValue = request.getPropertyValue(attribute.getJavaType(), propertyName);
           
            Set<ConstraintViolation<T>> violations = validator.validateValue(entityType.getJavaType(), attribute.getName(), newValue);
            if(!violations.isEmpty()) {
                throw new ChangeException(propertyName, violations);
            }
            
            Method setter = setterForAttribute(attribute);
            try {
                setter.invoke(entity, newValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new ChangeException(e);
            }
        }

        private Method setterForAttribute(Attribute<? super T, ?> attribute) {
            String setterName = "set" + attribute.getName().substring(0, 1).toUpperCase() + attribute.getName().substring(1);
            try {
                return entityType.getJavaType().getMethod(setterName, attribute.getJavaType());
            } catch (NoSuchMethodException e) {
                throw new ChangeException(ChangeFailureType.SERVER_FAULT, e);
            } catch (SecurityException e) {
                throw new ChangeException(ChangeFailureType.SERVER_FAULT, e);
            }
        }

        private Attribute<? super T, ?> attributeForPropertyName(String propertyName) {
            try {
                return entityType.getAttribute(propertyName);
            } catch(IllegalArgumentException e) {
                throw new ChangeException(ChangeFailureType.PROPERTY_DOES_NOT_EXIST, propertyName);
            }
        }


        /**
         * Locates the entity to be modified or deleted based on the 
         * properties of the change request.
         * 
         * @param request the change request
         * @return the entity
         * @throws ChangeException if the entity does not exist
         */
        public T locate() {
            T entity = entityManager.get().find(entityType.getJavaType(), getEntityId());
            if(entity == null) {
                throw new ChangeException(ChangeFailureType.ENTITY_DOES_NOT_EXIST);
            }
            return entity;
        }
        
        /**
         * Retrieves the id of the entity to create or modify from the
         * change request.
         * 
         * @param request
         * @return
         */
        public Object getEntityId() {
            String id = request.getEntityId();
            Class<?> type = entityType.getIdType().getJavaType();
            if(type.equals(Integer.class) || type.equals(int.class)) {
                return Integer.parseInt(id);
            } else if(type.equals(Long.class) || type.equals(long.class)) {
                return Long.parseLong(id);
            } else if(type.equals(String.class)) {
                return id;
            } else {
                throw new ChangeException(ChangeFailureType.MALFORMED_ID);
            }
        }
        
        
        /**
         * Verifies that the user is authorized to make the change. Subclass <strong>must</strong>
         * override, the default implementation always throws
         * @param user the requesting user
         * @param entity the entity to be created or modified
         * @param request the change request
         * @throws ChangeException if the user is not authorized
         */
        public void authorize(T entity) {
            
            Authorization authorization = findAuthorizationAnnotation();
            
            boolean authorized;
            try {
                AuthorizationHandler<T> authorizationHandler = authorization.handler().newInstance();
                authorized = authorizationHandler.isAuthorized(request.getRequestingUser(), entity);
            } catch(Exception e) {
                LOGGER.log(Level.SEVERE, "Exception thrown by authorization handler", e);
                throw new ChangeException(ChangeFailureType.SERVER_FAULT, e);            
            }
            
            if(!authorized) {
                throw new ChangeException(ChangeFailureType.NOT_AUTHORIZED);            
            }
        }

        private Authorization findAuthorizationAnnotation() {
            Authorization authorization = findAuthorizationAnnotation(entityType.getJavaType());
            if(authorization == null) {
                LOGGER.severe("Entity class " + entityType.getName() + " does not have an @Authorization annotation,"
                    + " denying all change requests.");
                throw new ChangeException(ChangeFailureType.NOT_AUTHORIZED);            
            }
            return authorization;
        }
        
        private Authorization findAuthorizationAnnotation(Class<?> type) {
            Authorization authorization = type.getAnnotation(Authorization.class);
            if(authorization != null) {
                return authorization;
            }
            if(type.getSuperclass() != null) {
                authorization = findAuthorizationAnnotation(type.getSuperclass());
                if(authorization != null) {
                    return authorization;
                }
            }
            for(Class<?> interfaceClass : type.getInterfaces()) {
                authorization = findAuthorizationAnnotation(interfaceClass);
                if(authorization != null) {
                    return authorization;
                }
            }
            
            return authorization;
        }
    }
}
