package ekol.hibernate5.domain.listener;

import ekol.hibernate5.domain.entity.RevisionInfo;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

/**
 * Created by kilimci on 29/04/16.
 */
public class RevisionInfoListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "not_authenticated";
        if (authentication != null && authentication.isAuthenticated()) {
            username = (String)authentication.getPrincipal();
        }
        RevisionInfo revisionInfo = (RevisionInfo) revisionEntity;
        revisionInfo.setUpdatedTime(new Date());
        revisionInfo.setUpdatedBy(username);
    }
}
