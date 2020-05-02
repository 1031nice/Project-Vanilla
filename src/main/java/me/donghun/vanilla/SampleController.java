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
import org.springframework.web.servlet.ModelAndView;
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
        List<Doc> docList = new ArrayList<>();
        docDAO.getAll(docList);
        model.addAttribute(docList);
        return "list.html";
    }

    @GetMapping("/read/{docId}")
    public String read(Model model, @PathVariable Integer docId){
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
        docDAO.add(doc);
        return "redirect:/show";
    }

    @GetMapping("/delete/{docId}")
    public String delete(@PathVariable Integer docId){
        docDAO.delete(docId);
        return "redirect:/show";
    }

    @GetMapping("/edit")
    // 올때는 다 string인가보다. doc id가 long타입이라고 해도 html에서 벨류로 전달할 때는
    public ModelAndView edit(@ModelAttribute Doc doc){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("doc", doc);
        modelAndView.setViewName("edit.html");
        return modelAndView;
    }

    @PostMapping("/edit")
    public String edit2(@ModelAttribute Doc doc){
        /*
         왜 docId는 ModelAttribute로 처리가 안될까
         A. Doc의 변수 이름은 docId가 아니라 id인데
         view에서 값을 전달할 때 name은 docId여서 인식을 못한거지
         view에서 name을 docId가 아니라 그냥 id로 수정하였음.
         */
        System.out.println(doc.getId());
        docDAO.update(doc);
        return "redirect:/show";
    }
}