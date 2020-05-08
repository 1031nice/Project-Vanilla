package me.donghun.vanilla;

import me.donghun.vanilla.dao.DocDAO;
import me.donghun.vanilla.dao.UserDAO;
import me.donghun.vanilla.model.Comment;
import me.donghun.vanilla.model.Doc;
import me.donghun.vanilla.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        List<Doc> docList = docDAO.getAllDoc();
//        for(Doc doc : docList){ 여기서 받을 필요가 있나? 게다가 여기서 받을 경우 read에서 전달해주어야 한다
//            Comment comment = docDAO.getCommentsByDocId(doc.getId());
//            if(comment != null) {
//                doc.getComments().add(comment);
//                System.out.println("댓글이 있는 doc: " + doc.getTitle());
//                System.out.println("댓글 내용: " + doc.getComments().get(0).getContent());
//            }
//        }
        model.addAttribute(docList);
        return "list.html";
    }

    @GetMapping("/read/{docId}")
    public String read(Model model, @PathVariable Long docId){
        Doc doc = docDAO.getDocByDocId(docId);
        List<Comment> comments = docDAO.getCommentsByDocId(docId);
        for (Comment comment : comments) {
            doc.getComments().add(comment);
        }
        model.addAttribute(doc);
        return "detail.html";
    }

    @GetMapping("/write")
    public String initWriteForm(Model model) {
        model.addAttribute("doc", new Doc());
        return "Write.html";
    }

    @PostMapping("/write")
    public String processWriteForm(HttpSession session, @ModelAttribute Doc doc) {
        doc.setUserId(((User)session.getAttribute("user")).getId());
        docDAO.addDoc(doc);
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
    public String editSubmit(@ModelAttribute Doc doc){
        /*
         Q. 왜 docId는 ModelAttribute로 처리가 안될까
         A. Doc의 변수 이름은 docId가 아니라 id인데
         view에서 값을 전달할 때 name은 docId여서 인식을 못한거지
         view에서 name을 docId가 아니라 그냥 id로 수정하였음.
         */
        System.out.println(doc.getContent());
        doc.setContent(doc.getContent().replaceAll("'", "\\'"));
        System.out.println(doc.getContent());
        docDAO.update(doc);
        return "redirect:/show";
    }

    @GetMapping("/modify")
    public ModelAndView initModifyForm(HttpSession session){
        User user = (User) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("userModify.html");
        return modelAndView;
    }

    @PostMapping("/modify")
    public String processModifyForm(@ModelAttribute User user, HttpSession session){
//        System.out.println(user.getId()); // disable 설정되어 있는 값은 전달이 안되나보네?
//        System.out.println(user.getPw());
//        System.out.println(user.getName());
        session.removeAttribute("user");
        session.setAttribute("user", user);
        userDAO.modify(user);
        return "redirect:/show";
    }

    @PostMapping("/comment")
    public String processCommentForm(@ModelAttribute Comment comment){
        /*
        1. database에 comment 튜플을 추가한다.
        2. 해당 Document가 comment id를 갖게 한다.
        Q. 그럼 document 하나가 하나의 comment id만 가질 수 있는건가?
        연결고리 역할을 하는 테이블이 필요할 거 같은데?
        야이 바보야 comment 테이블에서 doc Id를 갖고 있으면 되잖아
         */
        docDAO.addComment(comment);
        System.out.println(comment);
        return "redirect:/show";
    }
}

