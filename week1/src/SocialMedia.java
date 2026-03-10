import java.util.*;

class SocialMedia {

    private HashMap<String, Integer> users;

    private HashMap<String, Integer> attemptFrequency;

    public SocialMedia() {
        users = new HashMap<>();
        attemptFrequency = new HashMap<>();
    }

    public void addUser(String username, int userId) {
        users.put(username, userId);
    }

    public boolean checkAvailability(String username) {

        attemptFrequency.put(username,
                attemptFrequency.getOrDefault(username, 0) + 1);

        if (users.containsKey(username)) {
            return false;
        }

        return true;
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            String suggestion = username + i;

            if (!users.containsKey(suggestion)) {
                suggestions.add(suggestion);
            }
        }

        String alt = username.replace("_", ".");
        if (!users.containsKey(alt)) {
            suggestions.add(alt);
        }

        return suggestions;
    }

    public String getMostAttempted() {

        String mostAttempted = "";
        int max = 0;

        for (String user : attemptFrequency.keySet()) {

            int count = attemptFrequency.get(user);

            if (count > max) {
                max = count;
                mostAttempted = user;
            }
        }

        return mostAttempted + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        SocialMedia system = new SocialMedia();

        system.addUser("john_doe", 101);
        system.addUser("admin", 1);
        system.addUser("kevin", 102);

        System.out.println("john_doe available? " +
                system.checkAvailability("john_doe"));

        System.out.println("jane_smith available? " +
                system.checkAvailability("jane_smith"));

        System.out.println("Suggestions for john_doe: "
                + system.suggestAlternatives("john_doe"));

        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        System.out.println("Most attempted username: "
                + system.getMostAttempted());
    }
}