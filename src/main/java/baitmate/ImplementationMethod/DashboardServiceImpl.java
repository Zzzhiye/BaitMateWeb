package baitmate.ImplementationMethod;

import baitmate.Repository.CatchRecordRepository;
import baitmate.Repository.FishingLocationRepository;
import baitmate.Repository.PostRepository;
import baitmate.Repository.UserRepository;
import baitmate.Service.DashboardService;
import baitmate.model.CatchRecord;
import baitmate.model.FishingLocation;
import baitmate.model.Post;
import baitmate.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {

  @Autowired private UserRepository userRepository;

  @Autowired private PostRepository postRepository;

  @Autowired private CatchRecordRepository catchRecordRepository;

  @Autowired private FishingLocationRepository fishingLocationRepository;

  private static final Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);
  private static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
    return userRepository
        .findAll(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "joinDate")))
        .getContent();
  }

  public List<User> getMostActiveUsers() {
    return userRepository.findMostActiveUsers(PageRequest.of(0, 5));
  }

  // Post Related
  public List<Post> getRecentPosts() {
    return postRepository.findRecentPostsWithUser().stream().limit(5).toList();
  }

  public List<Post> getTopLikedPosts() {
    return postRepository.findTopLikedPosts(PageRequest.of(0, 10));
  }

  // Catch Record Related
  public List<CatchRecord> getRecentCatches() {
    return catchRecordRepository
        .findAll(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "time")))
        .getContent();
  }

  public List<CatchRecord> getTopCatches() {
    return catchRecordRepository.findTopCatchesByWeight(PageRequest.of(0, 5));
  }

  public List<Map<String, Object>> getTopCatchesByMonth(int month, int year) {
    logger.debug("Getting top catches for month: {}, year: {}", month, year);
    List<Object[]> results = catchRecordRepository.findTopCatchesByMonth(month, year);
    logger.debug("Raw results size: {}", results != null ? results.size() : 0);

    List<Map<String, Object>> catchSummary = new ArrayList<>();

    if (results != null) {
      for (Object[] result : results) {
        logger.debug("Processing result: {}", java.util.Arrays.toString(result));
        Map<String, Object> summary = new HashMap<>();

        // Create user object
        Map<String, Object> user = new HashMap<>();
        user.put("username", result[0]);
        summary.put("user", user);

        summary.put("fishType", result[1]);
        summary.put("weight", result[2]);

        // Create fishingLocation object
        Map<String, Object> fishingLocation = new HashMap<>();
        fishingLocation.put("locationName", result[3]);
        summary.put("fishingLocation", fishingLocation);

        summary.put("catchTime", result[4]);
        catchSummary.add(summary);
      }
    }

    logger.debug("Returning {} formatted records", catchSummary.size());
    return catchSummary;
  }

  // Location Related
  public List<FishingLocation> getPopularLocations() {
    return fishingLocationRepository.findPopularLocations(PageRequest.of(0, 10));
  }

  public List<Map<String, Object>> get10MostPopularLocations() {
    List<Object[]> results = fishingLocationRepository.findTop10LocationsByTotalCatches();

    return results.stream().map(result -> {
      Map<String, Object> locationData = new HashMap<>();
      locationData.put("mostPopularLocationName", result[1]); // location_name
      locationData.put("mostCatchCount", result[2]); // catch_count
      return locationData;
    }).collect(Collectors.toList());
  }

  // Activity Summary
  public Map<String, Object> getActivitySummary() {
    Map<String, Object> summary = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime weekAgo = now.minusWeeks(1);

    // User stats use LocalDate
    summary.put(
        "newUsersThisWeek",
        userRepository.countByJoinDateBetween(weekAgo.toLocalDate(), now.toLocalDate()));

    // Post stats use LocalDateTime
    summary.put("newPostsThisWeek", postRepository.countByPostTimeBetween(weekAgo, now));

    // Catch stats use formatted date strings
    String nowDate = now.format(DATE_FORMATTER);
    String weekAgoDate = weekAgo.format(DATE_FORMATTER);
    summary.put(
        "newCatchesThisWeek", catchRecordRepository.countByTimeBetween(weekAgoDate, nowDate));

    return summary;
  }

  // Engagement Metrics
  public Map<String, Long> getEngagementMetrics() {
    Map<String, Long> metrics = new HashMap<>();
    metrics.put("totalLikes", postRepository.sumLikeCount());
    metrics.put("totalComments", postRepository.countComments());

    // Double တန်ဖိုးကို Long အဖြစ် ပြောင်းပြီးမှ Map ထဲ ထည့်ပါမယ်
    Double avgCatches = catchRecordRepository.calculateAverageCatchesPerUser();
    metrics.put("averageCatchesPerUser", avgCatches != null ? Math.round(avgCatches) : 0L);

    return metrics;
  }

  public List<Map<String, Object>> getTodayMostCaughtFish() {
    // Get current time in system timezone
    LocalDateTime now = LocalDateTime.now();
    String today = now.format(DATE_FORMATTER);

    logger.info("Fetching most caught fish for date: {}", today);

    List<Object[]> results = catchRecordRepository.findTodayMostCaughtFishWithLocation(today);
    logger.info("Found {} results for most caught fish", results.size());

    List<Map<String, Object>> fishSummary = new ArrayList<>();
    for (Object[] result : results) {
      Map<String, Object> summary = new HashMap<>();
      summary.put("fishName", result[0]);
      summary.put("count", result[1]);
      summary.put("location", result[2]);
      fishSummary.add(summary);
      logger.debug("Added fish summary: {}", summary);
    }
    return fishSummary;
  }

  public Map<String, Object> getCatchStatistics() {
    List<Object[]> locationStats = catchRecordRepository.findTodayCatchesByLocation();
    List<Object[]> speciesStats = catchRecordRepository.findTodayCatchesBySpecies();

    List<String> locationLabels = new ArrayList<>();
    List<Long> locationCounts = new ArrayList<>();
    for (Object[] stat : locationStats) {
      locationLabels.add((String) stat[0]);
      locationCounts.add(((Number) stat[1]).longValue());
    }

    List<String> speciesLabels = new ArrayList<>();
    List<Long> speciesCounts = new ArrayList<>();
    for (Object[] stat : speciesStats) {
      speciesLabels.add((String) stat[0]);
      speciesCounts.add(((Number) stat[1]).longValue());
    }

    Map<String, Object> statistics = new HashMap<>();
    statistics.put("locationLabels", locationLabels);
    statistics.put("locationCounts", locationCounts);
    statistics.put("speciesLabels", speciesLabels);
    statistics.put("speciesCounts", speciesCounts);

    return statistics;
  }

  // Timeline Data
  public Map<String, Object> getTimelineData() {
    Map<String, Object> timelineData = new HashMap<>();

    // Initialize 24 hours with 0 counts
    List<Integer> postCounts = new ArrayList<>(Collections.nCopies(24, 0));
    List<Integer> catchCounts = new ArrayList<>(Collections.nCopies(24, 0));

    // Get post timeline data
    List<Object[]> postData = postRepository.findHourlyPostCountForToday();
    logger.info("Found {} hourly post records", postData.size());

    for (Object[] data : postData) {
      int hour = ((Number) data[0]).intValue();
      int count = ((Number) data[1]).intValue();
      postCounts.set(hour, count);
      logger.debug("Hour {}: {} posts", hour, count);
    }

    // Get catch timeline data
    List<Object[]> catchData = catchRecordRepository.findHourlyCatchCountForToday();
    logger.info("Found {} hourly catch records", catchData.size());

    for (Object[] data : catchData) {
      int hour = ((Number) data[0]).intValue();
      int count = ((Number) data[1]).intValue();
      catchCounts.set(hour, count);
      logger.debug("Hour {}: {} catches", hour, count);
    }

    timelineData.put("posts", postCounts);
    timelineData.put("catches", catchCounts);

    return timelineData;
  }
}
