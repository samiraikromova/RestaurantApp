import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ReserveTableFrame extends JFrame {
    private final JButton[] tableButtons = new JButton[10]; // Assuming 10 tables

    // Database connection variables
    private Connection conn;
    private PreparedStatement stmt;

    public ReserveTableFrame() {
        setTitle("Reserve Table");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            // Set up the database connection
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/RestaurantApp", "root", "ikromova2006"); 
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JLabel instructionLabel = new JLabel("Click on a table to reserve/unreserve:", SwingConstants.CENTER);
        JButton backButton = new JButton("Back to Navigation");

        setLayout(new BorderLayout());
        add(instructionLabel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new GridLayout(2, 5));
        for (int i = 0; i < tableButtons.length; i++) {
            int tableNumber = i + 1;
            tableButtons[i] = new JButton("Table " + tableNumber + " (Free)");
            tableButtons[i].setBackground(Color.GREEN);
            tableButtons[i].addActionListener((ActionEvent e) -> openReservationForm(tableNumber));
            tablePanel.add(tableButtons[i]);
        }
        add(tablePanel, BorderLayout.CENTER);

        backButton.addActionListener(e -> {
            dispose();
            new NavigationFrame("User").setVisible(true); // Assuming a NavigationFrame exists
        });

        add(backButton, BorderLayout.SOUTH);
    }

    private void openReservationForm(int tableNumber) {
        JDialog reservationDialog = new JDialog(this, "Reserve Table " + tableNumber, true);
        reservationDialog.setSize(400, 400);
        reservationDialog.setLayout(new GridLayout(6, 2, 10, 10));

        // Form fields
        JLabel nameLabel = new JLabel("   Name:");
        JTextField nameField = new JTextField();

        JLabel phoneLabel = new JLabel("  Phone Number:");
        JTextField phoneField = new JTextField();

        JLabel peopleLabel = new JLabel("  Number of People:");
        JTextField peopleField = new JTextField();

        JLabel timeLabel = new JLabel("  Reservation Time:");
        JTextField timeField = new JTextField();

        JLabel parkingLabel = new JLabel("  Need Parking?");
        JCheckBox parkingCheckBox = new JCheckBox();

        // Add components to dialog
        reservationDialog.add(nameLabel);
        reservationDialog.add(nameField);

        reservationDialog.add(phoneLabel);
        reservationDialog.add(phoneField);

        reservationDialog.add(peopleLabel);
        reservationDialog.add(peopleField);

        reservationDialog.add(timeLabel);
        reservationDialog.add(timeField);

        reservationDialog.add(parkingLabel);
        reservationDialog.add(parkingCheckBox);

        // Reserve button
        JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener((ActionEvent e) -> {
            // Get user input
            String name = nameField.getText();
            String phone = phoneField.getText();
            String people = peopleField.getText();
            String time = timeField.getText();
            boolean needParking = parkingCheckBox.isSelected();

            // Validate fields
            if (name.isEmpty() || phone.isEmpty() || people.isEmpty() || time.isEmpty()) {
                JOptionPane.showMessageDialog(reservationDialog, "Please fill out all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Prepare the SQL statement for insertion
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String reservationTime = sdf.format(sdf.parse(time)); // Convert to proper format

                String sql = "INSERT INTO reservations (table_number, name, phone_number, num_people, reservation_time, parking_needed) VALUES (?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, tableNumber);
                stmt.setString(2, name);
                stmt.setString(3, phone);
                stmt.setInt(4, Integer.parseInt(people));
                stmt.setString(5, reservationTime);
                stmt.setBoolean(6, needParking);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(reservationDialog, "Reservation Confirmed!", "Reservation Success", JOptionPane.INFORMATION_MESSAGE);
                    // Update the table button status to Reserved
                    tableButtons[tableNumber - 1].setText("Table " + tableNumber + " (Reserved)");
                    tableButtons[tableNumber - 1].setBackground(Color.RED);
                } else {
                    JOptionPane.showMessageDialog(reservationDialog, "Failed to reserve the table.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(reservationDialog, "Error processing reservation.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            reservationDialog.dispose();
        });

        reservationDialog.add(new JLabel()); // Spacer
        reservationDialog.add(reserveButton);

        reservationDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        reservationDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReserveTableFrame::new);
    }

    // Make sure to close the database connection when done
    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
