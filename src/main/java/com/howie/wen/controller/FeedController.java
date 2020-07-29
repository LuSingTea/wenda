package com.howie.wen.controller;

import com.howie.wen.model.EntityType;
import com.howie.wen.model.Feed;
import com.howie.wen.model.HostHolder;
import com.howie.wen.service.FeedService;
import com.howie.wen.service.FollowService;
import com.howie.wen.util.JedisAdapter;
import com.howie.wen.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;


@Controller
public class FeedController {
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);
    
    @Autowired(required = false)
    @Qualifier("feedService")
    FeedService feedService;

    @Autowired(required = false)
    @Qualifier("followService")
    FollowService followService;

    @Autowired(required = false)
    @Qualifier("hostHolder")
    HostHolder hostHolder;

    @Autowired(required = false)
    @Qualifier("jedisAdapter")
    JedisAdapter jedisAdapter;

    /**
     * @return
     * @Author HowieLee
     * @Description //TODO 推拉的feed流
     * @Date 20:53 1/14/2019
     * @Param
     **/

    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPushFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
        List<Feed> feeds = new ArrayList<Feed>();
        for (String feedId : feedIds) {
            Feed feed = feedService.getById(Integer.parseInt(feedId));
            if (feed != null) {
                feeds.add(feed);
            }
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
    private String getPullFeeds(Model model) {
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0) {
            // 关注的人
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
