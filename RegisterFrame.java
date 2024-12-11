import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterFrame() {
        setTitle("Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create UI components
        JLabel usernameLabel = new JLabel("   Username:");
        JLabel passwordLabel = new JLabel("   Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        
        registerButton = new JButton("Register");
        backButton = new JButton("Back to Login");

        // Set layout and add components
        setLayout(new GridLayout(4, 2, 6, 6));

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(backButton);

        // Action listener for register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Registration successful!");
                    // Redirect to LoginFrame after successful registration
                    dispose();
                    new LoginFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "Registration failed. Try again.");
                }
            }
        });

        // Action listener for back button
        backButton.addActionListener(e -> {
            // Go back to the Login frame
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    // Method to register the user in the database
    private boolean registerUser(String username, String password) {
        // Assuming you have a database connection utility to get connection
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection(); // Get connection
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Connection failed
        }

        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

}
