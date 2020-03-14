package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.BaseEntity;
import guru.springframework.sfgpetclinic.services.CrudService;

import java.util.*;

abstract class AbstractMapService<T extends BaseEntity> implements CrudService<T, Long> {

    private Map<Long, T> map = new HashMap<>();

    @Override
    public Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    @Override
    public T findById(Long id) {
        return map.get(id);
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }

    @Override
    public void delete(T object) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(object));
    }

    @Override
    public  T save(T object) {
        if (object != null) {
            if (object.getId() == null) {
                object.setId(getNexId());
            }
            map.put(object.getId(), object);
        } else {
            throw new RuntimeException("Object cannot be null");
        }
        return object;
    }

    private Long getNexId() {
        if (map.isEmpty()) {
            return 1L;
        } else {
            return Collections.max(map.keySet()) + 1;
        }
    }
}
