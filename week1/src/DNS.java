import java.util.*;

class DNS {

    static class DNSEntry {
        String domain;
        String ipAddress;
        long expiryTime;

        DNSEntry(String domain, String ipAddress, int ttlSeconds) {
            this.domain = domain;
            this.ipAddress = ipAddress;
            this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    private LinkedHashMap<String, DNSEntry> cache;

    private int maxSize;
    private int hits = 0;
    private int misses = 0;

    public DNS(int maxSize) {
        this.maxSize = maxSize;

        cache = new LinkedHashMap<String, DNSEntry>(16, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > DNS.this.maxSize;
            }
        };
    }


    public synchronized String resolve(String domain) {

        long startTime = System.nanoTime();

        DNSEntry entry = cache.get(domain);

        if (entry != null && !entry.isExpired()) {
            hits++;
            long time = System.nanoTime() - startTime;

            System.out.println("Cache HIT → " + entry.ipAddress +
                    " (Lookup time: " + time / 1000000.0 + " ms)");
            return entry.ipAddress;
        }

        if (entry != null && entry.isExpired()) {
            cache.remove(domain);
            System.out.println("Cache EXPIRED → Removing entry");
        }

        misses++;

        String newIp = queryUpstreamDNS(domain);

        cache.put(domain, new DNSEntry(domain, newIp, 5));

        System.out.println("Cache MISS → Query upstream → " + newIp);

        return newIp;
    }

    private String queryUpstreamDNS(String domain) {
        Random r = new Random();
        return "172.217.14." + (100 + r.nextInt(50));
    }

    public void cleanExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, DNSEntry> entry = it.next();

            if (entry.getValue().isExpired()) {
                it.remove();
            }
        }
    }

    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : (hits * 100.0 / total);

        System.out.println("Cache Hits: " + hits);
        System.out.println("Cache Misses: " + misses);
        System.out.println("Hit Rate: " + hitRate + "%");
    }

    public static void main(String[] args) throws Exception {

        DNS dnsCache = new DNS(3);

        dnsCache.resolve("google.com");
        dnsCache.resolve("google.com");

        Thread.sleep(6000);

        dnsCache.resolve("google.com");

        dnsCache.resolve("facebook.com");
        dnsCache.resolve("amazon.com");
        dnsCache.resolve("youtube.com");

        dnsCache.getCacheStats();
    }
}