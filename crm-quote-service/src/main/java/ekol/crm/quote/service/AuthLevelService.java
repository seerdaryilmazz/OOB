package ekol.crm.quote.service;

import ekol.crm.quote.client.AuthorizationService;
import ekol.crm.quote.domain.dto.authorizationservice.User;
import ekol.crm.quote.repository.UserRepository;
import ekol.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class AuthLevelService {



    private AuthorizationService authorizationService;
    private UserRepository userRepository;
    private static final String DEPARTMENT_CODE = "SALES";

    public void updateOrInsertGraphUsersToCrmUsersTable() {
        List<User> authUsers = authorizationService.getUsers(DEPARTMENT_CODE);
        saveCrmUsers(authUsers);
    }

    @Async
    protected void saveCrmUsers(List<User> authUsers){
        try {
            List<ekol.crm.quote.domain.model.User> userList = authUsers.stream().map(User::toEntity).collect(Collectors.toList());

            Map<Long,Boolean> authUserIndexMap = authUsers.stream().collect(Collectors.toMap(User::getId,User::isDeleted, (user1,user2) -> user2));
            userList.forEach(user -> user.setDeleted(authUserIndexMap.get(user.getId())));

            checkIfUsersExistInTable(userList);

            userRepository.save(userList);

        } catch (Exception e) {
            throw new BadRequestException("An error occurred while getting User infos belong to {0}. Exception : {1}", DEPARTMENT_CODE, e);
        }
    }

    private void checkIfUsersExistInTable(List<ekol.crm.quote.domain.model.User> userList) {
        Iterable<ekol.crm.quote.domain.model.User> storedUserList = userRepository.findAll();

        List<ekol.crm.quote.domain.model.User> usersToBeDeleted = StreamSupport.stream(storedUserList.spliterator(),false)
                .filter(user -> !userList.contains(user))
                .collect(Collectors.toList());

        usersToBeDeleted.forEach(user -> user.setDeleted(true));

        userRepository.save(usersToBeDeleted);
    }


}
