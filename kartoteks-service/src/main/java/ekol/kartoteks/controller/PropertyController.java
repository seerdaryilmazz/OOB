package ekol.kartoteks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/property/{key:.+}" ,method = RequestMethod.GET)
    public String getValue(@PathVariable String key) {
        return env.getProperty(key);
    }
}
