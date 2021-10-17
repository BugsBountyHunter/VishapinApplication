package com.ahmedsr.task.controller;

import com.ahmedsr.task.model.Comment;
import com.ahmedsr.task.model.CommentPage;
import com.ahmedsr.task.model.CommentSearchCriteria;
import com.ahmedsr.task.service.CommentService;
import com.ahmedsr.task.utils.CommentPDFExporter;
import com.ahmedsr.task.utils.Constants;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class CommentController {

    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping(value = "/comments")
    public ResponseEntity<List<Comment>> getComments(){
        List<Comment> comments = commentService.getCommentsAsObject();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping(value = "/comments_from_db")
    public ResponseEntity<Map<String, Object>> getCommentsFromDB(CommentPage commentPage, CommentSearchCriteria commentSearchCriteria){
        Page<Comment> comments = commentService.getCommentsFromDB(commentPage,commentSearchCriteria);
        Map<String, Object> response = new HashMap<>();
        if(comments.isEmpty()){
            response.put("Comments", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<Comment> commentsContent = comments.getContent();
        response.put("Comments", commentsContent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/comments/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat(Constants.DATE_TIME_PATTERN);
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Comment> listUsers = commentService.listAllComments();

        CommentPDFExporter exporter = new CommentPDFExporter(listUsers);
        exporter.export(response);
    }

    @GetMapping("/report/{format}")
    public ResponseEntity<String> generateReport(@PathVariable String format) throws IOException, JRException {
        return new ResponseEntity<>(commentService.exportReport(format),HttpStatus.OK);
    }
}
