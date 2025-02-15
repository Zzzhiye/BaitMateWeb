package baitmate.Service;

import baitmate.model.CatchRecord;
import baitmate.model.FishingLocation;
import baitmate.model.Post;
import baitmate.model.User;
import java.util.List;
import java.util.Map;

public interface DashboardService {
  Map<String, Long> getDashboardStats();

  List<User> getRecentUsers();

  List<User> getMostActiveUsers();

  List<Post> getRecentPosts();

  List<Post> getTopLikedPosts();

  List<CatchRecord> getRecentCatches();

  List<CatchRecord> getTopCatches();

  List<Map<String, Object>> getTopCatchesByMonth(int month, int year);

  List<FishingLocation> getPopularLocations();

  public List<Map<String, Object>> get10MostPopularLocations();

  Map<String, Object> getActivitySummary();

  Map<String, Long> getEngagementMetrics();

  List<Map<String, Object>> getTodayMostCaughtFish();

  Map<String, Object> getCatchStatistics();

  Map<String, Object> getTimelineData();
}
