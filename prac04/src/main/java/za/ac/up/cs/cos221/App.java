package za.ac.up.cs.cos221;

import org.mariadb.jdbc.export.ExceptionFactory;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Hello world!
 *
 */
public class App {
    private JFrame frame;
    private JTextField textField;
    private JTable table;
    private JTabbedPane tp = new JTabbedPane();

    private static String driver = "jdbc:mariadb";
    private static String host = "localhost";
    private static int port = 3306;
    private static String database = "u19264047_sakila";
    private static String username = "admin";
    private static String password = "password";

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    App window = new App();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     * 
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public App() throws SQLException, ClassNotFoundException {
        initialize();
    }

    private void initialize() throws SQLException, ClassNotFoundException {
        String url = new StringBuilder()
                .append(driver).append("://")
                .append(host).append(":").append(port).append("/")
                .append(database)
                .toString();
        frame = new JFrame();
        frame.setTitle("Prac04");
        frame.setBounds(100, 100, 1000, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Staff

        JPanel staff = new JPanel();
        tp.add("Staff", staff);
        tp.setBounds(0, 0, 900, 500);
        frame.getContentPane().add(tp);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 11, 500, 500);
        staff.add(scrollPane);

        String data[][] = getStaffTable(url);
        String column[] = { "First Name", "Last Name", "Address", "Address2", "District", "City", "Postal Code",
                "Phone Number", "Store ID", "Active" };

        MyTableModel tm = new MyTableModel(column, data);
        final TableRowSorter trs = new TableRowSorter(tm);
        table = new JTable();
        table.setModel(tm);
        table.setRowSorter(trs);
        for (int i = 0; i < 10; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
        scrollPane.setViewportView(table);

        JLabel lblNewLabel = new JLabel("Filter:");
        lblNewLabel.setBounds(510, 11, 46, 14);
        staff.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(550, 9, 131, 20);
        staff.add(textField);
        textField.setColumns(10);

        JButton FilterButton = new JButton("Filter");
        FilterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchText = textField.getText();
                trs.setRowFilter(new myRowFilter(searchText));
            }
        });
        FilterButton.setBounds(510, 37, 131, 23);
        staff.add(FilterButton);

        // Films

        JPanel films = new JPanel();
        tp.add("Films", films);
        JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setBounds(10, 11, 500, 300);
        films.add(scrollPane1);

        String filmdata[][] = getFilmsTable(url);
        String fcolumn[] = { "Ttile", "Desctiption", "Release Year", "Language", "Original Language", "Rental Duration",
                "Rental Rate", "Length", "Replacements Cost", "Rating" };

        final MyTableModel ftm = new MyTableModel(fcolumn, filmdata);
        final TableRowSorter ftrs = new TableRowSorter(ftm);
        final JTable fTable = new JTable();
        fTable.setModel(ftm);
        fTable.setRowSorter(ftrs);
        for (int i = 0; i < 10; i++) {
            fTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
        scrollPane1.setViewportView(fTable);
        JButton addFilm = new JButton("Add Film");
        addFilm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame add = new JFrame();
                add.setTitle("Add Film");
                add.setBounds(150, 150, 500, 500);
                add.getContentPane().setLayout(null);

                JLabel labels[] = new JLabel[10];
                final JTextField text[] = new JTextField[10];
                final String[] col = { "Title: ", "Description: ", "Release Year: ", "Laguage: ", "Rental Duration: ",
                        "Tental Rare: ", "Length: ", "Replacement Cost: ", "Rating: ", "Special Features: " };
                for (int i = 0; i < 10; i++) {
                    labels[i] = new JLabel(col[i]);
                    labels[i].setBounds(10, 10 + 20 * i, 120, 20);
                    add.add(labels[i]);
                    text[i] = new JTextField();
                    text[i].setBounds(130, 10 + 20 * i, 120, 20);
                    text[i].setText("");
                    add.add(text[i]);
                }

                JButton btnAdd = new JButton("Add Film");
                btnAdd.setBounds(10, 220, 120, 40);
                add.add(btnAdd);
                btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String url = new StringBuilder()
                                .append(driver).append("://")
                                .append(host).append(":").append(port).append("/")
                                .append(database)
                                .toString();
                        if ((text[0].getText().isBlank()) || (text[3].getText().isBlank())
                                || (text[4].getText().isBlank()) || (text[5].getText().isBlank())
                                || (text[7].getText().isBlank()))
                            JOptionPane.showMessageDialog(frame,
                                    "Title, Language, rental duration, rental rate and replacement cost can't be empty");
                        else {
                            String query = "INSERT INTO film (title";
                            String val = "VALUE ('" + text[0].getText();
                            if (!text[1].getText().isBlank()) {
                                query += ", description";
                                val += "', '" + text[1].getText();
                            }
                            if (!text[2].getText().isBlank()) {
                                query += ", release_year";
                                val += "', '" + text[2].getText();
                            }
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                try (Statement statement = connection.createStatement()) {
                                    try (ResultSet language = statement.executeQuery("SELECT * FROM language")) {
                                        for (language.first(); !language.isAfterLast(); language.next()) {
                                            if (language.getString(2).equals(text[3].getText())) {
                                                break;
                                            }
                                        }
                                        if (language.isAfterLast()) {
                                            try (ResultSet n = statement
                                                    .executeQuery("INSERT INTO language (name) value ('"
                                                            + text[3].getText() + "')")) {
                                                for (n.first(); !n.isAfterLast(); n.next()) {
                                                    if (n.getString(2).equals(text[3].getText())) {
                                                        query += ", language_id";
                                                        val += "', '" + Integer.toString(n.getInt(1));
                                                    }
                                                }
                                            }
                                        } else {
                                            query += ", language_id";
                                            val += "', '" + Integer.toString(language.getInt(1));
                                        }
                                    }
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            if (!text[4].getText().isBlank()) {
                                query += ", rental_duration";
                                val += "', '" + text[4].getText();
                            }
                            if (!text[5].getText().isBlank()) {
                                query += ", rental_rate";
                                val += "', '" + text[5].getText();
                            }
                            if (!text[6].getText().isBlank()) {
                                query += ", length";
                                val += "', '" + text[6].getText();
                            }
                            if (!text[7].getText().isBlank()) {
                                query += ", replacement_cost";
                                val += "', '" + text[7].getText();
                            }
                            if (!text[8].getText().isBlank()) {
                                query += ", rating";
                                val += "', '" + text[8].getText();
                            }
                            if (!text[9].getText().isBlank()) {
                                query += ", special_features";
                                val += "', '" + text[9].getText();
                            }
                            query += ")";
                            val += "')";
                            query += val;
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                try (Statement statement = connection.createStatement()) {
                                    try (ResultSet n = statement.executeQuery(query)) {

                                    }
                                }
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            try {
                                String[][] a = getFilmsTable(url);
                                MyTableModel nftm = new MyTableModel(col, a);
                                fTable.setModel(nftm);
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            add.dispose();
                        }
                    }
                });

                add.setVisible(true);
            }
        });
        addFilm.setBounds(510, 37, 131, 23);
        films.add(addFilm);

        // Invetory

        JPanel Inventory = new JPanel();
        tp.add("Inventory", Inventory);
        JScrollPane scrollPane2 = new JScrollPane();
        scrollPane2.setBounds(10, 11, 500, 300);
        Inventory.add(scrollPane2);

        String Invdata[][] = getInvTable(url);
        String Icolumn[] = { "Store", "Genre", "Count" };

        final MyTableModel itm = new MyTableModel(Icolumn, Invdata);
        final TableRowSorter itrs = new TableRowSorter(itm);
        final JTable iTable = new JTable();
        iTable.setModel(itm);
        iTable.setRowSorter(itrs);
        for (int i = 0; i < 3; i++) {
            iTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
        scrollPane2.setViewportView(iTable);

        // Clients

        JPanel Client = new JPanel();
        tp.add("Client", Client);
        JScrollPane scrollPane3 = new JScrollPane();
        scrollPane3.setBounds(10, 11, 500, 500);
        Client.add(scrollPane3);

        String cdata[][] = getClientTable(url);
        String ccolumn[] = { "ID", "First Name", "Last Name", "Email", "Address", "Address2", "District", "City",
                "Postal Code", "Phone Number", "Store ID", "Active", "Create Date" };

        final MyTableModel ctm = new MyTableModel(ccolumn, cdata);
        final TableRowSorter ctrs = new TableRowSorter(ctm);
        final JTable cTable = new JTable();
        cTable.setModel(ctm);
        cTable.setRowSorter(ctrs);
        for (int i = 0; i < 13; i++) {
            cTable.getColumnModel().getColumn(i).setPreferredWidth(200);
        }
        scrollPane3.setViewportView(cTable);

        JButton addClient = new JButton("Add Client");
        addClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame add = new JFrame();
                add.setTitle("Add Client");
                add.setBounds(150, 150, 500, 500);
                add.getContentPane().setLayout(null);

                JLabel labels[] = new JLabel[10];
                final JTextField text[] = new JTextField[10];
                final String[] col = { "First Name: ", "Last Name: ", "Email: ", "Address: ", "Address2: ",
                        "District: ", "City: ", "Postal Code: ", "Phone Number: ", "Store ID: " };
                for (int i = 0; i < 10; i++) {
                    labels[i] = new JLabel(col[i]);
                    labels[i].setBounds(10, 10 + 20 * i, 120, 20);
                    add.add(labels[i]);
                    text[i] = new JTextField();
                    text[i].setBounds(130, 10 + 20 * i, 120, 20);
                    text[i].setText("");
                    add.add(text[i]);
                }

                JButton btnAdd = new JButton("Add Client");
                btnAdd.setBounds(10, 220, 120, 40);
                add.add(btnAdd);
                btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String url = new StringBuilder()
                                .append(driver).append("://")
                                .append(host).append(":").append(port).append("/")
                                .append(database)
                                .toString();
                        if ((text[0].getText().isBlank()) || (text[1].getText().isBlank())
                                || (text[2].getText().isBlank()) || (text[3].getText().isBlank())
                                || (text[5].getText().isBlank()) || (text[6].getText().isBlank())
                                || (text[7].getText().isBlank()) || (text[8].getText().isBlank())
                                || (text[9].getText().isBlank()))
                            JOptionPane.showMessageDialog(frame,
                                    "First Name, Last Name, Address, District, City, Postal Code, Phone Number and Store ID can't be empty");
                        else {
                            String query = "INSERT INTO customer (first_name, last_name";
                            String val = "VALUE ('" + text[0].getText() + "', '" + text[1].getText();
                            if (!text[2].getText().isBlank()) {
                                query += ", email";
                                val += "', '" + text[2].getText();
                            }
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                try (Statement statement = connection.createStatement()) {
                                    try (ResultSet language = statement.executeQuery("SELECT * FROM address")) {
                                        for (language.first(); !language.isAfterLast(); language.next()) {
                                            if (language.getString(2).equals(text[3].getText())
                                                    && language.getString(4).equals(text[5].getText())
                                                    && language.getString(6).equals(text[7].getText())) {
                                                break;
                                            }
                                        }
                                        if (language.isAfterLast()) {
                                            String q = "INSERT INTO address (address, ";
                                            if (!text[4].getText().isBlank())
                                                q += "address2, ";
                                            q += "district, city_id, postal_code, phone) VALUE ('";
                                            q += text[3].getText() + "', '";
                                            if (!text[4].getText().isBlank())
                                                q += text[4].getText() + "', '";
                                            q += text[5].getText() + "', '";
                                            Integer i;
                                            try (ResultSet c = statement.executeQuery("SELECT * FROM city")) {
                                                for (c.first(); !c.isAfterLast(); c.next())
                                                    if (c.getString(2).equals(text[6].getText()))
                                                        break;
                                                if (c.isAfterLast())
                                                    try (ResultSet cunt = statement
                                                            .executeQuery("INSERT INTO city (city, country_id)VALUE ('"
                                                                    + text[6].getText() + "', 1)")) {
                                                        for (cunt.first(); !cunt.isAfterLast(); cunt.next())
                                                            if (cunt.getString(2).equals(text[6].getText()))
                                                                break;
                                                        i = cunt.getInt(1);
                                                    }
                                                else {
                                                    i = c.getInt(1);
                                                }
                                            }
                                            q += Integer.toString(i) + "', '";
                                            q += text[7].getText() + "', '";
                                            q += text[8].getText() + "')";
                                            try (ResultSet n = statement.executeQuery(q)) {
                                                for (n.first(); !n.isAfterLast(); n.next()) {
                                                    if (n.getString(2).equals(text[3].getText())) {
                                                        query += ", address_id";
                                                        val += "', '" + Integer.toString(n.getInt(1));
                                                    }
                                                }
                                            }
                                        } else {
                                            query += ", address_id";
                                            val += "', '" + Integer.toString(language.getInt(1));
                                        }
                                    }
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            query += ", active";
                            val += "', 1";
                            query += ", create_date";
                            java.util.Date d = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmMss");
                            val += ", " + dateFormat.format(d);
                            query += ", store_id";
                            val += ", " + text[9].getText();
                            query += ") ";
                            val += ")";
                            query += val;
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                try (Statement statement = connection.createStatement()) {
                                    try (ResultSet n = statement.executeQuery(query)) {

                                    }
                                }
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            try {
                                String[][] a = getClientTable(url);
                                MyTableModel nftm = new MyTableModel(col, a);
                                cTable.setModel(nftm);
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            add.dispose();
                        }
                    }
                });

                add.setVisible(true);
            }
        });
        addClient.setBounds(10, 10, 131, 23);
        Client.add(addClient);

        JButton deleteClient = new JButton("Delete Client");
        deleteClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame add = new JFrame();
                add.setTitle("Delete Client");
                add.setBounds(150, 150, 500, 500);
                add.getContentPane().setLayout(null);

                JLabel labels = new JLabel("ID: ");
                labels.setBounds(10, 10, 120, 20);
                add.add(labels);
                final JTextField text = new JTextField();
                text.setBounds(130, 10, 120, 20);
                text.setText("");
                add.add(text);

                JButton btnAdd = new JButton("Delete Client");
                btnAdd.setBounds(10, 220, 120, 40);
                add.add(btnAdd);
                btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String url = new StringBuilder()
                                .append(driver).append("://")
                                .append(host).append(":").append(port).append("/")
                                .append(database)
                                .toString();
                        if ((text.getText().isBlank()))
                            JOptionPane.showMessageDialog(frame, "ID can't be empty");
                        else {
                            String query = "DELETE FROM ";
                            String val = " WHERE customer_id = " + text.getText();
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                try (Statement statement = connection.createStatement()) {
                                    try (ResultSet n = statement.executeQuery(query + "customer" + val)) {

                                    }
                                }
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            try {
                                String col[] = { "ID", "First Name", "Last Name", "Email", "Address", "Address2",
                                        "District", "City", "Postal Code", "Phone Number", "Store ID", "Active",
                                        "Create Date" };
                                String[][] a = getClientTable(url);
                                MyTableModel nftm = new MyTableModel(col, a);
                                cTable.setModel(nftm);
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            add.dispose();
                        }
                    }
                });

                add.setVisible(true);
            }
        });
        deleteClient.setBounds(10, 10, 131, 23);
        Client.add(deleteClient);

        JButton UpdateClient = new JButton("Update Client");
        UpdateClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JFrame add = new JFrame();
                add.setTitle("Update Client");
                add.setBounds(150, 150, 500, 500);
                add.getContentPane().setLayout(null);

                JLabel labels = new JLabel("ID: ");
                labels.setBounds(10, 10, 120, 20);
                add.add(labels);
                final JTextField text = new JTextField();
                text.setBounds(130, 10, 120, 20);
                text.setText("");
                add.add(text);

                String[] optionsToChoose = { "store_id", "first_name", "last_name", "email", "address", "address2",
                        "district", "city_name", "postal_code", "phone", "store_id", "active" };

                final JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
                jComboBox.setBounds(10, 30, 120, 20);
                add.add(jComboBox);
                final JTextField text1 = new JTextField();
                text1.setBounds(130, 30, 120, 20);
                text1.setText("");
                add.add(text1);

                JButton btnAdd = new JButton("Update Client");
                btnAdd.setBounds(10, 220, 120, 40);
                add.add(btnAdd);
                btnAdd.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String url = new StringBuilder()
                                .append(driver).append("://")
                                .append(host).append(":").append(port).append("/")
                                .append(database)
                                .toString();
                        if ((text.getText().isBlank()) || text1.getText().isBlank())
                            JOptionPane.showMessageDialog(frame, "Neither field can be empty");
                        else {
                            String query = "UPDATE customer ";
                            String val = "SET " + jComboBox.getItemAt(jComboBox.getSelectedIndex()) + " = "
                                    + text1.getText();
                            String where = " WHERE customer_id = " + text.getText();
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                try (Statement statement = connection.createStatement()) {
                                    try (ResultSet n = statement.executeQuery(query + val + where)) {

                                    }
                                }
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            try {
                                String col[] = { "ID", "First Name", "Last Name", "Email", "Address", "Address2",
                                        "District", "City", "Postal Code", "Phone Number", "Store ID", "Active",
                                        "Create Date" };
                                String[][] a = getClientTable(url);
                                MyTableModel nftm = new MyTableModel(col, a);
                                cTable.setModel(nftm);
                            } catch (SQLException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                            add.dispose();
                        }
                    }
                });

                add.setVisible(true);
            }
        });
        UpdateClient.setBounds(10, 10, 131, 23);
        Client.add(UpdateClient);

        frame.setVisible(true);
    }

    private String[][] getClientTable(String url) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet cus = statement.executeQuery("SELECT * FROM customer")) {
                    try (Statement statement1 = connection.createStatement()) {
                        try (ResultSet address = statement1.executeQuery("SELECT * FROM address")) {
                            try (Statement statement3 = connection.createStatement()) {
                                try (ResultSet city = statement3.executeQuery("SELECT * FROM city")) {
                                    cus.last();
                                    int i = cus.getRow();
                                    String data[][] = new String[i][];
                                    cus.first();

                                    for (int j = 0; j < i; j++, cus.next()) {
                                        // {"First Name","Last Name","Email",
                                        // "Address","Address2","District","City","Postal Code","Phone Number","Store
                                        // ID","Active","Create Date"}
                                        data[j] = new String[13];
                                        data[j][0] = Integer.toString(cus.getInt(1));
                                        data[j][1] = cus.getString(3);
                                        data[j][2] = cus.getString(4);
                                        data[j][3] = cus.getString(5);

                                        for (address.first(); !address.isAfterLast(); address.next()) {
                                            if (address.getInt(1) == cus.getInt(6)) {
                                                break;
                                            }
                                        }
                                        data[j][4] = address.getString(2);
                                        data[j][5] = address.getString(3);
                                        data[j][6] = address.getString(4);
                                        for (city.first(); !city.isAfterLast(); city.next()) {
                                            if (city.getInt(1) == address.getInt(5)) {
                                                break;
                                            }
                                        }
                                        data[j][7] = city.getString(2);
                                        data[j][8] = address.getString(6);
                                        data[j][9] = address.getString(7);
                                        data[j][10] = cus.getString(2);
                                        data[j][11] = cus.getString(7);
                                        data[j][12] = cus.getString(8);
                                    }
                                    return data;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String[][] getInvTable(String url) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet client = statement.executeQuery(
                        "SELECT store_id, name, count(*) FROM inventory INNER JOIN film_category ON inventory.film_id = film_category.film_id INNER JOIN category ON film_category.category_id = category.category_id group by store_id,name;")) {
                    client.last();
                    int r = client.getRow();
                    client.first();
                    String[][] data = new String[r][];
                    for (int j = 0; j < r; j++, client.next()) {
                        data[j] = new String[3];
                        data[j][0] = Integer.toString(client.getInt(1));
                        data[j][1] = client.getString(2);
                        data[j][2] = Integer.toString(client.getInt(3));
                    }
                    return data;
                }
            }
        }
    }

    private String[][] getFilmsTable(String url) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet film = statement.executeQuery("SELECT * FROM film")) {
                    try (Statement statement1 = connection.createStatement()) {
                        try (ResultSet language = statement1.executeQuery("SELECT * FROM language")) {
                            film.last();
                            int i = film.getRow();
                            String[][] data = new String[i][];
                            film.first();

                            for (int j = 0; j < i; j++, film.next()) {
                                data[j] = new String[10];
                                data[j][0] = film.getString(2);
                                data[j][1] = film.getString(3);
                                Date date = film.getDate(4);
                                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                                String strDate = dateFormat.format(date);
                                data[j][2] = strDate;
                                for (language.first(); !language.isAfterLast(); language.next()) {
                                    if (film.getInt(5) == language.getInt(1)) {
                                        break;
                                    }
                                }
                                data[j][3] = language.getString(2);
                                if (film.getInt(6) == 0)
                                    data[j][4] = "";
                                else {
                                    for (language.first(); !language.isAfterLast(); language.next()) {
                                        if (film.getInt(6) == language.getInt(1)) {
                                            break;
                                        }
                                    }
                                    data[j][4] = language.getString(2);
                                }
                                data[j][5] = Integer.toString(film.getInt(7));
                                data[j][6] = film.getDouble(8) + "";
                                data[j][7] = Integer.toString(film.getInt(9));
                                data[j][8] = film.getDouble(10) + "";
                                data[j][9] = film.getString(11);
                            }
                            return data;
                        }
                    }
                }
            }
        }
    }

    private String[][] getStaffTable(String url) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet staff = statement.executeQuery("SELECT * FROM staff")) {
                    try (Statement statement1 = connection.createStatement()) {
                        try (ResultSet address = statement1.executeQuery("SELECT * FROM address")) {
                            try (Statement statement2 = connection.createStatement()) {
                                try (ResultSet store = statement2.executeQuery("SELECT * FROM store")) {
                                    try (Statement statement3 = connection.createStatement()) {
                                        try (ResultSet city = statement3.executeQuery("SELECT * FROM city")) {
                                            staff.last();
                                            int i = staff.getRow();
                                            String data[][] = new String[i][];
                                            staff.first();

                                            for (int j = 0; j < i; j++, staff.next()) {
                                                data[j] = new String[10];
                                                data[j][0] = staff.getString(2);
                                                data[j][1] = staff.getString(3);

                                                for (address.first(); !address.isAfterLast(); address.next()) {
                                                    if (address.getInt(1) == staff.getInt(4)) {
                                                        break;
                                                    }
                                                }
                                                data[j][2] = address.getString(2);
                                                data[j][3] = address.getString(3);
                                                data[j][4] = address.getString(4);
                                                for (city.first(); !city.isAfterLast(); city.next()) {
                                                    if (city.getInt(1) == address.getInt(5)) {
                                                        break;
                                                    }
                                                }
                                                data[j][5] = city.getString(2);
                                                data[j][6] = address.getString(6);
                                                data[j][7] = address.getString(7);
                                                data[j][8] = Integer.toString(staff.getInt(7));
                                                int k = staff.getInt(8);
                                                if (k == 1)
                                                    data[j][9] = "True";
                                                else
                                                    data[j][9] = "False";
                                            }
                                            return data;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

class myRowFilter extends RowFilter {
    private String searchText;

    myRowFilter(String st) {
        searchText = st;
    }

    @Override
    public boolean include(Entry entry) {
        for (int i = 0; i < 10; i++)
            if (entry.getStringValue(i).indexOf(searchText) >= 0)
                return true;
        return false;
    }

}

class MyTableModel extends AbstractTableModel {
    private String[] column;
    private String[][] data;

    MyTableModel(String[] col, String[][] d) {
        column = col;
        data = d;
    }

    public void newData(String[][] d) {
        data = d;
    }

    public int getRowCount() {
        return data.length;
    }

    public int getColumnCount() {
        return column.length;
    }

    public String getValueAt(int i, int j) {
        return data[i][j];
    }

    public String getColumnName(int i) {
        return column[i];
    }
}