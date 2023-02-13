package com.jessica.social.network.serverless.controller;

import com.jessica.social.network.serverless.bo.UserBo;
import com.jessica.social.network.serverless.service.UserService;
import com.jessica.social.network.serverless.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public UserVo createUser(@Valid @RequestBody UserVo userVo, HttpServletResponse response) {
        UserBo userBo = userVo.toBo();
        this.userService.createUser(userBo);
        return UserVo.fromBo(userBo);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void deleteUserById(@PathVariable String id) {
        this.userService.deleteById(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public UserVo updateUser(@PathVariable String id, @RequestBody UserVo userVo, HttpServletResponse response) {
        UserBo userBo = userVo.toBo();
        this.userService.updateUser(userBo);
        return UserVo.fromBo(userBo);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public UserVo getUserById(@PathVariable String id) {
        UserBo userBo = this.userService.getById(id);
        return UserVo.fromBo(userBo);
    }
}
