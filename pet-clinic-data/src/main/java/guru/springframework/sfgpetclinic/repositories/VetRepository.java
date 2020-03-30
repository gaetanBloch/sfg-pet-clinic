package guru.springframework.sfgpetclinic.repositories;

import guru.springframework.sfgpetclinic.model.Vet;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface VetRepository extends CrudRepository<Vet, Long> {
    @Transactional(readOnly = true)
    @Cacheable("vets")
    Collection<Vet> findAll() throws DataAccessException;
}
