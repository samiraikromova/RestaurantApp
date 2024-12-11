import javax.swing.*;
import java.awt.*;

public class NavigationFrame extends JFrame {
    public NavigationFrame(String username) {
        setTitle("Navigation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        JButton reserveTableButton = new JButton("Reserve Table");
        JButton menuButton = new JButton("Menu Options");

        setLayout(new GridLayout(3, 1));
        add(welcomeLabel);
        add(reserveTableButton);
        add(menuButton);

        // Action listener for Reserve Table button
        reserveTableButton.addActionListener(e -> {
            dispose(); // Close the current frame
            new ReserveTableFrame().setVisible(true); // Open ReserveTableFrame
        });

        // Action listener for Menu Options button
        menuButton.addActionListener(e -> {
            dispose(); // Close the current frame
            new MenuFrame().setVisible(true); // Open MenuFrame
        });
    }
}
