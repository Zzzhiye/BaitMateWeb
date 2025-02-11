package baitmate.Controller;

import baitmate.Service.PostReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class PostReportController {

    @Autowired
    private PostReportService postReportService;

    @GetMapping("/posts/word")
    public ResponseEntity<Resource> downloadWordReport(
            @RequestParam(required = false) List<Long> postIds,  // Changed from userIds
            @RequestParam(required = false) boolean allPosts,    // Changed from allUsers
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) boolean includePosts) throws IOException {
        ByteArrayInputStream bis = postReportService.generateWordReport(postIds, allPosts, startDate, endDate, includePosts);
        byte[] reportBytes = bis.readAllBytes();
        ByteArrayResource resource = new ByteArrayResource(reportBytes) {
            @Override
            public String getFilename() {
                return "PostReport.docx";
            }
        };
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PostReport.docx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                .contentLength(reportBytes.length)
                .body(resource);
    }

    @GetMapping("/posts/excel")
    public ResponseEntity<Resource> downloadExcelReport(
            @RequestParam(required = false) List<Long> postIds,  // Changed from userIds
            @RequestParam(required = false) boolean allPosts,    // Changed from allUsers
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) boolean includePosts) throws IOException {

        System.out.println("postIds: " + postIds);
        System.out.println("allPosts: " + allPosts);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        System.out.println("includePosts: " + includePosts);

        ByteArrayInputStream bis = postReportService.generateExcelReport(postIds, allPosts, startDate, endDate, includePosts);
        byte[] reportBytes = bis.readAllBytes();
        ByteArrayResource resource = new ByteArrayResource(reportBytes) {
            @Override
            public String getFilename() {
                return "PostReport.xlsx";
            }
        };
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=PostReport.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .contentLength(reportBytes.length)
                .body(resource);
    }
}