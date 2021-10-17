package com.ahmedsr.task.utils;

import com.ahmedsr.task.model.Comment;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CommentPDFExporter {

    private List<Comment> comments;

    public CommentPDFExporter(List<Comment> comments) {
        this.comments = comments;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(10);


        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("id", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("name", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("email", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("body", font));
        table.addCell(cell);
    }


    private void writeTableData(PdfPTable table) {
        for (Comment comment : comments) {
            table.addCell(String.valueOf(comment.getId()));
            table.addCell(comment.getName());
            table.addCell(comment.getEmail());
            table.addCell(comment.getBody());
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("List of Comments", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 5.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }
}
