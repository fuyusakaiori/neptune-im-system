package com.fuyusakaiori.nep.im.service.core.friendship.controller;

import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepAddFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepDeleteFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.request.group.NepMoveFriendshipGroupMemberRequest;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepAddFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepDeleteFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.entity.response.group.NepMoveFriendshipGroupMemberResponse;
import com.fuyusakaiori.nep.im.service.core.friendship.service.INepFriendshipGroupMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/nep/friendship/group-member")
public class NepFriendshipGroupMemberController {

    @Autowired
    private INepFriendshipGroupMemberService friendshipGroupMemberService;

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/move")
    public NepMoveFriendshipGroupMemberResponse moveFriendshipGroupMember(@RequestBody NepMoveFriendshipGroupMemberRequest request){
        log.info("NepFriendshipGroupMemberController moveFriendshipGroupMember: 开始将好友移动到新的分组中 - request: {}", request);
        NepMoveFriendshipGroupMemberResponse response = friendshipGroupMemberService.moveFriendshipGroupMember(request);
        log.info("NepFriendshipGroupMemberController moveFriendshipGroupMember: 将好友移动到新的分组中结束 - request: {}, response: {}", request, response);
        return response;
    }

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/delete")
    public NepDeleteFriendshipGroupMemberResponse deleteFriendshipGroupMember(@RequestBody NepDeleteFriendshipGroupMemberRequest request){
        log.info("NepFriendshipGroupMemberController deleteFriendshipGroupMember: 开始移除好友所在的分组 - request: {}", request);
        NepDeleteFriendshipGroupMemberResponse response = friendshipGroupMemberService.deleteFriendshipGroupMember(request);
        log.info("NepFriendshipGroupMemberController deleteFriendshipGroupMember: 移除好友所在的分组结束 - request: {}, response: {}", request, response);
        return response;
    }

    /**
     * <h3>完成</h3>
     */
    @PostMapping(value = "/add")
    public NepAddFriendshipGroupMemberResponse addFriendshipGroupMember(@RequestBody NepAddFriendshipGroupMemberRequest request){
        log.info("NepFriendshipGroupMemberController addFriendshipGroupMember: 开始将好友添加到新的好友分组 - request: {}", request);
        NepAddFriendshipGroupMemberResponse response = friendshipGroupMemberService.addFriendshipGroupMember(request);
        log.info("NepFriendshipGroupMemberController addFriendshipGroupMember: 将好友添加到新的好友分组结束 - request: {}, response: {}", request, response);
        return response;
    }

}
