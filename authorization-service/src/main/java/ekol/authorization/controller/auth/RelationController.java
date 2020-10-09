package ekol.authorization.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ekol.authorization.dto.InheritRelation;
import ekol.authorization.dto.Link;
import ekol.authorization.dto.MemberOfRelation;
import ekol.authorization.service.auth.AuthOperationService;
import ekol.authorization.service.auth.RelationService;
import ekol.event.auth.Authorize;
import lombok.AllArgsConstructor;

/**
 * Created by ozer on 08/03/2017.
 */
@RestController
@RequestMapping("/relation")
@AllArgsConstructor(onConstructor=@__(@Autowired))
public class RelationController {

    private RelationService relationService;
    private AuthOperationService authOperationService;

    @Authorize(operations = "team.manage")
    @PostMapping("/inherit")
    public Link mergeInheritRelation(@RequestBody InheritRelation inheritRelation) {
        Link result = relationService.mergeInheritRelation(inheritRelation);
        authOperationService.cleanCache();
        return result;
    }

    @Authorize(operations = "team.manage")
    @DeleteMapping("/inherit")
    public void deleteInheritRelation(@RequestParam("fromId") Long fromId, @RequestParam("toId") Long toId) {
    	relationService.deleteInheritRelation(fromId, toId);
        authOperationService.cleanCache();
    }

    @PostMapping("/memberOf")
    public void mergeMemberOfRelation(@RequestBody MemberOfRelation memberOfRelation) {
        relationService.mergeMemberOfRelation(memberOfRelation);
        authOperationService.cleanCache();
    }

    @DeleteMapping("/memberOf")
    public void deleteMemberOfRelation(@RequestParam("fromId") Long fromId, @RequestParam("toId") Long toId) {
    	relationService.deleteMemberOfRelation(fromId, toId);
        authOperationService.cleanCache();
    }
}

