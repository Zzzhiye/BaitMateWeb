package baitmate.ImplementationMethod;

import baitmate.Repository.PostRepository;
import baitmate.Repository.UserRepository;
import baitmate.model.Post;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostReportService {

  @Autowired private PostRepository postRepository;

  @Autowired private UserRepository userRepository;

  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  // Filtering Method
  private List<Post> getFilteredPosts(
      List<Long> postIds,
      boolean allPosts,
      LocalDate startDate,
      LocalDate endDate,
      boolean includePosts) {
    List<Post> posts;

    if (includePosts) {
      if (allPosts) {
        posts = postRepository.findAll();
      } else if (postIds != null && !postIds.isEmpty()) {
        posts = postRepository.findAllByPostIds(postIds);
      } else {
        posts = List.of();
      }

      if (startDate != null) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        posts =
            posts.stream()
                .filter(post -> !post.getPostTime().isBefore(startDateTime))
                .collect(Collectors.toList());
      }

      if (endDate != null) {
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        posts =
            posts.stream()
                .filter(post -> post.getPostTime().isBefore(endDateTime))
                .collect(Collectors.toList());
      }
    } else {
      posts = List.of();
    }

    return posts;
  }

  // Word Report Generation
  public ByteArrayInputStream generateWordReport(
      List<Long> postIds,
      boolean allPosts,
      LocalDate startDate,
      LocalDate endDate,
      boolean includePosts)
      throws IOException {
    List<Post> posts = getFilteredPosts(postIds, allPosts, startDate, endDate, includePosts);

    try (XWPFDocument document = new XWPFDocument()) {
      createTitle(document);
      addSummarySection(document, posts);
      createPostTable(document, posts);
      addEngagementStats(document, posts);
      addLocationAnalysis(document, posts);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      document.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }

  private void createTitle(XWPFDocument document) {
    XWPFParagraph title = document.createParagraph();
    title.setAlignment(ParagraphAlignment.CENTER);
    XWPFRun titleRun = title.createRun();
    titleRun.setText("BaitMate Post Analysis Report");
    titleRun.setBold(true);
    titleRun.setFontSize(16);
    titleRun.addBreak();

    XWPFRun dateRun = title.createRun();
    dateRun.setText("Generated on: " + LocalDateTime.now().format(DATE_FORMATTER));
    dateRun.setFontSize(10);
    dateRun.addBreak();
    dateRun.addBreak();
  }

  private void addSummarySection(XWPFDocument document, List<Post> posts) {
    XWPFParagraph summary = document.createParagraph();
    XWPFRun summaryRun = summary.createRun();
    summaryRun.setBold(true);
    summaryRun.setText("Summary Statistics");
    summaryRun.addBreak();

    summaryRun = summary.createRun();
    summaryRun.setText("Total Posts: " + posts.size());
    summaryRun.addBreak();
    summaryRun.setText(
        "Active Posts: "
            + posts.stream().filter(p -> "active".equalsIgnoreCase(p.getPostStatus())).count());
    summaryRun.addBreak();
    summaryRun.setText("Total Likes: " + posts.stream().mapToInt(Post::getLikeCount).sum());
    summaryRun.addBreak();
    summaryRun.setText("Total Saves: " + posts.stream().mapToInt(Post::getSavedCount).sum());
    summaryRun.addBreak();
    summaryRun.setText(
        "Average Accuracy Score: "
            + String.format(
                "%.2f", posts.stream().mapToDouble(Post::getAccuracyScore).average().orElse(0.0)));
    summaryRun.addBreak();
    summaryRun.addBreak();
  }

  private void createPostTable(XWPFDocument document, List<Post> posts) {
    XWPFTable table = document.createTable();

    XWPFTableRow headerRow = table.getRow(0);
    String[] headers = {
      "Post ID",
      "Title",
      "Status",
      "Location",
      "Post Time",
      "Likes",
      "Saves",
      "Accuracy Score",
      "Comments"
    };

    for (int i = 0; i < headers.length; i++) {
      XWPFTableCell cell = i == 0 ? headerRow.getCell(0) : headerRow.addNewTableCell();
      cell.setText(headers[i]);
      styleHeaderCell(cell);
    }

    for (Post post : posts) {
      XWPFTableRow row = table.createRow();
      row.getCell(0).setText(post.getId().toString());
      row.getCell(1).setText(post.getPostTitle());
      row.getCell(2).setText(post.getPostStatus());
      row.getCell(3).setText(post.getLocation());
      row.getCell(4).setText(post.getPostTime().format(DATE_FORMATTER));
      row.getCell(5).setText(String.valueOf(post.getLikeCount()));
      row.getCell(6).setText(String.valueOf(post.getSavedCount()));
      row.getCell(7).setText(String.format("%.2f", post.getAccuracyScore()));
      row.getCell(8).setText(String.valueOf(post.getComments().size()));
    }
  }

  private void styleHeaderCell(XWPFTableCell cell) {
    XWPFParagraph paragraph = cell.getParagraphs().get(0);
    XWPFRun run = paragraph.createRun();
    run.setBold(true);
    run.setFontFamily("Arial");
    run.setFontSize(11);
  }

  private void addEngagementStats(XWPFDocument document, List<Post> posts) {
    XWPFParagraph engagementPara = document.createParagraph();
    engagementPara.setSpacingBefore(200);
    XWPFRun engagementRun = engagementPara.createRun();
    engagementRun.setBold(true);
    engagementRun.setText("Engagement Analysis");
    engagementRun.addBreak();
    engagementRun.addBreak();

    List<Post> topLikedPosts =
        posts.stream()
            .sorted((p1, p2) -> p2.getLikeCount() - p1.getLikeCount())
            .limit(5)
            .collect(Collectors.toList());

    engagementRun = engagementPara.createRun();
    engagementRun.setText("Top 5 Most Liked Posts:");
    engagementRun.addBreak();

    for (Post post : topLikedPosts) {
      engagementRun.setText(
          String.format(
              "- %s (ID: %d) - %d likes", post.getPostTitle(), post.getId(), post.getLikeCount()));
      engagementRun.addBreak();
    }
  }

  private void addLocationAnalysis(XWPFDocument document, List<Post> posts) {
    XWPFParagraph locationPara = document.createParagraph();
    locationPara.setSpacingBefore(200);
    XWPFRun locationRun = locationPara.createRun();
    locationRun.setBold(true);
    locationRun.setText("Location Distribution");
    locationRun.addBreak();
    locationRun.addBreak();

    Map<String, Long> locationCounts =
        posts.stream().collect(Collectors.groupingBy(Post::getLocation, Collectors.counting()));

    locationRun = locationPara.createRun();
    XWPFRun finalLocationRun = locationRun;
    locationCounts.forEach(
        (location, count) -> {
          finalLocationRun.setText(String.format("%s: %d posts", location, count));
          finalLocationRun.addBreak();
        });
  }

  // Excel Report Generation
  public ByteArrayInputStream generateExcelReport(
      List<Long> postIds,
      boolean allPosts,
      LocalDate startDate,
      LocalDate endDate,
      boolean includePosts)
      throws IOException {
    List<Post> posts = getFilteredPosts(postIds, allPosts, startDate, endDate, includePosts);

    try (Workbook workbook = new XSSFWorkbook()) {
      Sheet postsSheet = workbook.createSheet("Posts Data");
      createPostsSheet(postsSheet, posts);

      Sheet summarySheet = workbook.createSheet("Summary");
      createSummarySheet(summarySheet, posts);

      Sheet locationSheet = workbook.createSheet("Location Analysis");
      createLocationSheet(locationSheet, posts);

      autoSizeAllSheets(workbook);

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    }
  }

  private void createPostsSheet(Sheet sheet, List<Post> posts) {
    CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());

    Row headerRow = sheet.createRow(0);
    String[] headers = {
      "Post ID",
      "Title",
      "Status",
      "Location",
      "Post Time",
      "Likes",
      "Saves",
      "Accuracy Score",
      "Comments Count",
      "Images Count"
    };

    for (int i = 0; i < headers.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(headers[i]);
      cell.setCellStyle(headerStyle);
    }

    int rowNum = 1;
    for (Post post : posts) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(post.getId());
      row.createCell(1).setCellValue(post.getPostTitle());
      row.createCell(2).setCellValue(post.getPostStatus());
      row.createCell(3).setCellValue(post.getLocation());
      row.createCell(4).setCellValue(post.getPostTime().format(DATE_FORMATTER));
      row.createCell(5).setCellValue(post.getLikeCount());
      row.createCell(6).setCellValue(post.getSavedCount());
      row.createCell(7).setCellValue(post.getAccuracyScore());
      row.createCell(8).setCellValue(post.getComments().size());
      row.createCell(9).setCellValue(post.getImages().size());
    }
  }

  private void createSummarySheet(Sheet sheet, List<Post> posts) {
    CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
    CellStyle boldStyle = createBoldStyle(sheet.getWorkbook());

    Row titleRow = sheet.createRow(0);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue("Post Analytics Summary");
    titleCell.setCellStyle(headerStyle);

    int rowNum = 2;

    addSummaryRow(sheet, rowNum++, "Total Posts", posts.size(), boldStyle);
    addSummaryRow(
        sheet,
        rowNum++,
        "Active Posts",
        posts.stream().filter(p -> "active".equalsIgnoreCase(p.getPostStatus())).count(),
        boldStyle);
    addSummaryRow(
        sheet,
        rowNum++,
        "Total Likes",
        posts.stream().mapToInt(Post::getLikeCount).sum(),
        boldStyle);
    addSummaryRow(
        sheet,
        rowNum++,
        "Total Saves",
        posts.stream().mapToInt(Post::getSavedCount).sum(),
        boldStyle);
    addSummaryRow(
        sheet,
        rowNum++,
        "Average Accuracy Score",
        posts.stream().mapToDouble(Post::getAccuracyScore).average().orElse(0.0),
        boldStyle);
  }

  private void createLocationSheet(Sheet sheet, List<Post> posts) {
    CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());

    Row headerRow = sheet.createRow(0);
    headerRow.createCell(0).setCellValue("Location");
    headerRow.createCell(1).setCellValue("Post Count");
    headerRow.createCell(2).setCellValue("Total Likes");
    headerRow.createCell(3).setCellValue("Average Accuracy Score");

    for (Cell cell : headerRow) {
      cell.setCellStyle(headerStyle);
    }

    Map<String, List<Post>> locationGroups =
        posts.stream().collect(Collectors.groupingBy(Post::getLocation));

    int rowNum = 1;
    for (Map.Entry<String, List<Post>> entry : locationGroups.entrySet()) {
      Row row = sheet.createRow(rowNum++);
      List<Post> locationPosts = entry.getValue();

      row.createCell(0).setCellValue(entry.getKey());
      row.createCell(1).setCellValue(locationPosts.size());
      row.createCell(2).setCellValue(locationPosts.stream().mapToInt(Post::getLikeCount).sum());
      row.createCell(3)
          .setCellValue(
              locationPosts.stream().mapToDouble(Post::getAccuracyScore).average().orElse(0.0));
    }
  }

  private CellStyle createHeaderStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    return style;
  }

  private CellStyle createBoldStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    return style;
  }

  private void addSummaryRow(Sheet sheet, int rowNum, String label, Number value, CellStyle style) {
    Row row = sheet.createRow(rowNum);
    Cell labelCell = row.createCell(0);
    labelCell.setCellValue(label);
    labelCell.setCellStyle(style);

    Cell valueCell = row.createCell(1);
    if (value instanceof Double) {
      valueCell.setCellValue(value.doubleValue());
    } else {
      valueCell.setCellValue(value.longValue());
    }
  }

  private void autoSizeAllSheets(Workbook workbook) {
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
      Sheet sheet = workbook.getSheetAt(i);
      if (sheet.getPhysicalNumberOfRows() > 0) {
        Row row = sheet.getRow(0);
        for (int j = 0; j < row.getLastCellNum(); j++) {
          sheet.autoSizeColumn(j);
        }
      }
    }
  }
}
