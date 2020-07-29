package com.howie.wen.controller;

import com.howie.wen.async.EventModel;
import com.howie.wen.async.EventProducer;
import com.howie.wen.async.EventType;
import com.howie.wen.model.Comment;
import com.howie.wen.model.EntityType;
import com.howie.wen.model.HostHolder;
import com.howie.wen.service.CommentService;
import com.howie.wen.service.LikeService;
import com.howie.wen.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author:HowieLee
 * @Date:1/10/2019
 * @Description:com.howie.wen.controller
 * @version:1.0
 */
@Controller
public class LikeController {
    @Autowired(required = false)
    @Qualifier("likeService")
    LikeService likeService;
    
    @Autowired(required = false)
    @Qualifier("hostHolder")
    HostHolder hostHolder;

    @Autowired(required = false)
    @Qualifier("commentService")
    CommentService commentService;

    @Autowired(required = false)
    @Qualifier("eventProducer")
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return WendaUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
