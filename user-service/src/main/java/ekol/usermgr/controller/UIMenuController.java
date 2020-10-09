package ekol.usermgr.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ekol.event.auth.Authorize;
import ekol.exceptions.*;
import ekol.usermgr.domain.*;
import ekol.usermgr.repository.UIMenuRepository;
import ekol.usermgr.service.*;
import lombok.AllArgsConstructor;

/**
 * Created by burak on 15/08/16.
 */
@RestController
@RequestMapping("/uimenu")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UIMenuController {
    private UIMenuRepository uiMenuRepository;
    private UIMenuService uiMenuService;
    private UserService userService;

    private static final String ERROR_MESSAGE_RESOURCENOTFOUND = "UIMenu with id {0} not found ";
    private static final String ERROR_MESSAGE_RESOURCEALREADYEXIST= "Can not create new menu item with a predefined id ";

    @Authorize(operations = "user.menu.manage")
    @GetMapping
    public Iterable<UIMenu> list() {
        return uiMenuRepository.findAllByOrderByRankAsc();
    }

    @Authorize(operations = "user.menu.manage")
    @PostMapping
    public UIMenu save(HttpServletRequest request) throws IOException {
        UIMenu uiMenu = new ObjectMapper().readValue(request.getReader().lines().collect(Collectors.joining(System.lineSeparator())), UIMenu.class);
        if (uiMenu.getId() != null) {
            throw new BadRequestException(MessageFormat.format(ERROR_MESSAGE_RESOURCEALREADYEXIST, uiMenu.getId()));
        }
        return uiMenuService.save(uiMenu);
    }

    @Authorize(operations = "user.menu.manage")
    @PutMapping("/{menuId}/changeRank")
    public void changeRank(@PathVariable Long menuId, @RequestParam(required = false) Long parentId, @RequestParam Integer rank) {
        if (!uiMenuRepository.exists(menuId)) {
            throw new ResourceNotFoundException(ERROR_MESSAGE_RESOURCENOTFOUND, menuId);
        }
        UIMenu menu = uiMenuRepository.findOne(menuId);

        UIMenu parent = null;
        if (parentId != null){
            if(!uiMenuRepository.exists(parentId)){
                throw new BadRequestException(ERROR_MESSAGE_RESOURCENOTFOUND, parentId);
            }
            parent = uiMenuRepository.findOne(parentId);
        }
        uiMenuService.changeRank(menu, parent, rank);
    }

    @Authorize(operations = "user.menu.manage")
    @PutMapping("/{menuId}")
    public UIMenu update(@PathVariable Long menuId, @RequestBody UIMenu uiMenu) {
        if (!uiMenuRepository.exists(menuId)) {
            throw new ResourceNotFoundException(ERROR_MESSAGE_RESOURCENOTFOUND, menuId);
        }
        uiMenu.setId(menuId);
        return uiMenuService.update(uiMenu);
    }

    @Authorize(operations = "user.menu.manage")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        UIMenu uiMenu = uiMenuRepository.findOne(id);
        if (uiMenu == null) {
            throw new ResourceNotFoundException(ERROR_MESSAGE_RESOURCENOTFOUND, id);
        }
        uiMenuService.delete(uiMenu);
    }

    @GetMapping("/usermenu")
    public Iterable<MenuItem> getUserMenu() {
        ekol.model.User sessionOwner = userService.retrieveSessionOwner();
        if(sessionOwner == null) {
            return null;
        }
        return uiMenuService.getUserMenu(sessionOwner.getUsername());
    }
}
