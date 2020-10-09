package ekol.mongodb.domain.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ekol.exceptions.ResourceNotFoundException;
import ekol.mongodb.domain.entity.BaseEntity;
import ekol.mongodb.domain.repository.ApplicationMongoRepository;

public abstract class BaseController<T extends BaseEntity> {

    protected abstract ApplicationMongoRepository<T> getApplicationRepository();

    protected void assertEntityExists(String id) {
        if (!getApplicationRepository().exists(id)) {
            throw new ResourceNotFoundException("Entity with id {0} not found", id);
        }
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
    public T add(@RequestBody T entity) {
        return getApplicationRepository().save(entity);
    }

    @RequestMapping(value = {"/{id}/", "/{id}"}, method = RequestMethod.PUT)
    public T update(@PathVariable String id, @RequestBody T entity) {
        assertEntityExists(id);
        return getApplicationRepository().save(entity);
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public Iterable<T> findAll() {
        return getApplicationRepository().findAll();
    }

    @RequestMapping(value = {"/{id}/", "/{id}"}, method = RequestMethod.GET)
    public T get(@PathVariable String id) {
        assertEntityExists(id);
        return getApplicationRepository().findOne(id);
    }

    @RequestMapping(value = {"/{id}/", "/{id}"}, method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        assertEntityExists(id);
        T entity = getApplicationRepository().findOne(id);
        entity.setDeleted(true);
        getApplicationRepository().save(entity);
    }
}
