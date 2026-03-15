import java.util.*;

public class App {
    public static void main(String[] args) {
        // [5] Reposicionar campos: Nome e e-mail encapsulados na classe Client
        Client client = new Client("João", "joao@email.com");
        Order order = new Order(client);
        
        // [2] Substituir primitivos por objetos: Itens agrupam produto, quantidade e preço
        order.addItem(new Item("Notebook", 1, 3500.0));
        order.addItem(new Item("Mouse", 2, 80.0));
        
        order.printInvoice();
        // [3] Ocultar delegados: App não acessa EmailService diretamente
        order.processConfirmation();
    }
}

// [5] Reposicionar campos em classes apropriadas
class Client {
    private String name;
    private String email;

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
}

// [2] Substituir primitivos por objetos com responsabilidade
class Item {
    private String product;
    private int quantity;
    private double price;

    public Item(String product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    // [4] Mover funções: Item calcula seu próprio subtotal
    public double getSubtotal() {
        return price * quantity;
    }

    @Override
    public String toString() {
        return quantity + "x " + product + " - R$" + price;
    }
}

class Order {
    // [1] Encapsular registros: Atributos privados e coleção protegida
    private Client client;
    private List<Item> items = new ArrayList<>();
    private double discountRate = 0.1;

    public Order(Client client) {
        this.client = client;
    }

    // [7] Proteger sistema contra inconsistências: Validação de entrada
    public void addItem(Item item) {
        if (item == null) throw new IllegalArgumentException("Item não pode ser nulo");
        items.add(item);
    }

    public void printInvoice() {
        System.out.println("Cliente: " + client.getName());
        for (Item item : items) {
            System.out.println(item.toString());
        }
        
        // [6] Substituir código embutido por chamadas auxiliares
        double subtotal = calculateSubtotal();
        double discount = DiscountPolicy.calculateDiscount(subtotal, discountRate);
        double totalFinal = subtotal - discount;

        printSummary(subtotal, discount, totalFinal);
    }

    // [6] Extração de método para cálculo de total
    private double calculateSubtotal() {
        double total = 0;
        for (Item item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    // [6] Extração de método para impressão formatada
    private void printSummary(double subtotal, double discount, double totalFinal) {
        System.out.println("Subtotal: R$" + subtotal);
        System.out.println("Desconto: R$" + discount);
        System.out.println("Total final: R$" + totalFinal);
    }

    // [3] Ocultar delegados: Encapsula a chamada ao serviço de e-mail
    public void processConfirmation() {
        sendEmail();
    }

    private void sendEmail() {
        EmailService.sendEmail(client.getEmail(), "Pedido recebido! Obrigado pela compra.");
    }
}

class EmailService {
    public static void sendEmail(String to, String message) {
        System.out.println("Enviando e-mail para " + to + ": " + message);
    }
}

class DiscountPolicy {
    public static double calculateDiscount(double amount, double rate) {
        return amount * rate;
    }
}
