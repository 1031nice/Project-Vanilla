package me.donghun.vanilla;

import me.donghun.vanilla.dao.DocDAO;
import me.donghun.vanilla.dao.UserDAO;
import me.donghun.vanilla.model.Doc;
import me.donghun.vanilla.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.SessionFlashMapManager;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SampleController {

    private static final String VIEWS_USER_CREATE_FORM = "signUp.html";

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DocDAO docDAO;

    @GetMapping("/signUp")
    public String initSignUpForm(Model model){
        model.addAttribute("user", new User());
        return VIEWS_USER_CREATE_FORM;
    }

    @PostMapping("/signUp")
    public String processSignUpForm(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()) // 적절한 에러메시지 출력되는 원리는?
            return VIEWS_USER_CREATE_FORM;
        else {
//            UserDAO userDAO = new UserDAO();
            userDAO.signUp(user);
            return "redirect:/";
        }
    }

    @GetMapping({"/logout", "/"})
    public String logout(SessionStatus sessionStatus){
        sessionStatus.setComplete(); // session 비우기
        return "Login.html";
    }

    @PostMapping(value = "/login", produces = "text/html;")
    public String login(HttpSession session, @RequestParam String id, @RequestParam String pw){
        User user = new User(id, pw);
//        UserDAO userDAO = new UserDAO();
        user = userDAO.login(user);
        if(user != null) {
            session.setAttribute("user", user);
            return "redirect:/show";
        }
        else
            return "Login.html";
    }

    @GetMapping("/show")
    public String show(Model model){
//        DocDAO docDAO = new DocDAO();
        List<Doc> docList = new ArrayList<>();
        docDAO.getAll(docList);
        model.addAttribute(docList);
        return "list.html";
    }

    @GetMapping("/read/{docId}")
    public String read(Model model, @PathVariable Integer docId){
//        DocDAO docDAO = new DocDAO();
        Doc doc = docDAO.get(docId);
        model.addAttribute(doc);
        return "detail.html";
    }

    @GetMapping("/write")
    public String write(Model model) {
        model.addAttribute("doc", new Doc());
        return "Write.html";
    }

    @PostMapping("/write")
    public String writeSubmit(@ModelAttribute Doc doc) {
//        DocDAO docDAO = new DocDAO();
        docDAO.add(doc);
        return "redirect:/show";
    }

    @GetMapping("/delete/{docId}")
    public String delete(@PathVariable Integer docId){
        docDAO.delete(docId);
        return "redirect:/show";
    }
}
