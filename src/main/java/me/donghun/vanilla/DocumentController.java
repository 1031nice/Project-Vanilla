package me.donghun.vanilla;

import me.donghun.vanilla.dao.DocDAO;
import me.donghun.vanilla.model.Comment;
import me.donghun.vanilla.model.Doc;
import me.donghun.vanilla.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DocumentController {

    @Autowired
    private DocDAO docDAO;

    @GetMapping("/board")
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
        return "documentsList";
    }

    @GetMapping("/board/doc/{docId}")
    public String read(Model model, @PathVariable Long docId, @ModelAttribute("Redirect") String redirect, @ModelAttribute("Doc") Doc doc){
        if(!redirect.equals("Redirect")){
            doc = docDAO.getDocByDocId(docId);
        }
        List<Comment> comments = docDAO.getCommentsByDocId(docId);
        for (Comment comment : comments) {
            doc.getComments().add(comment);
        }
        model.addAttribute("doc", doc); // 그냥 객체를 줄 수도, 이름 붙여서 줄 수도 있다
        return "documentDetails";
    }

    @GetMapping("/board/doc/write")
    public String initWriteForm(Model model) {
        model.addAttribute("doc", new Doc());
        return "writeDocumentForm";
    }

    @PostMapping("/board/doc/write")
    public String processWriteForm(HttpSession session, @ModelAttribute Doc doc) {
        if(docDAO.getDocByTitle(doc.getTitle()) == null) {
            doc.setUserId(((User) session.getAttribute("user")).getId());
            docDAO.addDoc(doc);
        }
        return "redirect:/board";
    }

    @PostMapping("/board/doc/delete/{docId}")
    public String delete(@PathVariable Integer docId){
        docDAO.delete(docId);
        return "redirect:/board";
    }

    @GetMapping("/board/doc/edit")
    // 올때는 다 string인가보다. doc id가 long타입이라고 해도 html에서 벨류로 전달할 때는
    public ModelAndView edit(@ModelAttribute Doc doc){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("doc", doc);
        modelAndView.setViewName("editDocumentForm.html");
        return modelAndView;
    }

    @PostMapping("/board/doc/edit")
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
        return "redirect:/board";
    }

    @PostMapping("/board/search")
    public ModelAndView initSearchForm(@RequestParam String searchBy, @RequestParam String searchWord){
        List<Doc> foundDocs = new ArrayList<Doc>();
        if(searchBy.equals("title")){ // 이것도 개선 가능. 글이 엄청 많으면 그걸 다들고 올거 아니잖아. db에서 id에 맞는 글만 추려내는일 해줘야지
            List<Doc> docs = docDAO.getAllDoc();
            for(Doc doc : docs){
                if(doc.getTitle().contains(searchWord))
                    foundDocs.add(doc);
            }
        }
        else { // author
            foundDocs = docDAO.getDocsByUserId(searchWord);
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("foundDocs", foundDocs);
        mav.setViewName("findDocuments.html");
        return mav;
    }



    /*
    Comment
     */
    @PostMapping("/board/doc/comment/write")
    public String processCommentForm(@ModelAttribute Comment comment, @ModelAttribute Doc doc, RedirectAttributes attributes){
        /*
        1. database에 comment 튜플을 추가한다.
        2. 해당 Document가 comment id를 갖게 한다.
        Q. 그럼 document 하나가 하나의 comment id만 가질 수 있는건가?
        연결고리 역할을 하는 테이블이 필요할 거 같은데?
        야이 바보야 comment 테이블에서 doc Id를 갖고 있으면 되잖아
         */
        System.out.println(doc);
        attributes.addFlashAttribute("Redirect", new String("Redirect"));
        attributes.addFlashAttribute("Doc", doc);
        docDAO.addComment(comment);
        return "redirect:/board/doc/" + comment.getDocumentId();
    }

    @PostMapping("/board/doc/comment/delete")
    public String deleteComment(@ModelAttribute Doc doc, @RequestParam String commentId, RedirectAttributes attributes){
        attributes.addFlashAttribute("Redirect", new String("Redirect"));
        attributes.addFlashAttribute("Doc", doc);
        docDAO.deleteComment(Long.parseLong(commentId));
        return "redirect:/board/doc/" + doc.getId();
    }

}

