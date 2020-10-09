package ekol.usermgr.service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.exceptions.*;
import ekol.resource.oauth2.SessionOwner;
import ekol.usermgr.common.Constants;
import ekol.usermgr.domain.*;
import ekol.usermgr.dto.UserSearchFilter;
import ekol.usermgr.repository.UserRepository;
import ekol.usermgr.serializer.PasswordSerializer;
import ekol.usermgr.validator.UserValidator;

/**
 * Created by fatmaozyildirim on 5/2/16.
 */
@Service
public class UserService {
    private static final String ERROR_MESSAGE= "User with id {0} not found";
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private SessionOwner sessionOwner;

    @Autowired
    private ActiveDirectoryService activeDirectoryService;

    @Value("${oneorder.userImages.directory}")
    private String imageDirectory;

    @Transactional
    @CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
    public User save(User user){
        userValidator.validate(user);
        setPassword(user);

        user.setStatus(UserStatus.ACTIVE);
        if(user.getThumbnail() != null){
            user.setThumbnailPath(saveThumbnail(user.getThumbnail(), user.getUsername()));
        }
        user.setNormalizedName(StringUtils.stripAccents(user.getDisplayName()).toLowerCase());
        return userRepository.save(user);
    }

    private String saveThumbnail(String encodedImage, String username){
        String fileName = username + ".jpeg";
        String path = buildImagePath(fileName);
        byte[] imageBytes = Base64.decodeBase64(encodedImage);
        try {
            FileUtils.writeByteArrayToFile(new File(path), imageBytes);
        } catch (IOException e) {
            throw new ApplicationException("Error saving user thumbnail", e);
        }
        return fileName;
    }

    private String buildImagePath(String imageName){
        StringBuilder sb = new StringBuilder(128);
        sb.append(imageDirectory);
        if(!imageDirectory.endsWith("/")){
            sb.append("/");
        }
        sb.append(imageName);
        return sb.toString();
    }

    public List<User> search(UserSearchFilter userSearchFilter){
        return userRepository.findAll(userSearchFilter.toSpecification()).stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList());
    }

    public void disableUser() {
        UserSearchFilter filter = UserSearchFilter.createWith(null, null, null, false, UserAuthenticationType.ACTIVE_DIRECTORY);

        userRepository.findAll(filter.toSpecification()).forEach(user -> {
            if (!activeDirectoryService.userExists(user.getUsername())) {
                user.setStatus(UserStatus.DISABLED);
                userRepository.save(user);
            }
        });
    }

    @Transactional
    @CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
    public User activate(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("username {0} not found", username);
        }
        user.setStatus(UserStatus.ACTIVE);
        return userRepository.save(user);
    }
    
    @Transactional
    @CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
    public User deactivate(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("username {0} not found", username);
        }
        user.setStatus(UserStatus.DISABLED);
        return userRepository.save(user);
    }

    @Transactional
    @CacheEvict(cacheNames = Constants.CACHE_NAME_USER_MENU, allEntries = true)
    public User updateUserAggregate(String username, User user) {
        if(!userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException(ERROR_MESSAGE, username);
        }
        userValidator.validate(user);
        setPassword(user);
        if(user.getThumbnail() != null){
            user.setThumbnailPath(saveThumbnail(user.getThumbnail(), user.getUsername()));
        }
        user.setNormalizedName(StringUtils.stripAccents(user.getDisplayName()).toLowerCase());
        return userRepository.save(user);
    }

    public ekol.model.User retrieveSessionOwner() {
    	return sessionOwner.getCurrentUser();
    }

    private void setPassword(User user) {
        if (user.getAuthenticationType() == UserAuthenticationType.ACTIVE_DIRECTORY) {
            user.setPassword(null);
        } else if (user.getAuthenticationType() == UserAuthenticationType.PASSWORD) {
            if (user.getPassword().equals(PasswordSerializer.DEFAULT_PASSWORD_DISPLAY)) {
                User existingUser = userRepository.findOne(user.getId());
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            }
        }
    }

    public boolean checkPassword(String accountName, String password) {
        User user = userRepository.findByUsername(accountName);
        return user != null
                && user.getAuthenticationType() == UserAuthenticationType.PASSWORD
                && StringUtils.isNotBlank(user.getPassword())
                && user.getPassword().equals(DigestUtils.md5Hex(password));
    }
}
