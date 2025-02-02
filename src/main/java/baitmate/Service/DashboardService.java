package baitmate.Service;

import baitmate.Repository.UserRepository;
import baitmate.Repository.PostRepository;
import baitmate.Repository.CatchRecordRepository;
import baitmate.Repository.FishingLocationRepository;
import baitmate.model.User;
import baitmate.model.Post;
import baitmate.model.CatchRecord;
import baitmate.model.FishingLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CatchRecordRepository catchRecordRepository;

    @Autowired
    private FishingLocationRepository fishingLocationRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Basic Statistics
    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalLocations", fishingLocationRepository.count());
        stats.put("totalPosts", postRepository.count());
        stats.put("totalCatches", catchRecordRepository.count());
        return stats;
    }

    // User Related
    public List<User> getRecentUsers() {
        return userRepository.findAll(
            PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "joinDate"))
        ).getContent();
    }

    public List<User> getMostActiveUsers() {
        return userRepository.findMostActiveUsers(PageRequest.of(0, 5));
    }

    // Post Related
    public List<Post> getRecentPosts() {
        return postRepository.findRecentPostsWithUser().stream()
            .limit(5)
            .toList();
    }

    public List<Post> getTopLikedPosts() {
        return postRepository.findTopLikedPosts(PageRequest.of(0, 5));
    }

    // Catch Record Related
    public List<CatchRecord> getRecentCatches() {
        return catchRecordRepository.findAll(
            PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time"))
        ).getContent();
    }

    public List<CatchRecord> getTopCatches() {
        return catchRecordRepository.findTopCatchesByWeight(PageRequest.of(0, 5));
    }

    // Location Related
    public List<FishingLocation> getPopularLocations() {
        return fishingLocationRepository.findPopularLocations(PageRequest.of(0, 5));
    }

    // Activity Summary
    public Map<String, Object> getActivitySummary() {
        Map<String, Object> summary = new HashMap<>();
        LocalDate now = LocalDate.now();
        LocalDate weekAgo = now.minusWeeks(1);

        // User stats use LocalDate
        summary.put("newUsersThisWeek", userRepository.countByJoinDateBetween(weekAgo, now));

        // Post stats use LocalDateTime
        LocalDateTime nowTime = LocalDateTime.now();
        LocalDateTime weekAgoTime = nowTime.minusWeeks(1);
        summary.put("newPostsThisWeek", postRepository.countByPostTimeBetween(weekAgoTime, nowTime));

        // Catch stats use formatted date strings
        String nowDate = now.format(DATE_FORMATTER);
        String weekAgoDate = weekAgo.format(DATE_FORMATTER);
        summary.put("newCatchesThisWeek", catchRecordRepository.countByTimeBetween(weekAgoDate, nowDate));
        
        return summary;
    }

    // Engagement Metrics
    public Map<String, Long> getEngagementMetrics() {
        Map<String, Long> metrics = new HashMap<>();
        metrics.put("totalLikes", postRepository.sumLikeCount());
        metrics.put("totalComments", postRepository.countComments());
        metrics.put("averageCatchesPerUser", catchRecordRepository.calculateAverageCatchesPerUser());
        return metrics;
    }

    public List<Map<String, Object>> getTodayMostCaughtFish() {
        String today = LocalDate.now().format(DATE_FORMATTER);
        List<Object[]> results = catchRecordRepository.findTodayMostCaughtFishWithLocation(today);
        
        List<Map<String, Object>> fishSummary = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> summary = new HashMap<>();
            summary.put("fishName", result[0]);
            summary.put("count", result[1]);
            summary.put("location", result[2]);
            fishSummary.add(summary);
        }
        return fishSummary;
    }
}
