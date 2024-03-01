import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Product {
    private String p_id;
    private String p_name;
    private int p_price;
    private int p_qty;
    private String url = "jdbc:mysql://localhost/comstore";
    private Connection con = null;
    private Statement st = null;
    private ResultSet rs = null;
    private PreparedStatement ps = null;

    public void setDetails(String id, String name, int qty, int price ) {
        p_id = id;
        p_name = name;
        p_price = qty;
        p_qty = price;
    }

    public String getid() {
        return p_id;
    }

    public String getname() {
        return p_name;
    }
    public int getqty() {
        return p_qty;
    }
    public int getprice() {
        return p_price;
    }



    public Object[] convert2row() {
        Object[] row = { p_id, p_name, p_qty, p_price};
        return row;
    }

    public void connectsql() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, "root", "");  
            st = con.createStatement();
            rs = st.executeQuery("select * from product");
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error data connect");
        }
    }

    public void insertP() {
        try {
            connectsql();
            ps = con.prepareStatement("INSERT INTO product (p_id, p_name, p_qty, p_price) VALUES (?,?,?,?)");
            ps.setString(1, p_id);
            ps.setString(2, p_name);
            ps.setInt(3, p_qty);
            ps.setInt(4, p_price);

            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public void updatep() {
        try {
            connectsql();
            ps = con.prepareStatement("UPDATE product SET p_name=?, p_qty=?, p_price=? WHERE p_id=?)");
            ps.setString(1, p_name);
            ps.setInt(2, p_qty);
            ps.setInt(3, p_price);
            ps.setString(4, p_id);

            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ResultSet selectAllp() {
        connectsql();
        ResultSet rs = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM product");

        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return rs;
    }

    public void deletep() {
        try {
            connectsql();
            ps = con.prepareStatement("DELETE FROM product WHERE p_id = ?");
            ps.setString(1, p_id);  // Assuming p_id is an attribute in your Product class
            ps.executeUpdate();
            ps.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public ResultSet getAllResultSet() {
        ResultSet resultSet = null;
        try {
            connectsql();
            Statement stmt = con.createStatement();
            String quey = "SELECT * FROM product";
            resultSet = stmt.executeQuery(quey);
        } catch (Exception e) {
            e.getMessage();
        }

        return resultSet ; 
    }

}////////// class main
