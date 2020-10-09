package ekol.usermgr.domain;

import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.util.StringUtils;

import ekol.exceptions.ApplicationException;
import ekol.usermgr.dto.ActiveDirectoryUser;
import ekol.usermgr.service.BlindSSLSocketFactory;

/**
 * Created by kilimci on 07/04/16.
 */

public class ActiveDirectory {

    private static String searchFilter= "(&(objectClass=user)(userPrincipalName={0}))";

    private String url;
    private String domain;
    private String ldapUsername;
    private String ldapPassword;
    private String rootDn;
    private DirContext ctx;

    private ActiveDirectory(String url, String domain, String ldapUsername, String ldapPassword){
        this.url = url;
        this.domain = domain;
        this.ldapPassword = ldapPassword;
        this.ldapUsername = ldapUsername;
        this.rootDn = rootDnFromDomain(domain);
    }

    public static ActiveDirectory connectWith(String url, String domain, String ldapUsername, String ldapPassword) {
        ActiveDirectory ad = new ActiveDirectory(url, domain, ldapUsername, ldapPassword);
        ad.bind();
        return ad;
    }

    public boolean userExists(String username){
        return searchUser(username) != null;
    }

    public ActiveDirectoryUser userDetails(String username){
        DirContextOperations userOperations = searchUser(username);
        if(userOperations == null){
            return null;
        }
        return ActiveDirectoryUser.createWith(userOperations);
    }

    private DirContextOperations searchUser(String username){
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
        	DirContextOperations userOperations = SpringSecurityLdapTemplate.searchForSingleEntryInternal(ctx,
                    searchControls, rootDn, searchFilter,
                    new Object[] { createBindPrincipal(username) });
        	if(null != userOperations) {
        		Enumeration<String> names = userOperations.getDn().getAll();
                while (names.hasMoreElements()) {
        			if(names.nextElement().endsWith("Ayrılan Kullanıcılar"))
        				return null;
        		}
        	}
        	return userOperations;
        } catch (IncorrectResultSizeDataAccessException incorrectResults) {
            if (incorrectResults.getActualSize() != 0) {
                throw incorrectResults;
            }
            return null;
        } catch (NamingException e){
            throw new ApplicationException("Error searching username",e);
		}
    }


    private void bind() {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, createBindPrincipal(ldapUsername));
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.OBJECT_FACTORIES, DefaultDirObjectFactory.class.getName());
        if(url.startsWith("ldaps://")) {
        	env.put(Context.SECURITY_PROTOCOL, "ssl");
        	env.put("java.naming.ldap.factory.socket", BlindSSLSocketFactory.class.getName());
        }
        try {
            ctx = new InitialLdapContext(env, null);
        }
        catch (NamingException e) {
            throw new ApplicationException("Error connecting LDAP", e);
        }
    }

    private String createBindPrincipal(String username) {
        return username.toLowerCase().concat("@").concat(domain);
    }
    private static String rootDnFromDomain(String domain) {
        String[] tokens = StringUtils.tokenizeToStringArray(domain, ".");
        StringBuilder root = new StringBuilder();

        for (String token : tokens) {
            if (root.length() > 0) {
                root.append(',');
            }
            root.append("dc=").append(token);
        }

        return root.toString();
    }
}
