package ekol.authorization.controller.auth;

import java.util.Collection;

import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.*;

import ekol.authorization.domain.auth.BaseEntity;
import ekol.exceptions.*;

/**
 * Created by ozer on 08/03/2017.
 */
public abstract class BaseController<T extends BaseEntity> {

    protected abstract GraphRepository<T> getGraphRepository();

    protected abstract Class<T> getEntityClass();

    @Autowired
    protected Session session;

    protected void assertIdExists(Long id) {
        if (!getGraphRepository().exists(id)) {
            throw new ResourceNotFoundException("Can not find entity", id);
        }
    }

    protected void assertExternalIdNotAlreadyIn(Long id, Long externalId) {
        if (externalId == null) {
            return;
        }
        Collection<T> c = session.loadAll(getEntityClass(), new Filter("externalId", externalId), 1);
        BaseEntity t = c == null || c.isEmpty() ? null : c.iterator().next();

        if (t == null) {
            return;
        }

        if (t.getId().equals(id)) {
            return;
        }

        throw new BadRequestException("Entity already exists");
    }

	@PostMapping({ "", "/"})
    public T add(@RequestBody T t) {
        if (t.getId() != null) {
            throw new BadRequestException("Can not update with this method");
        }

        assertExternalIdNotAlreadyIn(null, t.getExternalId());
        return getGraphRepository().save(t);
    }

	@PutMapping({ "/{id}", "/{id}/"})
    public T update(@RequestBody T t, @PathVariable Long id) {
        if (t.getId() == null) {
            throw new BadRequestException("Can not insert with this method");
        }

        if (!t.getId().equals(id)) {
            throw new BadRequestException("Ids do not match");
        }

        assertIdExists(id);
        assertExternalIdNotAlreadyIn(id, t.getExternalId());
        return getGraphRepository().save(t);
    }

	@DeleteMapping({ "/{id}", "/{id}/"})
    public void delete(@PathVariable Long id) {
        assertIdExists(id);
        getGraphRepository().delete(getGraphRepository().findOne(id));
    }

	@GetMapping({ "", "/" })
    public Iterable<T> findAll() {
        return getGraphRepository().findAll();
    }

	@GetMapping({ "/{id}", "/{id}/" })
    public T find(@PathVariable Long id) {
        assertIdExists(id);
        return getGraphRepository().findOne(id);
    }
}
