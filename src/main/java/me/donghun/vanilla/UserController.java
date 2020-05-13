package me.donghun.vanilla;

import me.donghun.vanilla.dao.UserDAO;
import me.donghun.vanilla.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {

    private static final String VIEWS_USER_CREATE_FORM = "signUpForm.html";

    @Autowired
    private UserDAO userDAO;

    @GetMapping({"/logout", "/"})
    public String logout(SessionStatus sessionStatus){
        sessionStatus.setComplete(); // session 비우기
        return "loginForm.html";
    }

    @PostMapping(value = "/login", produces = "text/html;")
    public String login(HttpSession session, @RequestParam String id, @RequestParam String pw){
        User user = new User(id, pw);
        user = userDAO.login(user);
        if(user != null) {
            session.setAttribute("user", user);
            return "redirect:/board";
        }
        else
            return "loginForm.html";
    }

    @GetMapping("/user/new")
    public String initSignUpForm(Model model){
        model.addAttribute("user", new User());
        return VIEWS_USER_CREATE_FORM;
    }

    @PostMapping("/user/new")
    public String processSignUpForm(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()) // 적절한 에러메시지 출력되는 원리는?
            return VIEWS_USER_CREATE_FORM;
        else {
            userDAO.signUp(user);
            return "redirect:/";
        }
    }

    @GetMapping("/user/edit")
    public ModelAndView initModifyForm(HttpSession session){
        User user = (User) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("editUserForm.html");
        return modelAndView;
    }

    @PostMapping("/user/edit")
    public String processModifyForm(@ModelAttribute User user, HttpSession session){
//        System.out.println(user.getId()); // disable 설정되어 있는 값은 전달이 안되나보네?
//        System.out.println(user.getPw());
//        System.out.println(user.getName());
        session.removeAttribute("user");
        session.setAttribute("user", user);
        userDAO.modify(user);
        return "redirect:/board";
    }

}
