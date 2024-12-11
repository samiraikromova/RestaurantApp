import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MenuFrame extends JFrame {
    private final List<MenuItem> selectedItems = new ArrayList<>();
    private final JLabel totalLabel = new JLabel("Total: $0.00");

    // Menu Item Class
    public static class MenuItem {
        private final String name;
        private final double price;
        private final String description;

        public MenuItem(String name, double price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return name + " - $" + price + ": " + description;
        }
    }

    // Menu Builder Class
    public static class MenuBuilder {
        private final List<MenuItem> menu = new ArrayList<>();

        public MenuBuilder addMenuItem(String name, double price, String description) {
            menu.add(new MenuItem(name, price, description));
            return this;
        }

        public List<MenuItem> build() {
            return menu;
        }
    }

    public MenuFrame() {
        setTitle("Menu Options");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton backButton = new JButton("Back to Navigation");
        JButton orderButton = new JButton("Order");

        // Create menu items using MenuBuilder
        List<MenuItem> menuItems = new MenuBuilder()
                .addMenuItem("Palov", 12.99, "Fried rice with a lot of meat")
                .addMenuItem("Manti", 8.99, "Juicy meat inside dough")
                .addMenuItem("Bo'girsoq", 10.99, "Fried dough")
                .addMenuItem("Achichu", 7.99, "Fresh garden salad with cucumber , tomato and onion")
                .addMenuItem("Limon choy", 2.99, "Limon tea")
                .build();

        // Panel for menu items
        JPanel menuPanel = new JPanel(new GridLayout(0, 1));
        for (MenuItem item : menuItems) {
            JCheckBox itemCheckBox = new JCheckBox(item.toString());
            itemCheckBox.addActionListener(e -> updateSelection(item, itemCheckBox.isSelected()));
            menuPanel.add(itemCheckBox);
        }

        // Bottom panel with total and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(orderButton, BorderLayout.CENTER);
        bottomPanel.add(backButton, BorderLayout.EAST);

        setLayout(new BorderLayout());
        add(new JLabel("HALAL AND APPROVED MENU", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(menuPanel), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Back button action
        backButton.addActionListener(e -> {
            dispose();
            new NavigationFrame("User").setVisible(true);
        });

        // Order button action
        orderButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Order placed for: " + getOrderSummary(),
                "Order Confirmation",
                JOptionPane.INFORMATION_MESSAGE));
    }

    private void updateSelection(MenuItem item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }
        totalLabel.setText("Total: $" + String.format("%.2f", calculateTotalPrice()));
    }

    private String getOrderSummary() {
        StringBuilder summary = new StringBuilder();
        for (MenuItem item : selectedItems) {
            summary.append(item.getName()).append(", ");
        }
        return summary.length() > 0 ? summary.substring(0, summary.length() - 2) : "No items selected";
    }

    private double calculateTotalPrice() {
        return selectedItems.stream().mapToDouble(MenuItem::getPrice).sum();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuFrame::new);
    }
}
