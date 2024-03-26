package com.example.springSecurity.controller;

import com.example.springSecurity.entity.SecurityUser;
import com.example.springSecurity.service.SecurityUserService;
import com.example.springSecurity.util.AsideUtil;
import com.example.springSecurity.util.ImageUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class SecurityUserController {
    private final SecurityUserService securityUserService;
    private final BCryptPasswordEncoder bCryptEncoder;
    private final ResourceLoader resourceLoader;
    private final ImageUtil imageUtil;
    private final AsideUtil asideUtil;

    @Value("${spring.servlet.multipart.location}") private String uploadDir;

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/register")
    public String registerProc(String uid, String pwd, String pwd2, String uname, String email, String provider,
                               MultipartHttpServletRequest req, Model model) {
        String filename = null;
        MultipartFile filePart = req.getFile("picture");

        SecurityUser sUser = securityUserService.getUserByUid(uid);
        if (sUser != null){
            model.addAttribute("msg", "사용자 아이디가 중복 되었습니다.");
            model.addAttribute("url", "user/register");
            return "common/alertMsg";
        }
        if (pwd == null || !pwd.equals(pwd2)) {
            model.addAttribute("msg", "비밀번호 입력이 잘못 되었습니다.");
            model.addAttribute("url", "user/register");
            return "common/alertMsg";
        }
        if (filePart.getContentType().contains("image")) {
            filename = filePart.getOriginalFilename();
            String path = uploadDir + "profile/" + filename;
            try {
                filePart.transferTo(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
            filename = imageUtil.squareImage(uid, filename);
        }
        String hashedPwd = bCryptEncoder.encode(pwd);
        sUser = SecurityUser.builder()
                .uid(uid).pwd(hashedPwd).uname(uname).email(email).provider(provider)
                .picture("/ss/file/download/profile/" + filename)
                .build();
        securityUserService.insertSecurityUser(sUser);
        model.addAttribute("msg", "가입이 완료 되었습니다.");
        model.addAttribute("url", "user/login");
        return "common/alertMsg";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpSession session, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 세션의 현재 사용자 아이디
        String uid = auth.getName();
        SecurityUser user = securityUserService.getUserByUid(uid);

        session.setAttribute("sessUid", uid);
        session.setAttribute("sessUname", user.getUname());
        session.setAttribute("picture", user.getPicture());
        session.setAttribute("email", user.getEmail());

        Resource resource = resourceLoader.getResource("classpath:/static/data/todayQuote.txt");
        String quoteFile = null;
        try{
            quoteFile = resource.getURI().getPath();
        } catch (IOException e){
            e.printStackTrace();
        }
        String stateMsg = asideUtil.getTodayQuote(quoteFile);
        session.setAttribute("stateMsg", stateMsg);

        log.info(uid,user.getUname());
        model.addAttribute("msg", user.getUname() + "님 환영합니다.");
        model.addAttribute("url", "user/list");
        return "common/alertMsg";
    }

    @GetMapping({"/list/{page}", "/list"})
    public String list(@PathVariable(required=false) Integer page, HttpSession session, Model model) {
        page = (page == null) ? 1 : page;
        session.setAttribute("currentUserPage", page);
        List<SecurityUser> list = securityUserService.getSecurityUserList(page);
        model.addAttribute("userList", list);

        // for pagination
        int totalUsers = securityUserService.getSecurityUserCount();
        int totalPages = (int) Math.ceil(totalUsers * 1.0 / securityUserService.COUNT_PER_PAGE);
        List<Integer> pageList = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++)
            pageList.add(i);
        model.addAttribute("pageList", pageList);

        return "user/list";
    }

    @ResponseBody
    @GetMapping("/detail/{uid}")
    public String detail(@PathVariable String uid) {
        SecurityUser securityUser = securityUserService.getUserByUid(uid);
        JSONObject jUser = new JSONObject();
        jUser.put("uid", uid);
        jUser.put("uid2", uid);
        jUser.put("hashedPwd", securityUser.getPwd());
        jUser.put("uname", securityUser.getUname());
        jUser.put("email", securityUser.getEmail());
        jUser.put("provider", securityUser.getProvider());
        jUser.put("role", securityUser.getRole());
        jUser.put("picture", securityUser.getPicture());
        return jUser.toString();
    }
}
