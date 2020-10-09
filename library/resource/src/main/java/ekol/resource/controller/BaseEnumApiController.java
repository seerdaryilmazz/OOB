package ekol.resource.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

public abstract class BaseEnumApiController<T extends Enum<T>> {

    private Class<T> type;
    private List<T> allValues;

    public void setType(Class<T> type) {
        this.type = type;
        this.allValues = Arrays.asList(this.type.getEnumConstants());
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    public List<T> findAll() {
        return allValues;
    }

    @RequestMapping(value = {"/{name}/", "/{name}"}, method = RequestMethod.GET)
    public T get(@PathVariable String name) {
        return Enum.valueOf(type, name);
    }
}
