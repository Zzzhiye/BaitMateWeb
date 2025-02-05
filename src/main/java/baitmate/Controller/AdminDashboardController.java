package baitmate.Controller;

import baitmate.ImplementationMethod.DashboardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        // Basic Statistics
        model.addAttribute("stats", dashboardService.getDashboardStats());

        // Timeline Data
        model.addAttribute("timelineData", dashboardService.getTimelineData());

        // Catch Statistics
        model.addAttribute("catchStats", dashboardService.getCatchStatistics());

        // Today's Most Caught Fish
        List<Map<String, Object>> todayMostCaughtFish = dashboardService.getTodayMostCaughtFish();
        model.addAttribute("todayMostCaughtFish", todayMostCaughtFish);

        // User Related
        model.addAttribute("recentUsers", dashboardService.getRecentUsers());
        model.addAttribute("mostActiveUsers", dashboardService.getMostActiveUsers());

        // Post Related
        model.addAttribute("recentPosts", dashboardService.getRecentPosts());
        model.addAttribute("topLikedPosts", dashboardService.getTopLikedPosts());

        // Catch Record Related
        model.addAttribute("recentCatches", dashboardService.getRecentCatches());
        model.addAttribute("topCatches", dashboardService.getTopCatches());

        // Location Related
        model.addAttribute("popularLocations", dashboardService.getPopularLocations());

        // Activity Summary
        model.addAttribute("activitySummary", dashboardService.getActivitySummary());

        // Engagement Metrics
        model.addAttribute("engagementMetrics", dashboardService.getEngagementMetrics());

        return "dashboard";
    }

    @GetMapping("/report")
    public String getReport(Model model) {
        // Basic Statistics
        model.addAttribute("stats", dashboardService.getDashboardStats());

        // User Related
        model.addAttribute("recentUsers", dashboardService.getRecentUsers());
        model.addAttribute("mostActiveUsers", dashboardService.getMostActiveUsers());

        // Post Related
        model.addAttribute("recentPosts", dashboardService.getRecentPosts());
        model.addAttribute("topLikedPosts", dashboardService.getTopLikedPosts());

        // Catch Record Related
        model.addAttribute("recentCatches", dashboardService.getRecentCatches());
        model.addAttribute("topCatches", dashboardService.getTopCatches());

        // Location Related
        model.addAttribute("popularLocations", dashboardService.getPopularLocations());

        // Activity Summary
        model.addAttribute("activitySummary", dashboardService.getActivitySummary());

        // Engagement Metrics
        model.addAttribute("engagementMetrics", dashboardService.getEngagementMetrics());

        // Today's Most Caught Fish
        model.addAttribute("todayMostCaughtFish", dashboardService.getTodayMostCaughtFish());

        return "report";
    }

    // API endpoint for AJAX updates
    @GetMapping("/api/dashboard-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> timelineData = dashboardService.getTimelineData();
        Map<String, Object> catchStats = dashboardService.getCatchStatistics();
        
        Map<String, Object> response = new HashMap<>();
        response.put("postActivity", timelineData.get("posts"));
        response.put("catchActivity", timelineData.get("catches"));
        response.put("todayMostCaughtFish", dashboardService.getTodayMostCaughtFish());
        response.put("catchStats", catchStats);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> dashboardData = Map.of(
            "basicStats", dashboardService.getDashboardStats(),
            "activitySummary", dashboardService.getActivitySummary(),
            "engagementMetrics", dashboardService.getEngagementMetrics()
        );
        return ResponseEntity.ok(dashboardData);
    }

    @GetMapping("/api/top-catches")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getTopCatches(
            @RequestParam(defaultValue = "0") int month,
            @RequestParam(defaultValue = "0") int year) {
        
        if (month == 0 || year == 0) {
            LocalDate now = LocalDate.now();
            month = now.getMonthValue();
            year = now.getYear();
        }
        
        System.out.println("Fetching top catches for month: " + month + ", year: " + year);
        List<Map<String, Object>> result = dashboardService.getTopCatchesByMonth(month, year);
        System.out.println("Found " + (result != null ? result.size() : 0) + " records");
        if (result != null && !result.isEmpty()) {
            System.out.println("First record: " + result.get(0));
        }
        
        return ResponseEntity.ok(result);
    }
}
