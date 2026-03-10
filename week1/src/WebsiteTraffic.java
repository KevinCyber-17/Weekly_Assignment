import java.util.*;

class WebsiteTraffic {

    static class Event {
        String url;
        String userId;
        String source;

        Event(String url, String userId, String source) {
            this.url = url;
            this.userId = userId;
            this.source = source;
        }
    }

    private HashMap<String, Integer> pageViews = new HashMap<>();
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
    private HashMap<String, Integer> trafficSources = new HashMap<>();

    public void processEvent(Event e) {

        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);

        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());
        uniqueVisitors.get(e.url).add(e.userId);

        trafficSources.put(e.source, trafficSources.getOrDefault(e.source, 0) + 1);
    }

    public void getDashboard() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageViews.entrySet());

        System.out.println("Top Pages:");

        int count = 0;

        while (!pq.isEmpty() && count < 10) {

            Map.Entry<String, Integer> entry = pq.poll();

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println((count + 1) + ". " + url + " - " + views + " views (" + unique + " unique)");

            count++;
        }

        System.out.println("\nTraffic Sources:");

        int total = 0;

        for (int c : trafficSources.values())
            total += c;

        for (String src : trafficSources.keySet()) {

            int c = trafficSources.get(src);

            double percent = (c * 100.0) / total;

            System.out.println(src + ": " + String.format("%.2f", percent) + "%");
        }
    }

    public static void main(String[] args) throws Exception {

        WebsiteTraffic analytics = new WebsiteTraffic();

        analytics.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new Event("/sports/championship", "user_222", "direct"));
        analytics.processEvent(new Event("/sports/championship", "user_333", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_789", "google"));
        analytics.processEvent(new Event("/tech/ai-news", "user_444", "direct"));
        analytics.processEvent(new Event("/tech/ai-news", "user_555", "google"));

        analytics.getDashboard();
    }
}