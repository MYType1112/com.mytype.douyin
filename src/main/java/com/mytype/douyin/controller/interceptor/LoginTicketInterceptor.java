package com.mytype.douyin.controller.interceptor;

import com.mytype.douyin.entity.LoginTicket;
import com.mytype.douyin.entity.User;
import com.mytype.douyin.service.FollowService;
import com.mytype.douyin.service.LikeService;
import com.mytype.douyin.service.UserService;
import com.mytype.douyin.until.CommunityConstant;
import com.mytype.douyin.until.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor, CommunityConstant {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从cookie中获取凭证
        System.out.println("拦截器 preHandle======" + handler.toString()+ "==============");
        String token = request.getParameter("token");

        if (token != null) {
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(token);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                int followeeCount = (int) followService.findFolloweeCount(user.getUserId(), ENTITY_TYPE_USER);
                int followerCount = (int) followService.findFollowerCount(ENTITY_TYPE_USER, user.getUserId());
                int userLikeCount = likeService.findUserLikeCount(user.getUserId());
                long favoriteCount = likeService.findFavoriteCount(user.getUserId(), ENTITY_TYPE_VIDEO);
                user.setFollowCount(followeeCount);
                user.setFollowerCount(followerCount);
                user.setTotalFavorited(userLikeCount);
                user.setFavoriteCount(favoriteCount);
                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("拦截器 afterCompletion======" + handler.toString()+ "==============");
        hostHolder.clear();
    }
}
