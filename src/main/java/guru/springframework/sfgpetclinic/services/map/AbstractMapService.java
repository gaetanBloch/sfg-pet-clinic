package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.BaseEntity;
import guru.springframework.sfgpetclinic.services.CrudService;

import java.util.*;

abstract class AbstractMapService<Entity extends BaseEntity> implements CrudService<Entity, Long> {

    private Map<Long, Entity> map = new HashMap<>();

    @Override
    public Set<Entity> findAll() {
        return new HashSet<>(map.values());
    }

    @Override
    public Entity findById(Long id) {
        return map.get(id);
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }

    @Override
    public void delete(Entity entity) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(entity));
    }

    @Override
    public Entity save(Entity entity) {

        if (entity == null) {
            throw new NullPointerException("Entity cannot be null");
        }

        if (entity.getId() == null) {
            entity.setId(getNexId());
        }
        map.put(entity.getId(), entity);

        return entity;
    }

    private Long getNexId() {
        if (map.isEmpty()) {
            return 1L;
        } else {
            return Collections.max(map.keySet()) + 1;
        }
    }
}
