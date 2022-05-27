package com.mytype.douyin.service;


import com.mytype.douyin.dao.LoginTicketMapper;
import com.mytype.douyin.dao.UserMapper;
import com.mytype.douyin.entity.LoginTicket;
import com.mytype.douyin.entity.User;
import com.mytype.douyin.until.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(username);
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 注册用户
        User user = new User();
        user.setUsername(username);
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(password + user.getSalt()));
        user.setFollowCount(0);
        user.setFollowerCount(0);
        user.setSignature("新人报道！");
        user.setAvatar(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        map.put("userId",user.getUserId());

        return map;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (StringUtils.isBlank(username)) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        // 验证账号
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        // 验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)) {
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        // 生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getUserId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 3600L * 24 * 100));
        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("userId",user.getUserId());
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public Map<String, Object> userInfo(String token){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isBlank(token)) {
            map.put("errMsg", "参数错误!");
            return map;
        }
        LoginTicket loginTicket = loginTicketMapper.selectByTicket(token);
        if(loginTicket==null||loginTicket.getStatus()==1){
            map.put("errMsg", "用户未登录!");
            return map;
        }
        if(loginTicket.getExpired().before(new Date())){
            map.put("errMsg", "用户登录过期!");
            return map;
        }
        User user = userMapper.selectById(loginTicket.getUserId());
        if(user==null){
            map.put("errMsg", "用户不存在!");
            return map;
        }
        map.put("userId",user.getUserId());
        map.put("user", user);
        return map;
    }

    public void logout(String ticket) {
        loginTicketMapper.updateStatus(ticket, 1);
    }
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.selectByTicket(ticket);
    }

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }
}
