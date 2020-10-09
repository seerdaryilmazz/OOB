package ekol.authorization.service.userdetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ekol.authorization.domain.auth.*;
import ekol.authorization.domain.auth.MemberOfRelation;
import ekol.authorization.dto.*;
import ekol.authorization.repository.auth.*;
import ekol.authorization.service.auth.*;
import ekol.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;

/**
 * Created by burak on 05/07/17.
 */
@Service
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class UserDetailsService {

    private AuthSubsidiaryRepository authSubsidiaryRepository;
    private AuthLocationRepository authLocationRepository;
    private NodeRepository nodeRepository;
    private NodeService nodeService;
    private AuthUserRepository authUserRepository;
    private AuthOperationService authOperationService;

    public Set<IdNamePair> retrieveUserSubsidiaries(String username) {
        Collection<AuthSubsidiary> subsidiaries = authSubsidiaryRepository.retrieveUserParentSubsidiaries(username, LocalDate.now().toEpochDay());
        Set<IdNamePair> result = new HashSet<>();
        for (AuthSubsidiary s : subsidiaries) {
            // Graph DB'deki externalId alanına, Relational DB'deki id değerini verdiğimiz için aşağıda subsidiaryId olarak externalId'yi veriyoruz.
            result.add(new IdNamePair(s.getExternalId(), s.getName()));
        }
        return result;
    }
    

    public Set<IdNamePair> retrieveUserLocations(String username) {
        Collection<AuthLocation> Locations = authLocationRepository.retrieveUserLocations(username, LocalDate.now().toEpochDay());

        return Locations.stream().filter(s -> s.getExternalId() != null).map(s -> {
            IdNamePair obj = new IdNamePair();
            obj.setId(s.getExternalId());
            obj.setName(s.getName());
            return obj;
        }).collect(Collectors.toSet());
    }

    //find by user name and team
    public Iterable<AuthUser> retrieveTeammates(String username) {
    	Iterable <AuthUser> users = authUserRepository.retrieveByUsernameAndTeam(username);
    	users.forEach(user->user.getMemberships().forEach(MemberOfRelation::setStartAndEndDates));
        return users;
    }
    
    public AuthUser findUser(String username) {
        AuthUser user = authUserRepository.findByUsername(username);
        if(user.getMemberships() != null){
            user.getMemberships().forEach(MemberOfRelation::setStartAndEndDates);
        }
        return  user;
    }

    public Iterable<BaseEntity> findUserWithActiveMemberships(String username) {
    	AuthUser user = authUserRepository.findActiveMembershipsByUsername(username, LocalDate.now().toEpochDay());
    	Iterable<BaseEntity> nodeList = nodeRepository.findActiveMembershipsByUsername(username, LocalDate.now().toEpochDay());
    	for (Iterator<BaseEntity> iterator = nodeList.iterator(); iterator.hasNext();) {
    		BaseEntity entity = iterator.next();
    		if(AuthTeam.class.isInstance(entity) && user.getMemberships().parallelStream().map(MemberOfRelation::getEntity).map(BaseEntity::getId).noneMatch(entity.getId()::equals)) {
    			iterator.remove();
    		}
		}
    	return nodeList;
    }

    @Transactional
    public AuthUser saveUserMemberships(String username, Set<MemberOfRelation> memberships){
        AuthUser user = authUserRepository.findByUsername(username);
        if(user == null){
            throw new ResourceNotFoundException("user with username {0} not found", username);
        }
        authUserRepository.deleteMembershipsByUsername(username);
        user.setMemberships(new HashSet<>());
        memberships.forEach(membership -> {
            BaseEntity entity = null;
            if(membership.getEntity().getId() != null){
                entity = nodeRepository.findByIdAndName(membership.getEntity().getId(), membership.getEntity().getName());
            }else if(membership.getEntity().getExternalId() != null){
                entity = nodeRepository.findByExternalIdAndName(membership.getEntity().getExternalId(), membership.getEntity().getName());
                if(entity == null){
                    Node node = new Node();
                    node.setName(membership.getEntity().getName());
                    node.setExternalId(membership.getEntity().getExternalId());
                    node.setType(membership.getEntity().getType());
                    entity = nodeService.createOrLoadFromNode(node);
                }
            }

            if(entity != null){
                MemberOfRelation memberOf = new MemberOfRelation();
                memberOf.setUser(user);
                memberOf.setEntity(entity);
                memberOf.setLevel(membership.getLevel());
                if(membership.getStartDate() != null){
                    memberOf.setStartDateDays(membership.getStartDate().toEpochDay());
                }
                if(membership.getEndDate() != null){
                    memberOf.setEndDateDays(membership.getEndDate().toEpochDay());
                }
                user.getMemberships().add(memberOf);
            }
        });

        AuthUser result = authUserRepository.save(user);
        authOperationService.cleanCache();
        return result;
    }



    public AuthUser saveUser(String username, AuthUser userData){
        AuthUser user = authUserRepository.findByUsername(username);
        if(user == null){
            user = new AuthUser();
        }
        user.setExternalId(userData.getExternalId());
        user.setName(userData.getName());
        return authUserRepository.save(user);

    }

}
