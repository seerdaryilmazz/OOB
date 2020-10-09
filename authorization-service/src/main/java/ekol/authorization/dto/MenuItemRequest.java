package ekol.authorization.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ekol.authorization.domain.auth.AuthMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kilimci on 19/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuItemRequest {
    AuthMenuItem menuItem = new AuthMenuItem();
    List<MenuItemRelation> relations = new ArrayList<>();

    public AuthMenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(AuthMenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public List<MenuItemRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<MenuItemRelation> relations) {
        this.relations = relations;
    }
}
