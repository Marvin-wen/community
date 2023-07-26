package com.neu.community.controller;

import com.neu.community.service.DemoService;
import com.neu.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello world";
    }

    //这是一个解耦的示例，调用关系：controller->service->dao
    @RequestMapping("/data")
    @ResponseBody
    public String find() {
        return demoService.find();
    }

    //这是一个http请求和响应数据的示例
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 获取响应数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ":" + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write("<h1>东大人论坛<h1>");
    }

    // GET请求的处理示例，获取参数：student？current=1&limit=20

    @RequestMapping(path = "/student", method = RequestMethod.GET)
    @ResponseBody
    public String getStudents(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        System.out.println(current);
        System.out.println(limit);
        return "neu students";
    }

    // 把参数编排在路径中：/student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a neu student";
    }

    //POST请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    // 响应HTML数据，方式一
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("name", "zzb");
        mav.addObject("age", 55);
        mav.setViewName("/demo/view");
        return mav;
    }

    // 响应HTML数据，方式二
    // 两种方式本质上是一样的，这里的参数model由dispatcherServlet创建，所以只要返回view文件地址，dispatcherServlet即可组装页面
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name", "东北大学");
        model.addAttribute("age", 100);
        return "/demo/view";
    }

    // 响应JSON数据（异步请求：网页没刷新，后台和服务器交互了，例如注册时判断用户名是否被占用）
    // JAVA对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> map = new HashMap<>();
        map.put("姓名", "张三");
        map.put("年龄", 22);
        map.put("薪水", 8000);
        return map;
    }

    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("姓名", "张三");
        map.put("年龄", 22);
        map.put("薪水", 8000);
        list.add(map);

        map = new HashMap<>();
        map.put("姓名", "李四");
        map.put("年龄", 26);
        map.put("薪水", 9000);
        list.add(map);

        map = new HashMap<>();
        map.put("姓名", "王五");
        map.put("年龄", 29);
        map.put("薪水", 10000);
        list.add(map);
        return list;
    }

    /*
    * cookie示例：数据存在客户端，不安全
    * */
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("id","1");
        cookie.setPath("/community/demo");
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);
        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    @ResponseBody
    public String getCookie(@CookieValue("id") String id) {
        System.out.println(id);
        return "get cookie";
    }

    /*
    * session示例：数据存在服务端，安全但是服务器内存压力大，本质上是基于cookie实现的，cookie负责保存sessionId
    * 分布式部署的解决方案：粘性session，同步session，共享session，redis
    * */
    @RequestMapping(path = "/session/set",method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", 2);
        session.setAttribute("name","Test");
        return "set session";
    }

    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    // Ajax示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody
    public String testAjax (String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0, "操作成功！");
    }

}
