package baitmate.Service;

import baitmate.model.Post;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface PostReportService {

    List<Post> getFilteredPosts(List<Long> postIds, boolean allPosts,
                                LocalDate startDate, LocalDate endDate, boolean includePosts);

    // Word Report Generation
    ByteArrayInputStream generateWordReport(List<Long> postIds, boolean allPosts,
                                            LocalDate startDate, LocalDate endDate,
                                            boolean includePosts) throws IOException;


    void createTitle(XWPFDocument document);

    void addSummarySection(XWPFDocument document, List<Post> posts);

    void createPostTable(XWPFDocument document, List<Post> posts);

    void styleHeaderCell(XWPFTableCell cell);

    void addEngagementStats(XWPFDocument document, List<Post> posts);

    void addLocationAnalysis(XWPFDocument document, List<Post> posts);

    // Excel Report Generation
    ByteArrayInputStream generateExcelReport(List<Long> postIds, boolean allPosts,
                                             LocalDate startDate, LocalDate endDate,
                                             boolean includePosts) throws IOException;
    void createPostsSheet(Sheet sheet, List<Post> posts);

    void createSummarySheet(Sheet sheet, List<Post> posts);

    void createLocationSheet(Sheet sheet, List<Post> posts);

    CellStyle createHeaderStyle(Workbook workbook);

    CellStyle createBoldStyle(Workbook workbook);

    void addSummaryRow(Sheet sheet, int rowNum, String label, Number value, CellStyle style);

    void autoSizeAllSheets(Workbook workbook);
}
