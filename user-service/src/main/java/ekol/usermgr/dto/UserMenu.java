package ekol.usermgr.dto;

import ekol.usermgr.domain.UIMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by burak on 18/08/16.
 */
public class UserMenu {

    public UserMenu(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static UserMenu fromMenu(UIMenu menu){
        return new UserMenu(menu.getName(), menu.getUrl());
    }

    public String name;
    private String url;

    private List<UserMenu> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<UserMenu> getChildren() {
        return children;
    }

    public void setChildren(List<UserMenu> children) {
        this.children = children;
    }
}
