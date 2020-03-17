package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.BaseEntity;
import guru.springframework.sfgpetclinic.services.CrudService;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;
import java.util.Set;

abstract class AbstractSDJpaService<Entity extends BaseEntity, Repository extends CrudRepository<Entity, Long>>
        implements CrudService<Entity, Long> {

    protected final Repository repository;

    protected AbstractSDJpaService(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Entity findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Set<Entity> findAll() {
        Set<Entity> entities = new HashSet<>();
        repository.findAll().forEach(entities::add);
        return entities;
    }

    @Override
    public Entity save(Entity entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(Entity entity) {
        repository.delete(entity);
    }
}
