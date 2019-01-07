package com.howie.wen.controller;

import com.howie.wen.dao.UserDAO;
import com.howie.wen.model.User;
import com.howie.wen.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Random;

//import org.springframework.util.Assert;

/**
 * @Author:HowieLee
 * @Date:1/6/2019
 * @Description:com.howie.wenda.controller
 * @version:1.0
 */
@Controller
public class SettingController {
    @Autowired(required=false)
    @Qualifier("wendaService")
    WendaService wendaService;

    @RequestMapping(path = {"/setting"},method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession httpSession){
        return "Setting OK. " + wendaService.getMessage(1);
    }


    @Autowired(required=false)
    @Qualifier("userDAO")
    UserDAO userDAO;


//	@WebAppConfiguration
    @RequestMapping(path = {"/init"},method = {RequestMethod.GET})
    @ResponseBody
    public String initDatabase() {
        Random random = new Random();

        for(int i = 0 ; i < 15 ; i++){
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("xx");
            user.setSalt("xx");
            userDAO.addUser(user);

//            user.setPassword("xx");
//            userDAO.updatePassword(user);


        }

//        Assert.assertEquals("xx", userDAO.selectById(1).getPassword());
//        userDAO.deleteById(1);
//        Assert.assertNull(userDAO.selectById(1));
        return "hello world";
    }

}
