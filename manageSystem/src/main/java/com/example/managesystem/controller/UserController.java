package com.example.managesystem.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

/**
 * @projectName: manageSystem
 * @package: main.java.com.example.managesystem.Controller
 * @className: HelloWorld
 * @author: zhangcx
 * @description: TODO
 * @date: 2024/5/7 23:24
 * @version: 1.0
 */
@RestController
public class UserController {

    @RequestMapping("/admin/addUser")
    public String addUser(HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            String headerData = new String(Base64.getDecoder().decode(header));
            JSONObject data = JSON.parseObject(headerData);
            if (!"admin".equals(data.get("role"))) {
                return "sorry,you have no access";
            }
            String bodyData = request.getParameter("data");
            JSONObject body = JSON.parseObject(bodyData);
            String userId = body.getString("userId");
            List<String> endpoint = (List<String>) body.get("endpoint");
            StringBuffer content = new StringBuffer();
            try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/dataFile/UserAccess"))) {
                String line;
                boolean exist = false;
                while((line = reader.readLine()) != null) {
                    String[] userdata = line.split("\\|");
                    if(userdata[0].equals(userId)) {
                        Set<String> endpointList = new HashSet<>(Arrays.asList(userdata[1].split(",")));
                        endpointList.addAll(endpoint);
                        exist = true;
                        StringBuilder sb = new StringBuilder();
                        for (String s : endpointList) {
                            sb.append(s).append(",");
                        }
                        sb.delete(sb.length()-1,sb.length());
                        content.append(userdata[0]).append("|").append(sb).append("\n");
                    } else {
                        content.append(line).append("\n");
                    }
                }
                if (!exist) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : endpoint) {
                        sb.append(s).append(",");
                    }
                    sb.delete(sb.length()-1,sb.length());
                    content.append(userId).append("|").append(sb).append("\n");
                }
                try(BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/dataFile/UserAccess"))) {
                    writer.write(content.toString());
                } catch (Exception e) {
                    return "error";
                }
            } catch (FileNotFoundException e) {
                return "file error";
            } catch (IOException e) {
                return "file error";
            }
            return "success";
        } catch (Exception e) {
            return "request error";
        }
    }

    @RequestMapping("/user/resource")
    public String accessCheck(HttpServletRequest request) {
        try {
            String header = request.getHeader("Authorization");
            String headerData = new String(Base64.getDecoder().decode(header));
            JSONObject data = JSON.parseObject(headerData);
            String userId = data.getString("userId");

            String bodyData = request.getParameter("data");
            JSONObject body = JSON.parseObject(bodyData);
            String resource = body.getString("resource");

            String msg = "";
            try(BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/dataFile/UserAccess"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] userdata = line.split("\\|");
                    if (userdata[0].equals(userId)) {
                        List<String> endpoint = Arrays.asList(userdata[1].split(","));
                        if (endpoint.contains(resource)) {
                            msg = "success";
                        } else {
                            msg = "you have no access";
                        }
                    }
                }
                return msg.equals("") ? "no user" : msg;
            } catch (FileNotFoundException e) {
                return "file error";
            } catch (IOException e) {
                return "file error";
            }
        } catch (Exception e) {
            return "request error";
        }
    }
}
