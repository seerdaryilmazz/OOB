package ekol.usermgr.service;

import ekol.exceptions.ResourceNotFoundException;
import ekol.usermgr.domain.ActiveDirectory;
import ekol.usermgr.dto.ActiveDirectoryUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ActiveDirectoryService {

    @Value("${oneorder.ldap.domain}")
    private String ldapDomain;

    @Value("${oneorder.ldap.url}")
    private String ldapUrl;

    @Value("${oneorder.ldap.username}")
    private String ldapUsername;

    @Value("${oneorder.ldap.password}")
    private String ldapPassword;

    public boolean userExists(String username) {
        return ActiveDirectory.connectWith(ldapUrl, ldapDomain, ldapUsername, ldapPassword).userExists(username);
    }

    public ActiveDirectoryUser userDetails(String username) {
        ActiveDirectory ad = ActiveDirectory.connectWith(ldapUrl, ldapDomain, ldapUsername, ldapPassword);
        if (!ad.userExists(username)) {
            throw new ResourceNotFoundException("username {0} not found", username);
        }
        return ad.userDetails(username);
    }
}
