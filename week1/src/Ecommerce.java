import java.util.*;

class Ecommerce {


    private HashMap<String, Integer> stock;


    private HashMap<String, Queue<Integer>> waitingList;

    public Ecommerce() {
        stock = new HashMap<>();
        waitingList = new HashMap<>();
    }


    public void addProduct(String productId, int quantity) {
        stock.put(productId, quantity);
        waitingList.put(productId, new LinkedList<>());
    }


    public int checkStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }


    public synchronized String purchaseItem(String productId, int userId) {

        int available = stock.getOrDefault(productId, 0);

        if (available > 0) {

            stock.put(productId, available - 1);

            return "Success: User " + userId +
                    " purchased item. Remaining stock: " +
                    (available - 1);
        }
        else {

            Queue<Integer> queue = waitingList.get(productId);
            queue.add(userId);

            return "Stock unavailable. User " + userId +
                    " added to waiting list. Position #" +
                    queue.size();
        }
    }


    public void showWaitingList(String productId) {

        Queue<Integer> queue = waitingList.get(productId);

        System.out.println("Waiting List: " + queue);
    }

    public static void main(String[] args) {

        Ecommerce system =
                new Ecommerce();


        system.addProduct("IPHONE15_256GB", 5);

        System.out.println("Stock Available: " +
                system.checkStock("IPHONE15_256GB"));

        for (int i = 1; i <= 8; i++) {
            System.out.println(
                    system.purchaseItem("IPHONE15_256GB", 1000 + i)
            );
        }

        system.showWaitingList("IPHONE15_256GB");
    }
}