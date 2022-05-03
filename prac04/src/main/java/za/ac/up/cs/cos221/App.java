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
     */
    public App() throws SQLException {
        initialize();
    }

    private void initialize() throws SQLException {
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