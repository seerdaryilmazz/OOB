package ekol.authorization.service.auth;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.authorization.domain.auth.AuthMenuItem;
import ekol.authorization.domain.auth.BaseEntity;
import ekol.authorization.dto.MenuItemRequest;
import ekol.authorization.repository.auth.AuthMenuItemRepository;
import ekol.model.User;
import ekol.resource.oauth2.SessionOwner;

/**
 * Created by kilimci on 18/04/2017.
 */
@Service
public class AuthMenuItemService {
    @Autowired
    private SessionOwner sessionOwner;

    @Autowired
    private AuthMenuItemRepository authMenuItemRepository;

    @Autowired
    private NodeService nodeService;

    public Collection<AuthMenuItem> getMyMenuItems() {
        User currentUser = sessionOwner.getCurrentUser();
        return getUsersMenuItems(currentUser.getUsername());
    }

    public Collection<AuthMenuItem> getUsersMenuItems(String accountName) {
        return authMenuItemRepository.findMenuItemsByUserName(accountName, LocalDate.now().toEpochDay());
    }

    @Transactional
    public void saveMenuItemRelation(MenuItemRequest menuItemRequest) {
        if(menuItemRequest.getMenuItem() != null){
            authMenuItemRepository.deleteMenuItemRelation(menuItemRequest.getMenuItem().getExternalId());
            AuthMenuItem menuItem = nodeService.loadFromExternalId(menuItemRequest.getMenuItem().getExternalId(), AuthMenuItem.class);
            if(menuItem != null){
                menuItem.getViewers().clear();
            }
        }
        menuItemRequest.getRelations().forEach(menuItemRelation -> {
            BaseEntity from = nodeService.createOrLoadFromNode(menuItemRelation.getFrom());
            BaseEntity to = nodeService.createOrLoadFromNode(menuItemRelation.getTo());
            authMenuItemRepository.mergeMenuItemRelation(from.getId(), to.getId(), menuItemRelation.getLevel());
        });
    }

    @Transactional
    public void deleteMenuItem(Long externalId){
        authMenuItemRepository.deleteMenuItemRelationAndMenuItem(externalId);
    }

}
