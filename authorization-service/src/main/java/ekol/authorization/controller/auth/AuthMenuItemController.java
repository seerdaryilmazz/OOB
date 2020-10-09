package ekol.authorization.controller.auth;

import ekol.authorization.domain.auth.AuthMenuItem;
import ekol.authorization.dto.MenuItemRequest;
import ekol.authorization.repository.auth.AuthMenuItemRepository;
import ekol.authorization.service.auth.AuthMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by kilimci on 18/04/2017.
 */
@RestController
@RequestMapping("/auth/menu-item")
public class AuthMenuItemController extends BaseController<AuthMenuItem>{
    @Autowired
    private AuthMenuItemRepository authMenuItemRepository;

    @Autowired
    private AuthMenuItemService authMenuItemService;

    @Override
    protected GraphRepository<AuthMenuItem> getGraphRepository() {
        return authMenuItemRepository;
    }

    @Override
    protected Class getEntityClass() {
        return AuthMenuItem.class;
    }

    @RequestMapping(value = "/menu/{externalId}", method = RequestMethod.GET)
    public AuthMenuItem findMenuItemRelations(@PathVariable Long externalId) {
        List<AuthMenuItem> menuItems = authMenuItemRepository.findMenuItemRelations(externalId);
        return menuItems.isEmpty() ? null : menuItems.get(0);
    }
    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    public void mergeMenuItemRelation(@RequestBody MenuItemRequest menuItemRequest) {
        authMenuItemService.saveMenuItemRelation(menuItemRequest);
    }

    @RequestMapping(value = "/menu/{externalId}", method = RequestMethod.DELETE)
    public void deleteMenuItem(@PathVariable Long externalId) {
        authMenuItemService.deleteMenuItem(externalId);
    }

    @RequestMapping(value = "/user/{username:.+}", method = RequestMethod.GET)
    public Collection<AuthMenuItem> findAuthorizedMenuItemsByUserName(@PathVariable String username) {
        return authMenuItemService.getUsersMenuItems(username);
    }

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public Collection<AuthMenuItem> myMenuItems() {
        return authMenuItemService.getMyMenuItems();
    }


}
