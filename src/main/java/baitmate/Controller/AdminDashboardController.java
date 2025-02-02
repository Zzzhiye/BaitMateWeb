package baitmate.Controller;

import baitmate.Service.DashboardService;
import baitmate.model.User;
import baitmate.model.Post;
import baitmate.model.CatchRecord;
import baitmate.model.FishingLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
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

        return "admin/dashboard";
    }

    // API endpoint for AJAX updates
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
}
