package ekol.authorization.domain.dto;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ekol.authorization.domain.auth.*;
import ekol.model.IdNamePair;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMemberships {

    private List<Membership> locations = new ArrayList<>();
    private List<Membership> subsidiaries = new ArrayList<>();
    private List<Membership> departments = new ArrayList<>();
    private List<Membership> teams = new ArrayList<>();

    public static UserMemberships createWith(AuthUser user){
        UserMemberships userMemberships = new UserMemberships();
        if(user.getMemberships() == null){
            return userMemberships;
        }

        userMemberships.setLocations(
                user.getMemberships().stream().filter(memberOfRelation -> memberOfRelation.getEntity().getType().equals("Location"))
                        .map(Membership::createWith).collect(Collectors.toList()));

        userMemberships.setSubsidiaries(
                user.getMemberships().stream().filter(memberOfRelation -> memberOfRelation.getEntity().getType().equals("Subsidiary"))
                        .map(Membership::createWith).collect(Collectors.toList()));

        userMemberships.setDepartments(
                user.getMemberships().stream().filter(memberOfRelation -> memberOfRelation.getEntity().getType().equals("Department"))
                        .map(Membership::createWith).collect(Collectors.toList()));

        userMemberships.setTeams(
                user.getMemberships().stream().filter(memberOfRelation -> memberOfRelation.getEntity().getType().equals("Team"))
                        .map(Membership::createWith).collect(Collectors.toList()));

        return userMemberships;
    }
    
    public static UserMemberships createWith(Iterable<BaseEntity> nodes){
        UserMemberships userMemberships = new UserMemberships();
        
        Supplier<Stream<BaseEntity>> nodesStream = () -> StreamSupport.stream(nodes.spliterator(), true);
        if(!nodesStream.get().findAny().isPresent()){
            return userMemberships;
        }

        userMemberships.setLocations(nodesStream.get().filter(node->node instanceof AuthLocation).map(Membership::createWith).collect(Collectors.toList()));
        userMemberships.setSubsidiaries(nodesStream.get().filter(node->node instanceof AuthSubsidiary).map(Membership::createWith).collect(Collectors.toList()));
        userMemberships.setDepartments(nodesStream.get().filter(node->node instanceof AuthDepartment).map(Membership::createWith).collect(Collectors.toList()));
        userMemberships.setTeams(nodesStream.get().filter(node->node instanceof AuthTeam).map(Membership::createWith).collect(Collectors.toList()));
        return userMemberships;
    }

    public List<Membership> getLocations() {
        return locations;
    }

    public void setLocations(List<Membership> locations) {
        this.locations = locations;
    }

    public List<Membership> getSubsidiaries() {
        return subsidiaries;
    }

    public void setSubsidiaries(List<Membership> subsidiaries) {
        this.subsidiaries = subsidiaries;
    }

    public List<Membership> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Membership> departments) {
        this.departments = departments;
    }

    public List<Membership> getTeams() {
        return teams;
    }

    public void setTeams(List<Membership> teams) {
        this.teams = teams;
    }

    public static class Membership{
        private LocalDate startDate;
        private LocalDate endDate;
        private IdNamePair memberOf;

        public static Membership createWith(MemberOfRelation relation){
            Membership membership = new Membership();
            membership.setStartDate(relation.getStartDate());
            membership.setEndDate(relation.getEndDate());
            membership.setMemberOf(new IdNamePair(relation.getEntity().getExternalId(), relation.getEntity().getName()));
            return membership;
        }
        
        public static Membership createWith(BaseEntity entity){
            Membership membership = new Membership();
            membership.setMemberOf(new IdNamePair(entity.getExternalId(), entity.getName()));
            return membership;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public IdNamePair getMemberOf() {
            return memberOf;
        }

        public void setMemberOf(IdNamePair memberOf) {
            this.memberOf = memberOf;
        }
    }
}
