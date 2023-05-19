package ua.foxminded.asharov.cardatamanager.service.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractMappingUtil<E,D> {
    
        private final ModelMapper modelMapper;
        
        private final Class<E> entityType;
        private final Class<D> dtoType;
        
        protected AbstractMappingUtil(Class<D> dtoType, Class<E> entityType) {
            super();
            this.modelMapper = new ModelMapper();
            this.dtoType = dtoType;
            this.entityType = entityType;
        }

        public Class<D> getDtoType() {
            return this.dtoType;
        }
        
        public Class<E> getEntityType() {
            return this.entityType;
        }
        
        public D toDto(E entity) {
            return modelMapper.map(entity, getDtoType());
        }

        public List<D> toDtoList(List<E> entities) {
            return entities.stream().map(this::toDto).collect(Collectors.toList());
        }

        public List<D> toDtoList(Iterable<E> iterable){
            return StreamSupport.stream(iterable.spliterator(), false).map(this::toDto).collect(Collectors.toList());
        }

        public E toEntity(D dto) {
            return modelMapper.map(dto, getEntityType());
        }
}
