package baitmate.Controller;

import baitmate.ImplementationMethod.DashboardServiceImpl;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

  @Autowired private DashboardServiceImpl dashboardServiceImpl;

  @GetMapping("/dashboard")
  public String getDashboard(Model model) {
    // Basic Statistics
    model.addAttribute("stats", dashboardServiceImpl.getDashboardStats());

    // Timeline Data
    model.addAttribute("timelineData", dashboardServiceImpl.getTimelineData());

    // Catch Statistics
    model.addAttribute("catchStats", dashboardServiceImpl.getCatchStatistics());

    // Today's Most Caught Fish
    List<Map<String, Object>> todayMostCaughtFish = dashboardServiceImpl.getTodayMostCaughtFish();
    model.addAttribute("todayMostCaughtFish", todayMostCaughtFish);

    // User Related
    model.addAttribute("recentUsers", dashboardServiceImpl.getRecentUsers());
    model.addAttribute("mostActiveUsers", dashboardServiceImpl.getMostActiveUsers());

    // Post Related
    model.addAttribute("recentPosts", dashboardServiceImpl.getRecentPosts());
    model.addAttribute("topLikedPosts", dashboardServiceImpl.getTopLikedPosts());

    // Catch Record Related
    model.addAttribute("recentCatches", dashboardServiceImpl.getRecentCatches());
    model.addAttribute("topCatches", dashboardServiceImpl.getTopCatches());

    // Location Related
    model.addAttribute("popularLocations", dashboardServiceImpl.getPopularLocations());

    // Activity Summary
    model.addAttribute("activitySummary", dashboardServiceImpl.getActivitySummary());

    // Engagement Metrics
    model.addAttribute("engagementMetrics", dashboardServiceImpl.getEngagementMetrics());

    return "dashboard";
  }

  @GetMapping("/report")
  public String getReport(Model model) {
    // Basic Statistics
    model.addAttribute("stats", dashboardServiceImpl.getDashboardStats());

    // User Related
    model.addAttribute("recentUsers", dashboardServiceImpl.getRecentUsers());
    model.addAttribute("mostActiveUsers", dashboardServiceImpl.getMostActiveUsers());

    // Post Related
    model.addAttribute("recentPosts", dashboardServiceImpl.getRecentPosts());
    model.addAttribute("topLikedPosts", dashboardServiceImpl.getTopLikedPosts());

    // Catch Record Related
    model.addAttribute("recentCatches", dashboardServiceImpl.getRecentCatches());
    model.addAttribute("topCatches", dashboardServiceImpl.getTopCatches());

    // Location Related
    model.addAttribute("popularLocations", dashboardServiceImpl.getPopularLocations());

    // Activity Summary
    model.addAttribute("activitySummary", dashboardServiceImpl.getActivitySummary());

    // Engagement Metrics
    model.addAttribute("engagementMetrics", dashboardServiceImpl.getEngagementMetrics());

    // Today's Most Caught Fish
    model.addAttribute("todayMostCaughtFish", dashboardServiceImpl.getTodayMostCaughtFish());

    return "report";
  }

  @GetMapping("/systemPerformance")
  public String getSystemPerformance(Model model) {

    return "grafana";
  }

  // API endpoint for AJAX updates
  @GetMapping("/api/dashboard-data")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> getDashboardData() {
    Map<String, Object> timelineData = dashboardServiceImpl.getTimelineData();
    Map<String, Object> catchStats = dashboardServiceImpl.getCatchStatistics();

    Map<String, Object> response = new HashMap<>();
    response.put("postActivity", timelineData.get("posts"));
    response.put("catchActivity", timelineData.get("catches"));
    response.put("todayMostCaughtFish", dashboardServiceImpl.getTodayMostCaughtFish());
    response.put("catchStats", catchStats);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/dashboard/stats")
  @ResponseBody
  public ResponseEntity<Map<String, Object>> getDashboardStats() {
    Map<String, Object> dashboardData =
        Map.of(
            "basicStats", dashboardServiceImpl.getDashboardStats(),
            "activitySummary", dashboardServiceImpl.getActivitySummary(),
            "engagementMetrics", dashboardServiceImpl.getEngagementMetrics());
    return ResponseEntity.ok(dashboardData);
  }

  @GetMapping("/api/top-catches")
  @ResponseBody
  public ResponseEntity<List<Map<String, Object>>> getTopCatches(
      @RequestParam(defaultValue = "0") int month, @RequestParam(defaultValue = "0") int year) {

    if (month == 0 || year == 0) {
      LocalDate now = LocalDate.now();
      month = now.getMonthValue();
      year = now.getYear();
    }

    System.out.println("Fetching top catches for month: " + month + ", year: " + year);
    List<Map<String, Object>> result = dashboardServiceImpl.getTopCatchesByMonth(month, year);
    System.out.println("Found " + (result != null ? result.size() : 0) + " records");
    if (result != null && !result.isEmpty()) {
      System.out.println("First record: " + result.get(0));
    }

    return ResponseEntity.ok(result);
  }
}
