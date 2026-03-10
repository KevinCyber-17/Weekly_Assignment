import java.util.*;

class Plagiarism {

    private HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();
    private int N = 5;

    public void addDocument(String docId, String text) {
        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    private List<String> generateNgrams(String text) {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder gram = new StringBuilder();
            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }
            grams.add(gram.toString().trim());
        }

        return grams;
    }

    public void analyzeDocument(String docId) {
        List<String> targetNgrams = documentNgrams.get(docId);
        HashMap<String, Integer> matchCounts = new HashMap<>();

        for (String gram : targetNgrams) {
            if (ngramIndex.containsKey(gram)) {
                for (String otherDoc : ngramIndex.get(gram)) {
                    if (!otherDoc.equals(docId)) {
                        matchCounts.put(otherDoc,
                                matchCounts.getOrDefault(otherDoc, 0) + 1);
                    }
                }
            }
        }

        System.out.println("Extracted " + targetNgrams.size() + " n-grams");

        for (String doc : matchCounts.keySet()) {
            int matches = matchCounts.get(doc);
            double similarity = (matches * 100.0) / targetNgrams.size();

            System.out.println("Found " + matches + " matching n-grams with " + doc);
            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity > 50) {
                System.out.println("PLAGIARISM DETECTED!");
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {

        Plagiarism detector = new Plagiarism();

        String essay1 = "Artificial intelligence is transforming the world by enabling machines to learn from data and perform tasks";
        String essay2 = "Artificial intelligence is transforming the world by enabling machines to learn from data";
        String essay3 = "Cloud computing provides scalable infrastructure for modern applications and services";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);
        detector.addDocument("essay_123.txt", essay3);

        System.out.println("Analyzing essay_092.txt\n");

        detector.analyzeDocument("essay_092.txt");
    }
}