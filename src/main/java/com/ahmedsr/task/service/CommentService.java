package com.ahmedsr.task.service;

import com.ahmedsr.task.error.customexception.ConflictException;
import com.ahmedsr.task.error.customexception.NotfoundException;
import com.ahmedsr.task.model.Comment;
import com.ahmedsr.task.model.CommentPage;
import com.ahmedsr.task.model.CommentSearchCriteria;
import com.ahmedsr.task.repository.CommentCriteriaRepository;
import com.ahmedsr.task.repository.CommentRepository;
import com.ahmedsr.task.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;

@Service
public class CommentService {
    // == Fields ==
    private final RestTemplate restTemplate;
    private final CommentRepository commentRepository;
    private final CommentCriteriaRepository commentCriteriaRepository;

    //== Constructor ==
    @Autowired
    public CommentService(RestTemplateBuilder restTemplateBuilder, CommentRepository commentRepository, CommentCriteriaRepository commentCriteriaRepository) {
        //  By default, RestTemplate has infinite timeouts. But we can change this behavior by set connection and read timeouts
        this.restTemplate = restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(5000)).setReadTimeout(Duration.ofSeconds(5000)).build();
        this.commentRepository = commentRepository;
        this.commentCriteriaRepository = commentCriteriaRepository;
    }

    // == public method ==
    /**
     * Get Comments from url api https://jsonplaceholder.typicode.com/comments
     * @return List<Comment>
     */
    public List<Comment> getCommentsAsObject() {
            String url = Constants.COMMENT_URL;
            try{
                Comment[] tempComments = this.restTemplate.getForObject(url, Comment[].class);
                List<Comment> comments = Arrays.asList(tempComments);
                addComment(comments);
                return comments;
            }catch (Exception ex){
                throw new NotfoundException(String.format("error when get comments : %s " ,ex.getMessage()));
            }
    }

    public Page<Comment> getCommentsFromDB(CommentPage commentPage, CommentSearchCriteria commentSearchCriteria){
        Page<Comment> comments = commentCriteriaRepository.findAllWithFilters(commentPage, commentSearchCriteria);
        return comments;
    }

    /**
     *
     * @return List<Comment>
     */
    public List<Comment> listAllComments(){
        return commentRepository.findAll();
    }

    /**
     * add a new method to database but before add a new row check is comment.id already exist or not
     * @param comments
     */
    public void addComment(List<Comment> comments){
        for (Comment comment: comments){
            if(commentRepository.findById(comment.getId()).isPresent()){
                throw new ConflictException(String.format("Another record with the same id [%s] exists", comment.getId()));
            }
            Comment tempComment = new Comment(comment.getPostId(), comment.getName(), comment.getEmail(), comment.getBody());
            commentRepository.save(tempComment);
        }
    }

    /**
     * by using this method we can export report by using Jasper in pdf || html format
     * @param reportFormat
     * @return export file name with location.
     * @throws IOException
     * @throws JRException
     */

    public String exportReport(String reportFormat) throws IOException, JRException {
        //TODO:- need to
        BufferedImage image = ImageIO.read(getClass().getResource("/assets/nisLOGO.jpeg"));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Ahmed Saber");
        parameters.put("logo", image );

        UUID uuid = UUID.randomUUID();
        File path = new File(uuid.toString());

        //TODO:- need to more enhancement
        Pageable pageable = PageRequest.of(0, 5);
        Page<Comment> pageComments =  commentRepository.findAll(pageable);
        List<Comment> comments = pageComments.getContent();

        //load file and compile it
        File file = ResourceUtils.getFile("classpath:report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(comments);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        StringBuilder outputMessage = new StringBuilder( "report generated in project root path : " + path);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + ".html");
            outputMessage.append(".html");

        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + ".pdf");
            outputMessage.append(".pdf");
        }

        return outputMessage.toString() ;
    }

}
