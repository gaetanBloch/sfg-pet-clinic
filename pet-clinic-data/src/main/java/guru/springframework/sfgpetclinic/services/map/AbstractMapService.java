package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.BaseEntity;
import guru.springframework.sfgpetclinic.services.CrudService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMapService<T extends BaseEntity> implements CrudService<T, Long> {

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
    public T save(T object) {
        return save(object.getId(), object);
    }

    private T save(Long id, T object) {
        map.put(id, object);
        return object;
    }
}
