package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import guiManager.backup;
import guiManager.restore;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.String.format;
import java.sql.Connection;
import java.sql.DriverManager;
import model.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import model.GRNItem;
import model.chartAdmin;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class adminDashboard extends javax.swing.JFrame {

    private static HashMap<String, String> brandMap = new HashMap<>();
    private static HashMap<String, String> categoryMap = new HashMap<>();
    private static HashMap<String, String> cMap = new HashMap<>();
    HashMap<String, GRNItem> grnItemMap = new HashMap<>();

    public adminDashboard(String email) {
        initComponents();
        jLabel3.setText(email);
        loadEmployee();
        loadSpplier("first_name", "ASC");
        loadSelling1();
        loadSummary();
        loadProduct("datetime", "DESC");
        loadBrand();
        loadCategory();
        loadStock();
        loadGRN();
        jTextField8.setEditable(false);
        jTextField10.setEditable(false);

        loadGRNItems();
        hint();
        generateGRNid();
        jTextField5.setEditable(false);
        jTextField6.setEditable(false);
        reload();
        productId();
        jTextField1.setEditable(false);
        loadProductall();
        loadStock1();
        startAutoRefresh();
        loardchart();
    }

    //summery refreash
    public void startAutoRefresh() {
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadSummary();
                loardchart();
            }
        });
        timer.start();
    }

    //chart
    private void loardchart() {
        chartAdmin.loadChartToJPanel(jPanel49);
    }

    private adminDashboard() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void generateGRNid() {
        Long id = System.currentTimeMillis();
        jTextField8.setText(String.valueOf(id));

    }

    private void productId() {
        Long id = System.currentTimeMillis();
        String productId = "MEDI-" + id;
        jTextField1.setText(productId);
    }

    //mobile
    public JTextField getjTextField5() {
        return jTextField5;
    }

    //product id
    public JTextField getjTextField6() {
        return jTextField6;
    }

    //brand
    public JLabel getjLabel20() {
        return jLabel20;
    }

    //name
    public JLabel getjLabel21() {
        return jLabel21;
    }

    private void reload() {

        java.lang.Runnable runnable = new java.lang.Runnable() {
            @Override
            public void run() {

                while (true) {

                    loadEmployee();
                    loadProduct("datetime", "DESC");
                    loadSpplier("first_name", "ASC");

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(adminDashboard.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }
        };

        java.lang.Thread thread = new java.lang.Thread(runnable);
        thread.start();

    }

    private void loadGRNItems() {
        DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
        model.setRowCount(0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        double total = 0;

        for (GRNItem grnItem : grnItemMap.values()) {
            Vector<String> vector = new Vector<>();
            vector.add(grnItem.getProductId());
            vector.add(grnItem.getBrandName());
            vector.add(grnItem.getProductName());
            vector.add(String.valueOf(grnItem.getQty()));
            vector.add(String.valueOf(grnItem.getBuyingPrice()));
            vector.add(String.valueOf(grnItem.getSellingPrice()));
            vector.add(format.format(grnItem.getMfd()));
            vector.add(format.format(grnItem.getExp()));

            double itemTotal = grnItem.getQty() * grnItem.getBuyingPrice();
            total += itemTotal;
            vector.add(String.valueOf(itemTotal));

            model.addRow(vector);
        }

        jTextField10.setText(String.valueOf(total));

    }

    private void hint() {

        if (jTextField5 != null) {
            jTextField5.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Suplier");
        }
        if (jTextField6 != null) {
            jTextField6.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Product");
        }
        if (jFormattedTextField6 != null) {
            jFormattedTextField6.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Qty");
        }
        if (jFormattedTextField3 != null) {
            jFormattedTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Buying Price");
        }
        if (jFormattedTextField4 != null) {
            jFormattedTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selling Price");
        }

        if (jTextField7 != null) {
            jTextField7.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mobile");
        }
        if (jTextField1 != null) {
            jTextField1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Product ID");
        }
        if (jTextField2 != null) {
            jTextField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Product Name");
        }
        if (jTextField3 != null) {
            jTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Add New Brand");
        }
        if (jTextField4 != null) {
            jTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Add New Category");
        }
        if (jTextField12 != null) {
            jTextField12.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Product Name");
        }
    }

    private void loadEmployee() {
        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM employee "
                    + "INNER JOIN gender ON employee.gender_id=gender.id "
                    + "INNER JOIN employee_type ON employee.employee_type_id=employee_type.id ORDER BY `date_registered` DESC");

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("first_name"));
                vector.add(resultSet.getString("last_name"));
                vector.add(resultSet.getString("nic"));
                vector.add(resultSet.getString("mobile"));
                vector.add(resultSet.getString("password"));
                vector.add(resultSet.getString("gender.name"));
                vector.add(resultSet.getString("employee_type.name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadSpplier(String column, String orderby) {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM suppler "
                    + "INNER JOIN company ON suppler.company_id=company.id ORDER BY `" + column + "` " + orderby + "");

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("mobile"));
                vector.add(resultSet.getString("first_name"));
                vector.add(resultSet.getString("last_name"));
                vector.add(resultSet.getString("email"));
                vector.add(resultSet.getString("company.name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadSelling1() {
        try {
            boolean isQueryUpdated = false;

            String query = "SELECT * FROM invoice "
                    + "INNER JOIN employee ON invoice.employee_email = employee.email "
                    + "INNER JOIN payment_method ON invoice.payment_method_id = payment_method.id";

            if (jDateChooser6.getDate() != null && !isQueryUpdated) {
                query += " WHERE DATE(invoice.date) = '" + new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser6.getDate()) + "'";
                isQueryUpdated = true;
            }

            if (!jTextField7.getText().isEmpty() && !isQueryUpdated) {
                query += " WHERE invoice.employee_email LIKE '" + jTextField7.getText() + "%'";
                isQueryUpdated = true;
            } else if (!jTextField7.getText().isEmpty() && isQueryUpdated) {
                query += " AND invoice.employee_email LIKE '" + jTextField7.getText() + "%'";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("id"));
                vector.add(resultSet.getString("employee.first_name"));
                vector.add(resultSet.getString("payment_method.name"));
                vector.add(resultSet.getString("total_amount"));
                vector.add(resultSet.getString("date"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSummary() {

        try {
            //all employee
            ResultSet employeeCountResult = MySQL.executeSearch("SELECT COUNT(*) AS `employee_count` FROM `employee`");

            if (employeeCountResult.next()) {
                jLabel12.setText(employeeCountResult.getString("employee_count"));
            }

            //all product
            ResultSet productCountResult = MySQL.executeSearch("SELECT COUNT(*) AS `product_count` FROM `product`");

            if (productCountResult.next()) {
                jLabel14.setText(productCountResult.getString("product_count") + "+");
            }

            //all selling count
            ResultSet SellingCountResult = MySQL.executeSearch("SELECT COUNT(*) AS `selling_count` FROM `invoice`");

            if (SellingCountResult.next()) {
                jLabel7.setText(SellingCountResult.getString("selling_count"));
            }

            //all amount
            String date = new SimpleDateFormat("yyyy-MM").format(new Date());
            double allEarning = 0.0;
            ResultSet allEarningRS = MySQL.executeSearch("SELECT * FROM `invoice` WHERE `date` LIKE '" + date + "%'");

            while (allEarningRS.next()) {
                allEarning += allEarningRS.getDouble("total_amount");
            }

            jLabel17.setText(String.valueOf("Rs." + allEarning + "0"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String weekStartDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 6L * 24 * 60 * 60 * 1000)); // 7 days including today
            String monthDate = new SimpleDateFormat("yyyy-MM").format(new Date());
            String yearDate = new SimpleDateFormat("yyyy").format(new Date());

            double todayTotal = 0.0, weeklyTotal = 0.0, monthlyTotal = 0.0, yearlyTotal = 0.0;
            int todayCount = 0, weeklyCount = 0, monthlyCount = 0, yearlyCount = 0;

            // Today sales
            ResultSet todayRS = MySQL.executeSearch("SELECT COUNT(*) AS invoice_count, SUM(total_amount) AS total FROM invoice WHERE date LIKE '" + todayDate + "%'");
            if (todayRS.next()) {
                todayTotal = todayRS.getDouble("total");
                todayCount = todayRS.getInt("invoice_count");
            }
            jLabel67.setText("Rs." + todayTotal + "0"); 
            jLabel78.setText(String.valueOf(todayCount));

            // Weekly sales
            ResultSet weeklyRS = MySQL.executeSearch("SELECT COUNT(*) AS invoice_count, SUM(total_amount) AS total FROM invoice WHERE date BETWEEN '" + weekStartDate + "' AND '" + todayDate + "'");
            if (weeklyRS.next()) {
                weeklyTotal = weeklyRS.getDouble("total");
                weeklyCount = weeklyRS.getInt("invoice_count");
            }
            jLabel68.setText("Rs." + weeklyTotal + "0"); 
            jLabel79.setText(String.valueOf(weeklyCount));

            // Monthly sales
            ResultSet monthlyRS = MySQL.executeSearch("SELECT COUNT(*) AS invoice_count, SUM(total_amount) AS total FROM invoice WHERE date LIKE '" + monthDate + "%'");
            if (monthlyRS.next()) {
                monthlyTotal = monthlyRS.getDouble("total");
                monthlyCount = monthlyRS.getInt("invoice_count");
            }
            jLabel69.setText("Rs." + monthlyTotal + "0"); 
            jLabel80.setText(String.valueOf(monthlyCount));

            // Yearly sales
            ResultSet yearlyRS = MySQL.executeSearch("SELECT COUNT(*) AS invoice_count, SUM(total_amount) AS total FROM invoice WHERE date LIKE '" + yearDate + "%'");
            if (yearlyRS.next()) {
                yearlyTotal = yearlyRS.getDouble("total");
                yearlyCount = yearlyRS.getInt("invoice_count");
            }
            jLabel72.setText("Rs." + yearlyTotal + "0");
            jLabel81.setText(String.valueOf(yearlyCount));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadProduct(String column, String ORDRYBY) {
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM product "
                    + "INNER JOIN brand ON product.brand_id=brand.id "
                    + "INNER JOIN category ON product.category_id=category.id ORDER BY '" + column + "' '" + ORDRYBY + "'");

            DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("id"));
                vector.add(resultSet.getString("brand.name"));
                vector.add(resultSet.getString("category.name"));
                vector.add(resultSet.getString("name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadStock() {

        try {

            String query = "SELECT * FROM `stock` INNER JOIN `product`"
                    + "ON `stock`.`product_id` = `product`.`id` "
                    + "INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` ";

            int row = jTable4.getSelectedRow();

            if (row != -1) {
                String pid = String.valueOf(jTable4.getValueAt(row, 0));
                query += "WHERE `stock`.`product_id` = '" + pid + "' ";
            }

            if (query.contains("WHERE")) {
                query += "AND ";
            } else {
                query += "WHERE ";
            }

            double min_price = 0;
            double max_price = 0;

            if (!jFormattedTextField1.getText().isEmpty()) {
                min_price = Double.parseDouble(jFormattedTextField1.getText());
            }

            if (!jFormattedTextField2.getText().isEmpty()) {
                max_price = Double.parseDouble(jFormattedTextField2.getText());
            }

            if (min_price > 0 && max_price == 0) {
                query += "`stock`.`selling_price` > '" + min_price + "' ";
            } else if (min_price == 0 && max_price > 0) {
                query += "`stock`.`selling_price` < '" + max_price + "' ";
            } else if (min_price > 0 && max_price > 0) {
                query += "`stock`.`selling_price` > '" + min_price + "' AND `stock`.`selling_price` <  '" + max_price + "'";
            }

            //exp
            Date start = null;
            Date end = null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (jDateChooser1.getDate() != null) {
                start = jDateChooser1.getDate();
                query += "`stock`.`exp` > '" + format.format(start) + "' AND ";
            }

            if (jDateChooser2.getDate() != null) {
                end = jDateChooser2.getDate();
                query += "`stock`.`exp` < '" + format.format(end) + "' ";
            }

            String sort = String.valueOf(jComboBox4.getSelectedItem());
            query += "ORDER BY ";
            query = query.replace("WHERE ORDER BY ", "ORDER BY ");
            query = query.replace("AND ORDER BY ", "ORDER BY ");

            if (sort.equals("Stock ID ASC")) {
                query += "`stock`.`id` ASC";
            } else if (sort.equals("Stock ID DESC")) {
                query += "`stock`.`id` DESC";
            } else if (sort.equals("Product ID ASC")) {
                query += "`product`.`id` ASC";
            } else if (sort.equals("Product ID DESC")) {
                query += "`product`.`id` DESC";
            } else if (sort.equals("Brand ASC")) {
                query += "`brand`.`id` ASC";
            } else if (sort.equals("Brand DESC")) {
                query += "`brand`.`id` DESC";
            } else if (sort.equals("Name ASC")) {
                query += "`brand`.`name` ASC";
            } else if (sort.equals("Name DESC")) {
                query += "`brand`.`name` DESC";
            } else if (sort.equals("Selling Price ASC")) {
                query += "`stock`.`selling_price` ASC";
            } else if (sort.equals("Selling Price DESC")) {
                query += "`stock`.`selling_price` DESC";
            } else if (sort.equals("Quantity ASC")) {
                query += "`stock`.`qty` ASC";
            } else if (sort.equals("Quantity DESC")) {
                query += "`stock`.`qty` DESC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable5.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("stock.id"));
                vector.add(resultSet.getString("product.id"));
                vector.add(resultSet.getString("brand.name"));
                vector.add(resultSet.getString("product.name"));
                vector.add(resultSet.getString("stock.selling_price"));
                vector.add(resultSet.getString("qty"));
                vector.add(resultSet.getString("mfd"));
                vector.add(resultSet.getString("exp"));
                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadStock1() {

        try {

            String query = "SELECT * FROM `stock` INNER JOIN `product`"
                    + "ON `stock`.`product_id` = `product`.`id` "
                    + "INNER JOIN `brand` ON `brand`.`id` = `product`.`brand_id` ";

            int row = jTable9.getSelectedRow();

            if (row != -1) {
                String pid = String.valueOf(jTable9.getValueAt(row, 0));
                query += "WHERE `stock`.`product_id` = '" + pid + "' ";
            }

            if (query.contains("WHERE")) {
                query += "AND ";
            } else {
                query += "WHERE ";
            }

            double min_price = 0;
            double max_price = 0;

            if (!jFormattedTextField7.getText().isEmpty()) {
                min_price = Double.parseDouble(jFormattedTextField7.getText());
            }

            if (!jFormattedTextField8.getText().isEmpty()) {
                max_price = Double.parseDouble(jFormattedTextField8.getText());
            }

            if (min_price > 0 && max_price == 0) {
                query += "`stock`.`selling_price` > '" + min_price + "' ";
            } else if (min_price == 0 && max_price > 0) {
                query += "`stock`.`selling_price` < '" + max_price + "' ";
            } else if (min_price > 0 && max_price > 0) {
                query += "`stock`.`selling_price` > '" + min_price + "' AND `stock`.`selling_price` <  '" + max_price + "'";
            }

            //exp
            Date start = null;
            Date end = null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            if (jDateChooser7.getDate() != null) {
                start = jDateChooser7.getDate();
                query += "`stock`.`exp` > '" + format.format(start) + "' AND ";
            }

            if (jDateChooser8.getDate() != null) {
                end = jDateChooser8.getDate();
                query += "`stock`.`exp` < '" + format.format(end) + "' ";
            }

            String sort = String.valueOf(jComboBox6.getSelectedItem());
            query += "ORDER BY ";
            query = query.replace("WHERE ORDER BY ", "ORDER BY ");
            query = query.replace("AND ORDER BY ", "ORDER BY ");

            if (sort.equals("Stock ID ASC")) {
                query += "`stock`.`id` ASC";
            } else if (sort.equals("Stock ID DESC")) {
                query += "`stock`.`id` DESC";
            } else if (sort.equals("Product ID ASC")) {
                query += "`product`.`id` ASC";
            } else if (sort.equals("Product ID DESC")) {
                query += "`product`.`id` DESC";
            } else if (sort.equals("Brand ASC")) {
                query += "`brand`.`id` ASC";
            } else if (sort.equals("Brand DESC")) {
                query += "`brand`.`id` DESC";
            } else if (sort.equals("Name ASC")) {
                query += "`brand`.`name` ASC";
            } else if (sort.equals("Name DESC")) {
                query += "`brand`.`name` DESC";
            } else if (sort.equals("Selling Price ASC")) {
                query += "`stock`.`selling_price` ASC";
            } else if (sort.equals("Selling Price DESC")) {
                query += "`stock`.`selling_price` DESC";
            } else if (sort.equals("Quantity ASC")) {
                query += "`stock`.`qty` ASC";
            } else if (sort.equals("Quantity DESC")) {
                query += "`stock`.`qty` DESC";
            }

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable9.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("stock.id"));
                vector.add(resultSet.getString("product.id"));
                vector.add(resultSet.getString("brand.name"));
                vector.add(resultSet.getString("product.name"));
                vector.add(resultSet.getString("stock.selling_price"));
                vector.add(resultSet.getString("qty"));
                vector.add(resultSet.getString("mfd"));
                vector.add(resultSet.getString("exp"));
                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadBrand() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `brand`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                brandMap.put(resultSet.getString("name"), resultSet.getString("id"));
                cMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            jComboBox3.setModel(new DefaultComboBoxModel<>(vector));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCategory() {

        try {
            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `category`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                categoryMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            jComboBox5.setModel(new DefaultComboBoxModel<>(vector));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGRN() {
        try {
            boolean isQUeryUpdated = false;

            String query = "SELECT * FROM `grn`";

            if (!jTextField7.getText().isEmpty() && !isQUeryUpdated) {
                query += " WHERE `suppler_mobile` LIKE '" + jTextField7.getText() + "%'";
                isQUeryUpdated = true;
            } else if (!jTextField7.getText().isEmpty() && isQUeryUpdated) {
                query += " AND `suppler_mobile` LIKE '" + jTextField7.getText() + "%'";
            }

            if (jDateChooser5.getDate() != null && !isQUeryUpdated) {
                query += " WHERE `added_date` LIKE '" + new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser5.getDate()) + "%'";
                isQUeryUpdated = true;
            } else if (jDateChooser5.getDate() != null && isQUeryUpdated) {
                query += " AND `added_date` LIKE '" + new SimpleDateFormat("yyyy-MM-dd").format(jDateChooser5.getDate()) + "%'";
            }

            ResultSet result = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (result.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(result.getString("id"));
                vector.add(result.getString("suppler_mobile"));
                vector.add(result.getString("added_date"));
                vector.add(result.getString("paid_amount"));
                vector.add(result.getString("employee_email"));

                model.addRow(vector);
                jTable1.setModel(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadProductall() {
        try {
            String searchText = jTextField12.getText().trim();

            String query = "SELECT product.id, brand.name AS brand_name, category.name AS category_name, product.name "
                    + "FROM product "
                    + "INNER JOIN brand ON product.brand_id = brand.id "
                    + "INNER JOIN category ON product.category_id = category.id ";

            if (!searchText.isEmpty()) {
                query += "WHERE product.name LIKE '%" + searchText + "%' "
                        + "OR brand.name LIKE '%" + searchText + "%' "
                        + "OR category.name LIKE '%" + searchText + "%' ";
            }

            query += "ORDER BY product.id ASC";

            ResultSet resultSet = MySQL.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable8.getModel();
            model.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("id"));
                vector.add(resultSet.getString("brand_name"));
                vector.add(resultSet.getString("category_name"));
                vector.add(resultSet.getString("name"));

                model.addRow(vector);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane8 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jPanel49 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel37 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jLabel38 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel39 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton19 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox<>();
        jButton20 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton43 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jButton42 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jButton38 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jLabel47 = new javax.swing.JLabel();
        jDateChooser6 = new com.toedter.calendar.JDateChooser();
        jButton27 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jButton39 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton24 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jTextField10 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jButton23 = new javax.swing.JButton();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        jButton22 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jFormattedTextField4 = new javax.swing.JFormattedTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jTextField8 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel19 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jFormattedTextField6 = new javax.swing.JFormattedTextField();
        jPanel34 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jButton40 = new javax.swing.JButton();
        jTextField12 = new javax.swing.JTextField();
        jButton34 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jPanel35 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jButton41 = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel50 = new javax.swing.JLabel();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jLabel51 = new javax.swing.JLabel();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jButton35 = new javax.swing.JButton();
        jDateChooser7 = new com.toedter.calendar.JDateChooser();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jDateChooser8 = new com.toedter.calendar.JDateChooser();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jPanel51 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jButton17 = new javax.swing.JButton();
        jPanel47 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setBackground(new java.awt.Color(255, 255, 51));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(193, 215, 226));

        jButton1.setBackground(new java.awt.Color(24, 119, 242));
        jButton1.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dashboard.png"))); // NOI18N
        jButton1.setText("Dashboard");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(56, 69, 81));
        jButton2.setText("Manage Stock");
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(255, 0, 0));
        jButton6.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logout-24.png"))); // NOI18N
        jButton6.setText("Logout");
        jButton6.setBorderPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(56, 69, 81));
        jButton7.setText("Manage Employees");
        jButton7.setBorderPainted(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(56, 69, 81));
        jButton8.setText("Manage Supplier");
        jButton8.setBorderPainted(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton10.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(56, 69, 81));
        jButton10.setText("View All Selling");
        jButton10.setBorderPainted(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton12.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton12.setForeground(new java.awt.Color(56, 69, 81));
        jButton12.setText("View All GRN");
        jButton12.setBorderPainted(false);
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton21.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton21.setForeground(new java.awt.Color(56, 69, 81));
        jButton21.setText("Add New GRN");
        jButton21.setBorderPainted(false);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton32.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton32.setForeground(new java.awt.Color(56, 69, 81));
        jButton32.setText("All Product");
        jButton32.setBorderPainted(false);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton33.setForeground(new java.awt.Color(56, 69, 81));
        jButton33.setText("All Stock");
        jButton33.setBorderPainted(false);
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton29.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton29.setForeground(new java.awt.Color(56, 69, 81));
        jButton29.setText("Settings");
        jButton29.setBorderPainted(false);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(44, 73, 114));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_new.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 201, 63));

        jLabel2.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Welcome");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, -1, 20));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(".");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 20, 140, -1));

        jLabel30.setFont(new java.awt.Font("Inter ExtraBold", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Admin Dashboard");
        jPanel2.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 0, 350, 60));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(244, 245, 250));
        jPanel4.setPreferredSize(new java.awt.Dimension(1020, 630));

        jPanel27.setPreferredSize(new java.awt.Dimension(620, 120));

        jPanel16.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(56, 69, 81));
        jLabel5.setText("Summary");

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3.png"))); // NOI18N

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2.png"))); // NOI18N

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setForeground(new java.awt.Color(255, 255, 255));
        jPanel14.setPreferredSize(new java.awt.Dimension(167, 100));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Untitled design (2).png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(114, 122, 132));
        jLabel6.setText("Sales");

        jLabel7.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel7.setText(".");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(8, 8, 8)
                .addComponent(jLabel7))
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.png"))); // NOI18N

        jLabel11.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(114, 122, 132));
        jLabel11.setText("Employee");

        jLabel12.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel12.setText("150+");

        jLabel13.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(114, 122, 132));
        jLabel13.setText("Medication");

        jLabel14.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel14.setText("2000+");

        jLabel15.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(114, 122, 132));
        jLabel15.setText("Earning");

        jLabel17.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel17.setText("10,000");

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(151, 151, 151)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(34, 34, 34))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addGap(8, 8, 8)
                            .addComponent(jLabel12))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                            .addComponent(jLabel13)
                            .addGap(8, 8, 8)
                            .addComponent(jLabel14)))
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel17))
                    .addComponent(jLabel10))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel33.setBackground(new java.awt.Color(255, 255, 255));

        jLabel33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-today-40.png"))); // NOI18N

        jLabel57.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(114, 122, 132));
        jLabel57.setText("Today");

        jLabel59.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(56, 69, 81));
        jLabel59.setText("Sales");

        jLabel60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-week-40.png"))); // NOI18N

        jLabel61.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(114, 122, 132));
        jLabel61.setText("Weekly");

        jLabel64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-month-40.png"))); // NOI18N

        jLabel65.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(114, 122, 132));
        jLabel65.setText("Monthly");

        jLabel67.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel67.setText("10,000");

        jLabel68.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel68.setText("10,000");

        jLabel69.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel69.setText("10,000");

        jLabel66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-plus-1-year-40.png"))); // NOI18N

        jLabel70.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(114, 122, 132));
        jLabel70.setText("Annualy");

        jLabel72.setFont(new java.awt.Font("Inter Display SemiBold", 0, 13)); // NOI18N
        jLabel72.setText("10,000");

        jLabel73.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(56, 69, 81));
        jLabel73.setText("Invoice Count");

        jLabel74.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(114, 122, 132));
        jLabel74.setText("Today :");

        jLabel75.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(114, 122, 132));
        jLabel75.setText("Weekly :");

        jLabel76.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(114, 122, 132));
        jLabel76.setText("Monthly :");

        jLabel77.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(114, 122, 132));
        jLabel77.setText("Annualy :");

        jLabel78.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(0, 0, 0));
        jLabel78.setText("10");

        jLabel79.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(0, 0, 0));
        jLabel79.setText("10");

        jLabel80.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(0, 0, 0));
        jLabel80.setText("10");

        jLabel81.setFont(new java.awt.Font("Inter", 0, 12)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(0, 0, 0));
        jLabel81.setText("10");

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel64, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel66, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel70, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel68, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel72, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel73)
                            .addComponent(jLabel59)
                            .addGroup(jPanel33Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel74)
                                    .addComponent(jLabel75)
                                    .addComponent(jLabel76)
                                    .addComponent(jLabel77))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel59)
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel57)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel67))
                    .addComponent(jLabel33, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel68))
                    .addComponent(jLabel60, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel69))
                    .addComponent(jLabel64, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel33Layout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel72))
                    .addComponent(jLabel66, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addComponent(jLabel73)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(jLabel78))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(jLabel80))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(jLabel81))
                .addGap(38, 38, 38))
        );

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 657, Short.MAX_VALUE)
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane8.addTab("tab1", jPanel4);

        jPanel5.setBackground(new java.awt.Color(244, 245, 250));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));

        jLabel32.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(56, 69, 81));
        jLabel32.setText("Manage Product & Stock");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel32)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jTextField1.setForeground(new java.awt.Color(30, 30, 30));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jTextField2.setForeground(new java.awt.Color(30, 30, 30));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(56, 69, 81));
        jLabel34.setText("Brand");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        jTextField3.setForeground(new java.awt.Color(30, 30, 30));

        jButton3.setBackground(new java.awt.Color(24, 119, 242));
        jButton3.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("+");
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(24, 119, 242));
        jButton14.setFont(new java.awt.Font("Open Sans Extrabold", 1, 12)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("Add");
        jButton14.setBorderPainted(false);
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(113, 221, 55));
        jButton15.setFont(new java.awt.Font("Open Sans Extrabold", 1, 12)); // NOI18N
        jButton15.setForeground(new java.awt.Color(255, 255, 255));
        jButton15.setText("Update");
        jButton15.setBorderPainted(false);
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(51, 51, 51));
        jButton16.setFont(new java.awt.Font("Open Sans Extrabold", 1, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(255, 255, 255));
        jButton16.setText("Clear");
        jButton16.setBorderPainted(false);
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Brand", "Category", "Product Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable4);

        jLabel35.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(56, 69, 81));
        jLabel35.setText("Sort By :");

        jComboBox4.setForeground(new java.awt.Color(30, 30, 30));
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stock ID ASC", "Stock ID DESC", "Product ID ASC", "Product ID DESC", "Brand ASC", "Brand DESC", "Name ASC", "Name DESC", "Selling Price ASC", "Selling Price DESC", "Quantity ASC", "Quantity DESC" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });
        jComboBox4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox4KeyReleased(evt);
            }
        });

        jLabel36.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(56, 69, 81));
        jLabel36.setText("Selling Price");

        jFormattedTextField1.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyTyped(evt);
            }
        });

        jLabel37.setFont(new java.awt.Font("Inter Medium", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(56, 69, 81));
        jLabel37.setText("to");

        jFormattedTextField2.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField2KeyTyped(evt);
            }
        });

        jLabel38.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(56, 69, 81));
        jLabel38.setText("EXP");

        jDateChooser1.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser1.setDateFormatString("yyyy-MM-dd");
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jDateChooser1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser1KeyReleased(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Inter Medium", 1, 12)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(56, 69, 81));
        jLabel39.setText("to");

        jDateChooser2.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser2.setDateFormatString("yyyy-MM-dd");
        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });
        jDateChooser2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser2KeyReleased(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(51, 51, 51));
        jButton19.setFont(new java.awt.Font("Open Sans Extrabold", 1, 12)); // NOI18N
        jButton19.setForeground(new java.awt.Color(255, 255, 255));
        jButton19.setText("Clear");
        jButton19.setBorderPainted(false);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Id", "Brand", "Name", "Selling Price", "Quantity", "MFD", "EXP"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable5.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(jTable5);

        jLabel40.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(56, 69, 81));
        jLabel40.setText("Category");

        jTextField4.setForeground(new java.awt.Color(30, 30, 30));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox5ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(24, 119, 242));
        jButton20.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setText("+");
        jButton20.setBorderPainted(false);
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(24, 119, 242));
        jButton18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton18.setForeground(new java.awt.Color(255, 255, 255));
        jButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search-14.png"))); // NOI18N
        jButton18.setBorderPainted(false);
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton26.setBackground(new java.awt.Color(24, 119, 242));
        jButton26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton26.setForeground(new java.awt.Color(255, 255, 255));
        jButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-search-14.png"))); // NOI18N
        jButton26.setBorderPainted(false);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3))
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20))
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(152, 152, 152)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton18)
                        .addGap(56, 56, 56)
                        .addComponent(jButton19))
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton14)
                                .addComponent(jButton15))
                            .addGap(3, 3, 3)
                            .addComponent(jButton16))
                        .addGroup(jPanel5Layout.createSequentialGroup()
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(3, 3, 3)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton3)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel40)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton20))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(jButton18)
                        .addComponent(jButton19)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel36, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel37)
                                    .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jButton26))
                        .addGap(1, 1, 1)))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
        );

        jTabbedPane8.addTab("tab2", jPanel5);

        jPanel7.setBackground(new java.awt.Color(244, 245, 250));
        jPanel7.setForeground(new java.awt.Color(56, 69, 81));

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));

        jLabel22.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(56, 69, 81));
        jLabel22.setText("Manage Employee");

        jButton5.setBackground(new java.awt.Color(24, 119, 242));
        jButton5.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Add New Employee");
        jButton5.setBorderPainted(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addContainerGap(384, Short.MAX_VALUE)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Email", "First Name", "Last Name", "NIC", "Mobile", "Password", "Gender", "Job Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setMinWidth(150);
            jTable2.getColumnModel().getColumn(0).setMaxWidth(500);
        }

        jButton43.setBackground(new java.awt.Color(24, 119, 242));
        jButton43.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton43.setForeground(new java.awt.Color(255, 255, 255));
        jButton43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-print-25.png"))); // NOI18N
        jButton43.setText("Print Report");
        jButton43.setBorderPainted(false);
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2)
                        .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jTabbedPane8.addTab("tab4", jPanel7);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));

        jLabel23.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(56, 69, 81));
        jLabel23.setText("Manage Supplier");

        jButton11.setBackground(new java.awt.Color(24, 119, 242));
        jButton11.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Add New Supplier");
        jButton11.setBorderPainted(false);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(351, Short.MAX_VALUE)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel24.setFont(new java.awt.Font("Inter Medium", 0, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(56, 69, 81));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("000");

        jLabel25.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(56, 69, 81));
        jLabel25.setText("Total Payments :");

        jComboBox2.setForeground(new java.awt.Color(30, 30, 30));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Name ASC", "Name DESC", "Company ASC", "Company DESC" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(56, 69, 81));
        jLabel26.setText("Short By");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mobile", "First Name", "Last Name", "Email", "Company"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jLabel27.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(56, 69, 81));
        jLabel27.setText("Total GRN :");

        jLabel28.setFont(new java.awt.Font("Inter Medium", 0, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(56, 69, 81));
        jLabel28.setText("000");

        jButton42.setBackground(new java.awt.Color(24, 119, 242));
        jButton42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton42.setForeground(new java.awt.Color(255, 255, 255));
        jButton42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-print-25.png"))); // NOI18N
        jButton42.setText("Print Report");
        jButton42.setBorderPainted(false);
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel38Layout.createSequentialGroup()
                .addGap(0, 23, Short.MAX_VALUE)
                .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(jLabel26)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel25)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addGap(46, 46, 46)
                                    .addComponent(jLabel28))
                                .addGroup(jPanel8Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addComponent(jScrollPane3)
                        .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel24))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane8.addTab("tab5", jPanel8);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));

        jLabel29.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(56, 69, 81));
        jLabel29.setText("All Selling");

        jButton38.setBackground(new java.awt.Color(24, 119, 242));
        jButton38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton38.setForeground(new java.awt.Color(255, 255, 255));
        jButton38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-print-25.png"))); // NOI18N
        jButton38.setText("Print Report");
        jButton38.setBorderPainted(false);
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(381, 381, 381)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(264, Short.MAX_VALUE))
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                    .addContainerGap(758, Short.MAX_VALUE)
                    .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel29)
                .addGap(12, 12, 12))
            .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel20Layout.createSequentialGroup()
                    .addGap(5, 5, 5)
                    .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Invoice Number", "Added By", "Payment Method", "Total", "Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(jTable6);

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setText("Date");

        jDateChooser6.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser6.setToolTipText("");
        jDateChooser6.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser6PropertyChange(evt);
            }
        });

        jButton27.setBackground(new java.awt.Color(51, 51, 51));
        jButton27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton27.setForeground(new java.awt.Color(255, 255, 255));
        jButton27.setText("Clear");
        jButton27.setBorderPainted(false);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel47)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane6)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooser6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane8.addTab("tab7", jPanel11);

        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        jPanel25.setBackground(new java.awt.Color(255, 255, 255));
        jPanel25.setPreferredSize(new java.awt.Dimension(505, 52));

        jLabel42.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(56, 69, 81));
        jLabel42.setText("View All GRN");

        jButton39.setBackground(new java.awt.Color(24, 119, 242));
        jButton39.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton39.setForeground(new java.awt.Color(255, 255, 255));
        jButton39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-print-25.png"))); // NOI18N
        jButton39.setText("Print Report");
        jButton39.setBorderPainted(false);
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(407, Short.MAX_VALUE)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(285, 285, 285))
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                    .addContainerGap(750, Short.MAX_VALUE)
                    .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel25Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(7, Short.MAX_VALUE)))
        );

        jTextField7.setForeground(new java.awt.Color(30, 30, 30));
        jTextField7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTextField7MouseReleased(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField7KeyTyped(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel44.setText("Date");

        jDateChooser5.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser5.setToolTipText("");
        jDateChooser5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser5PropertyChange(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GRN ID", "Suplier", "Date", "Paid Amount", "Added By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
        }

        jButton24.setBackground(new java.awt.Color(51, 51, 51));
        jButton24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setText("Clear");
        jButton24.setBorderPainted(false);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel25, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton24, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(187, Short.MAX_VALUE)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(412, 412, 412))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane8.addTab("tab8", jPanel12);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));

        jLabel41.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(56, 69, 81));
        jLabel41.setText("Add New GRN");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap(327, Short.MAX_VALUE)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(294, 294, 294))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));

        jTextField10.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        jTextField10.setForeground(new java.awt.Color(24, 119, 242));
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setText("0");
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jTextField10, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));

        jButton23.setBackground(new java.awt.Color(24, 119, 242));
        jButton23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.setText("Save GRN");
        jButton23.setBorderPainted(false);
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel22.setLayout(new java.awt.GridLayout(1, 0));

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Brand", "Name", "Qty", "Buying Price", "Selling Price", "MFD", "EXP", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(jTable7);
        if (jTable7.getColumnModel().getColumnCount() > 0) {
            jTable7.getColumnModel().getColumn(0).setMaxWidth(75);
            jTable7.getColumnModel().getColumn(3).setMaxWidth(40);
        }

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));

        jButton22.setBackground(new java.awt.Color(24, 119, 242));
        jButton22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setText("Add to GRN");
        jButton22.setBorderPainted(false);
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(51, 51, 51));
        jButton25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton25.setForeground(new java.awt.Color(255, 255, 255));
        jButton25.setText("Clear All");
        jButton25.setBorderPainted(false);
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jFormattedTextField3.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField3ActionPerformed(evt);
            }
        });
        jFormattedTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField3KeyTyped(evt);
            }
        });

        jFormattedTextField4.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextField4ActionPerformed(evt);
            }
        });
        jFormattedTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField4KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextField3)
                    .addComponent(jFormattedTextField4)
                    .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(jButton25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));

        jLabel20.setForeground(new java.awt.Color(24, 119, 242));
        jLabel20.setText(".");

        jLabel21.setForeground(new java.awt.Color(24, 119, 242));
        jLabel21.setText(".");

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel45.setText("Brand Name :");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel46.setText("Product Name :");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel46)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addContainerGap())
        );

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));

        jTextField8.setForeground(new java.awt.Color(30, 30, 30));
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("MFD");

        jDateChooser4.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser4.setDateFormatString("yyyy-MM-dd");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("EXP");

        jDateChooser3.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser3.setDateFormatString("yyyy-MM-dd");

        jTextField5.setForeground(new java.awt.Color(30, 30, 30));

        jTextField6.setForeground(new java.awt.Color(30, 30, 30));

        jButton9.setBackground(new java.awt.Color(24, 119, 242));
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-13-16.png"))); // NOI18N
        jButton9.setBorderPainted(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(24, 119, 242));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-13-16.png"))); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jFormattedTextField6.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField6KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField6)
                    .addComponent(jFormattedTextField6)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(70, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGap(157, 157, 157)
                                .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(196, 196, 196))
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane7)
                                .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, 700, Short.MAX_VALUE)))
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(70, 70, 70))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 599, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jTabbedPane8.addTab("tab7", jPanel6);

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setPreferredSize(new java.awt.Dimension(505, 52));

        jLabel43.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(56, 69, 81));
        jLabel43.setText("View All Product");

        jButton40.setBackground(new java.awt.Color(24, 119, 242));
        jButton40.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-print-25.png"))); // NOI18N
        jButton40.setText("Print Report");
        jButton40.setBorderPainted(false);
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel36Layout = new javax.swing.GroupLayout(jPanel36);
        jPanel36.setLayout(jPanel36Layout);
        jPanel36Layout.setHorizontalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(285, 285, 285))
            .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel36Layout.createSequentialGroup()
                    .addContainerGap(750, Short.MAX_VALUE)
                    .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel36Layout.setVerticalGroup(
            jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
            .addGroup(jPanel36Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel36Layout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(7, Short.MAX_VALUE)))
        );

        jTextField12.setForeground(new java.awt.Color(30, 30, 30));
        jTextField12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTextField12MouseReleased(evt);
            }
        });
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField12KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField12KeyTyped(evt);
            }
        });

        jButton34.setBackground(new java.awt.Color(51, 51, 51));
        jButton34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 255, 255));
        jButton34.setText("Clear");
        jButton34.setBorderPainted(false);
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product ID", "Brand", "Category", "Product Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable8.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(jTable8);

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel34Layout.createSequentialGroup()
                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(666, 666, 666)
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane8)
                    .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE))
                .addGap(79, 79, 79))
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel34Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField12, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE))
        );

        jTabbedPane8.addTab("tab8", jPanel34);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));

        jLabel48.setFont(new java.awt.Font("Inter Display SemiBold", 1, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(56, 69, 81));
        jLabel48.setText("View All Stock");

        jButton41.setBackground(new java.awt.Color(24, 119, 242));
        jButton41.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton41.setForeground(new java.awt.Color(255, 255, 255));
        jButton41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-print-25.png"))); // NOI18N
        jButton41.setText("Print Report");
        jButton41.setBorderPainted(false);
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                .addContainerGap(372, Short.MAX_VALUE)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(167, 167, 167))
            .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                    .addContainerGap(760, Short.MAX_VALUE)
                    .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel48)
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel37Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabel49.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(56, 69, 81));
        jLabel49.setText("Sort By :");

        jComboBox6.setForeground(new java.awt.Color(30, 30, 30));
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stock ID ASC", "Stock ID DESC", "Product ID ASC", "Product ID DESC", "Brand ASC", "Brand DESC", "Name ASC", "Name DESC", "Selling Price ASC", "Selling Price DESC", "Quantity ASC", "Quantity DESC" }));
        jComboBox6.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox6ItemStateChanged(evt);
            }
        });
        jComboBox6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jComboBox6KeyReleased(evt);
            }
        });

        jLabel50.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(56, 69, 81));
        jLabel50.setText("Selling Price");

        jFormattedTextField7.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField7KeyTyped(evt);
            }
        });

        jLabel51.setFont(new java.awt.Font("Inter Medium", 1, 12)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(56, 69, 81));
        jLabel51.setText("to");

        jFormattedTextField8.setForeground(new java.awt.Color(30, 30, 30));
        jFormattedTextField8.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jFormattedTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jFormattedTextField8KeyTyped(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(24, 119, 242));
        jButton35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton35.setForeground(new java.awt.Color(255, 255, 255));
        jButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-13-16.png"))); // NOI18N
        jButton35.setBorderPainted(false);
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jDateChooser7.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser7.setDateFormatString("yyyy-MM-dd");
        jDateChooser7.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser7PropertyChange(evt);
            }
        });
        jDateChooser7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser7KeyReleased(evt);
            }
        });

        jLabel52.setFont(new java.awt.Font("Inter Display Medium", 1, 12)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(56, 69, 81));
        jLabel52.setText("EXP");

        jLabel53.setFont(new java.awt.Font("Inter Medium", 1, 12)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(56, 69, 81));
        jLabel53.setText("to");

        jDateChooser8.setForeground(new java.awt.Color(30, 30, 30));
        jDateChooser8.setDateFormatString("yyyy-MM-dd");
        jDateChooser8.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser8PropertyChange(evt);
            }
        });
        jDateChooser8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser8KeyReleased(evt);
            }
        });

        jButton36.setBackground(new java.awt.Color(24, 119, 242));
        jButton36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton36.setForeground(new java.awt.Color(255, 255, 255));
        jButton36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-13-16.png"))); // NOI18N
        jButton36.setBorderPainted(false);
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setBackground(new java.awt.Color(51, 51, 51));
        jButton37.setFont(new java.awt.Font("Open Sans Extrabold", 1, 12)); // NOI18N
        jButton37.setForeground(new java.awt.Color(255, 255, 255));
        jButton37.setText("Clear");
        jButton37.setBorderPainted(false);
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product Id", "Brand", "Name", "Selling Price", "Quantity", "MFD", "EXP"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable9.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(jTable9);

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addComponent(jLabel52)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel51)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel35Layout.createSequentialGroup()
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addComponent(jLabel49)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton37))
                            .addGroup(jPanel35Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(71, 71, 71))))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextField7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jDateChooser7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDateChooser8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane8.addTab("tab9", jPanel35);

        jPanel31.setLayout(new java.awt.BorderLayout());

        jPanel32.setPreferredSize(new java.awt.Dimension(1020, 40));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        jPanel31.add(jPanel32, java.awt.BorderLayout.PAGE_START);

        jPanel39.setPreferredSize(new java.awt.Dimension(1020, 30));

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel31.add(jPanel39, java.awt.BorderLayout.PAGE_END);

        jPanel40.setPreferredSize(new java.awt.Dimension(30, 468));

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 529, Short.MAX_VALUE)
        );

        jPanel31.add(jPanel40, java.awt.BorderLayout.LINE_START);

        jPanel41.setPreferredSize(new java.awt.Dimension(70, 468));

        jPanel46.setPreferredSize(new java.awt.Dimension(960, 240));
        jPanel46.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, 920, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel41Layout.createSequentialGroup()
                .addContainerGap(186, Short.MAX_VALUE)
                .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103))
        );

        jPanel31.add(jPanel41, java.awt.BorderLayout.LINE_END);

        jPanel42.setLayout(new java.awt.BorderLayout());

        jPanel50.setBackground(new java.awt.Color(255, 255, 255));
        jPanel50.setPreferredSize(new java.awt.Dimension(920, 123));
        jPanel50.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel31.setFont(new java.awt.Font("Inter Display SemiBold", 1, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(56, 69, 81));
        jLabel31.setText("Backup Data");
        jPanel50.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, -1, 30));

        jLabel54.setBackground(new java.awt.Color(102, 102, 102));
        jLabel54.setForeground(new java.awt.Color(153, 153, 153));
        jLabel54.setText("Secure and safegurd your critical information with automated backup.");
        jPanel50.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jButton13.setBackground(new java.awt.Color(24, 119, 242));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("Backup Now");
        jButton13.setBorderPainted(false);
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel50.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 35));

        jPanel42.add(jPanel50, java.awt.BorderLayout.PAGE_START);

        jPanel51.setLayout(new java.awt.BorderLayout());

        jPanel43.setPreferredSize(new java.awt.Dimension(920, 30));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 920, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel51.add(jPanel43, java.awt.BorderLayout.PAGE_START);

        jPanel44.setLayout(new java.awt.BorderLayout());

        jPanel45.setBackground(new java.awt.Color(255, 255, 255));
        jPanel45.setPreferredSize(new java.awt.Dimension(920, 123));
        jPanel45.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel55.setFont(new java.awt.Font("Inter Display SemiBold", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(56, 69, 81));
        jLabel55.setText("Restore Data");
        jPanel45.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 14, -1, 30));

        jLabel56.setBackground(new java.awt.Color(102, 102, 102));
        jLabel56.setForeground(new java.awt.Color(153, 153, 153));
        jLabel56.setText("Secure and safegurd your critical information with automated backup.");
        jPanel45.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jButton17.setBackground(new java.awt.Color(24, 119, 242));
        jButton17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton17.setForeground(new java.awt.Color(255, 255, 255));
        jButton17.setText("Restore Now");
        jButton17.setBorderPainted(false);
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });
        jPanel45.add(jButton17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, 35));

        jPanel44.add(jPanel45, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 920, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 253, Short.MAX_VALUE)
        );

        jPanel44.add(jPanel47, java.awt.BorderLayout.CENTER);

        jPanel51.add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel42.add(jPanel51, java.awt.BorderLayout.CENTER);

        jPanel31.add(jPanel42, java.awt.BorderLayout.CENTER);

        jTabbedPane8.addTab("tab10", jPanel31);

        jPanel3.add(jTabbedPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(-6, -35, 1020, 630));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1177, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        jTabbedPane8.setSelectedIndex(1);


    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        jTabbedPane8.setSelectedIndex(0);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        jTabbedPane8.setSelectedIndex(2);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        jTabbedPane8.setSelectedIndex(3);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        jTabbedPane8.setSelectedIndex(4);

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        addNewEmployee anemp = new addNewEmployee(this, rootPaneCheckingEnabled);
        anemp.setVisible(true);

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        addNewSupplier ans = new addNewSupplier();
        ans.setVisible(true);


    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String brand = jTextField3.getText();

        if (brand.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter brand name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `brand` WHERE `name` ='" + brand + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Brand already added", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {

                    if (jComboBox3.getSelectedIndex() == 0) {
                        //Select
                        MySQL.executeIUD("INSERT INTO `brand`(`name`) VALUES('" + brand + "')");
                        JOptionPane.showMessageDialog(this, "New brand added", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        int showConfirm = JOptionPane.showConfirmDialog(this, "Do you want to update brand?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                        if (showConfirm == JOptionPane.YES_OPTION) {
                            MySQL.executeIUD("UPDATE `brand` SET `name`='" + brand + "' WHERE `name` = '" + String.valueOf(jComboBox3.getSelectedItem()) + "'");
                            JOptionPane.showMessageDialog(this, "Brand update", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                    loadBrand();
                    jTextField3.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        jTabbedPane8.setSelectedIndex(5);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        String category = jTextField4.getText();

        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter category name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `category` WHERE `name` ='" + category + "'");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Category already added", "Warning", JOptionPane.WARNING_MESSAGE);

                } else {

                    if (jComboBox5.getSelectedIndex() == 0) {
                        //Select
                        MySQL.executeIUD("INSERT INTO `category`(`name`) VALUES('" + category + "')");
                        JOptionPane.showMessageDialog(this, "New brand added", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {

                        int showConfirm = JOptionPane.showConfirmDialog(this, "Do you want to update category?", "Update", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                        if (showConfirm == JOptionPane.YES_OPTION) {
                            MySQL.executeIUD("UPDATE `category` SET `name`='" + category + "' WHERE `name` = '" + String.valueOf(jComboBox5.getSelectedItem()) + "'");
                            JOptionPane.showMessageDialog(this, "Category update", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                    loadCategory();
                    jTextField4.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        SignIn signin = new SignIn();
        signin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        String pid = jTextField1.getText();
        String pname = jTextField2.getText();
        String pb = String.valueOf(jComboBox3.getSelectedItem());
        String pc = String.valueOf(jComboBox5.getSelectedItem());

        if (pid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter product id", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (pname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter product name", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (pb.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select product brand", "Warning", JOptionPane.WARNING_MESSAGE);
        } else if (pc.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please select product category", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {

            try {

                ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `product` WHERE `id` = '" + pid + "' OR (`name` = '" + pname + "' AND `brand_id` = '" + brandMap.get(pb) + "')");

                if (resultSet.next()) {
                    JOptionPane.showMessageDialog(this, "Product already added", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    MySQL.executeIUD("INSERT INTO `product`(`id`,`name`,`brand_id`,`category_id`,`datetime`) VALUES('" + pid + "','" + pname + "','" + brandMap.get(pb) + "','" + categoryMap.get(pc) + "','" + sdf.format(date) + "')");
                    loadProduct("datetime", "DESC");
                    resetProduct();
                    JOptionPane.showMessageDialog(this, "New product added", "Success", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        loadProduct("datetime", "DESC");
        loadStock();
        resetProduct();
        productId();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        int row = jTable4.getSelectedRow();
        jTextField1.setEditable(false);
        jButton14.setEnabled(false);

        String pid = String.valueOf(jTable4.getValueAt(row, 0));
        jTextField1.setText(pid);

        String pname = String.valueOf(jTable4.getValueAt(row, 3));
        jTextField2.setText(pname);

        String pb = String.valueOf(jTable4.getValueAt(row, 1));
        jComboBox3.setSelectedItem(pb);

        String pc = String.valueOf(jTable4.getValueAt(row, 2));
        jComboBox5.setSelectedItem(pc);

        loadStock();

    }//GEN-LAST:event_jTable4MouseClicked

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        int row = jTable4.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            try {

                String pid = jTextField1.getText();
                String pname = jTextField2.getText();
                String pb = String.valueOf(jComboBox3.getSelectedItem());
                String pc = String.valueOf(jComboBox3.getSelectedItem());

                if (pid.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter product id", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (pname.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please enter product name", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (pb.equals("Select")) {
                    JOptionPane.showMessageDialog(this, "Please select product brand", "Warning", JOptionPane.WARNING_MESSAGE);
                } else if (pc.equals("Select")) {
                    JOptionPane.showMessageDialog(this, "Please select product category", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `product` WHERE `name` = '" + pname + "' AND `brand_id` = '" + brandMap.get(pb) + "'");

                    if (resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "Change name or brand to update", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else {
                        MySQL.executeIUD("UPDATE `product` SET `name`='" + pname + "',`brand_id`='" + brandMap.get(pb) + "',`category_id`='" + cMap.get(pc) + "' WHERE `id`='" + pid + "'");
                        loadProduct("datetime", "DESC");
                        resetProduct();
                        JOptionPane.showMessageDialog(this, "Product Updated", "Success", JOptionPane.INFORMATION_MESSAGE);

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        jComboBox4.setSelectedItem(0);
        jFormattedTextField1.setText("");
        jFormattedTextField2.setText("");
        loadStock();

    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        resetStock();
        loadStock();

    }//GEN-LAST:event_jButton19ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        search();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int row = jTable3.getSelectedRow();
        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `grn` INNER JOIN `grn_item`"
                    + "ON `grn`.`id` = `grn_item`.`grn_id` WHERE `grn`.`suppler_mobile` = '" + String.valueOf(jTable3.getValueAt(row, 0)) + "'");

            double total = 0;

            HashMap<Long, Double> grns = new HashMap<>();

            while (resultSet.next()) {

                double qty = resultSet.getDouble("grn_item.qty");

                double buyingPrice = resultSet.getDouble("grn_item.buying_price");

                double itemTotal = qty * buyingPrice;

                //total = total + itemTotal;
                total += itemTotal;

                grns.put(resultSet.getLong("grn.id"), resultSet.getDouble("grn.paid_amount"));
            }

            double totalPaid = 0;

            for (Double paid : grns.values()) {

                totalPaid = totalPaid + paid;
            }

            jLabel28.setText(String.valueOf(grns.size()));
            jLabel24.setText(String.valueOf(totalPaid));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (evt.getClickCount() == 2) {

            String mobile = String.valueOf(jTable3.getValueAt(row, 0));
            String fname = String.valueOf(jTable3.getValueAt(row, 1));
            String lname = String.valueOf(jTable3.getValueAt(row, 2));
            String email = String.valueOf(jTable3.getValueAt(row, 3));
            String company = String.valueOf(jTable3.getValueAt(row, 4));

            addNewSuplier addnewsuplier = new addNewSuplier(this, true, mobile, fname, lname, email, company);
            addnewsuplier.setVisible(true);
        }

    }//GEN-LAST:event_jTable3MouseClicked

    private void jComboBox4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox4KeyReleased
    }//GEN-LAST:event_jComboBox4KeyReleased

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        loadStock();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased

    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void jFormattedTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyReleased

    }//GEN-LAST:event_jFormattedTextField2KeyReleased

    private void jDateChooser1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser1KeyReleased

    }//GEN-LAST:event_jDateChooser1KeyReleased

    private void jDateChooser2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser2KeyReleased

    }//GEN-LAST:event_jDateChooser2KeyReleased

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange

    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange

    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        if (evt.getClickCount() == 2) {

            int row = jTable2.getSelectedRow();
            String email1 = String.valueOf(jTable2.getValueAt(row, 0));
            String fname = String.valueOf(jTable2.getValueAt(row, 1));
            String lname = String.valueOf(jTable2.getValueAt(row, 2));
            String nic = String.valueOf(jTable2.getValueAt(row, 3));
            String mobile = String.valueOf(jTable2.getValueAt(row, 4));
            String password = String.valueOf(jTable2.getValueAt(row, 5));
            String gender = String.valueOf(jTable2.getValueAt(row, 6));
            String job = String.valueOf(jTable2.getValueAt(row, 7));

            updateNewEmployee updatenewemployee = new updateNewEmployee(this, true, email1, fname, lname, nic, mobile, password, gender, job);
            updatenewemployee.setVisible(true);

        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTextField7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField7MouseReleased
        loadGRN();
    }//GEN-LAST:event_jTextField7MouseReleased

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        loadGRN();
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jDateChooser5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser5PropertyChange
        loadGRN();
    }//GEN-LAST:event_jDateChooser5PropertyChange

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed

        jTextField7.setText("");
        jDateChooser5.setDate(null);
        loadGRN();
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        jTabbedPane8.setSelectedIndex(6);
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        try {
            String grnNumber = jTextField8.getText();
            String supplierMobile = jTextField5.getText();
            String paidAmount = jTextField10.getText();
            String minimumAmount = jTextField10.getText();
            String user = jLabel3.getText();

            if (supplierMobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Supplier Mobile is Empty", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (!supplierMobile.matches("^07[01245678]{1}[0-9]{7}$")) {
                JOptionPane.showMessageDialog(this, "It's not a valid Number", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (paidAmount.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter paid amount", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (Double.parseDouble(paidAmount) <= 0) {
                JOptionPane.showMessageDialog(this, "Please select Product", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (Double.parseDouble(paidAmount) < Double.parseDouble(minimumAmount)) {
                JOptionPane.showMessageDialog(this, "Paid amount must be greater than or equal to the minimum amount", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                MySQL.executeIUD("INSERT INTO `grn` (`id`,`suppler_mobile`, `added_date`, `paid_amount`, `employee_email`) VALUES("
                        + "'" + grnNumber + "', "
                        + "'" + supplierMobile + "', "
                        + "'" + date + "', "
                        + "'" + paidAmount + "', "
                        + "'" + user + "')");

                Iterator<Map.Entry<String, GRNItem>> iterator = grnItemMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, GRNItem> entry = iterator.next();
                    GRNItem grnItem = entry.getValue();

                    ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `stock` WHERE "
                            + "`product_id`= '" + grnItem.getProductId() + "' AND "
                            + "`selling_price`='" + grnItem.getSellingPrice() + "' AND"
                            + "`mfd`='" + sdf.format(grnItem.getMfd()) + "' AND"
                            + "`exp`='" + sdf.format(grnItem.getExp()) + "' ");

                    String sid = "";

                    if (resultSet.next()) {
                        sid = resultSet.getString("id");
                        String currentQty = resultSet.getString("qty");
                        String updatedQuantity = String.valueOf(Double.parseDouble(currentQty) + grnItem.getQty());
                        MySQL.executeIUD("UPDATE `stock` SET `qty` = '" + updatedQuantity + "' WHERE `id` = '" + sid + "'");
                    } else {
                        MySQL.executeIUD("INSERT INTO `stock`(`product_id`,`date_added`,`selling_price`,`qty`,`mfd`,`exp`) "
                                + "VALUES('" + grnItem.getProductId() + "','" + date + "','" + grnItem.getSellingPrice() + "','" + grnItem.getQty() + "','" + sdf.format(grnItem.getMfd()) + "','" + sdf.format(grnItem.getExp()) + "')");

                        ResultSet resultSet2 = MySQL.executeSearch("SELECT * FROM `stock` WHERE "
                                + "`product_id`= '" + grnItem.getProductId() + "' AND "
                                + "`selling_price`='" + grnItem.getSellingPrice() + "' AND"
                                + "`mfd`='" + sdf.format(grnItem.getMfd()) + "' AND"
                                + "`exp`='" + sdf.format(grnItem.getExp()) + "' ");

                        if (resultSet2.next()) {
                            sid = resultSet2.getString("id");
                        }
                    }

                    MySQL.executeIUD("INSERT INTO `grn_item`"
                            + "(`qty`,`buying_price`,`grn_id`,`stock_id`) VALUES('" + grnItem.getQty() + "','" + grnItem.getBuyingPrice() + "','" + grnNumber + "','" + sid + "')");

                    iterator.remove();
                }

                JOptionPane.showMessageDialog(this,
                        "<html><div style='text-align: center;'>Successful Added GRN</div></html>",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                resetGRN();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        String supplier = jTextField5.getText();
        String product = jTextField6.getText();
        String qty = jFormattedTextField6.getText();
        String buying_price = jFormattedTextField3.getText();
        String selling_price = jFormattedTextField4.getText();
        Date mfd = jDateChooser4.getDate();
        Date exp = jDateChooser3.getDate();

        if (supplier.isEmpty() || product.isEmpty() || qty.isEmpty()
                || buying_price.isEmpty() || selling_price.isEmpty()
                || mfd == null || exp == null) {
            JOptionPane.showMessageDialog(this, "All fields must be filled", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date today = new Date();
        if (mfd.after(today)) {
            JOptionPane.showMessageDialog(this, "Manufacturing date must be today or earlier", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (exp.before(today)) {
            JOptionPane.showMessageDialog(this, "Expiry date must be today or later", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate prices
        double buyingPrice;
        double sellingPrice;
        double quantity;
        try {
            buyingPrice = Double.parseDouble(buying_price);
            sellingPrice = Double.parseDouble(selling_price);
            quantity = Double.parseDouble(qty);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format in quantity or prices", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (buyingPrice >= sellingPrice) {
            JOptionPane.showMessageDialog(this, "Selling price must be greater than buying price", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        GRNItem grnItem = new GRNItem();
        grnItem.setProductId(product);
        grnItem.setBrandName(jLabel20.getText());
        grnItem.setProductName(jLabel21.getText());
        grnItem.setQty(quantity);
        grnItem.setBuyingPrice(buyingPrice);
        grnItem.setSellingPrice(sellingPrice);
        grnItem.setMfd(mfd);
        grnItem.setExp(exp);

        if (grnItemMap.get(product) == null) {
            grnItemMap.put(product, grnItem);
            loadGRNItems();
            jButton4.setEnabled(false);
        } else {
            GRNItem found = grnItemMap.get(product);

            if (found.getExp().compareTo(exp) == 0
                    && found.getMfd().compareTo(mfd) == 0
                    && found.getBuyingPrice() == buyingPrice
                    && found.getSellingPrice() == sellingPrice) {
                // Show confirmation dialog
                int response = JOptionPane.showConfirmDialog(this,
                        "The product already exists in the table. Do you want to increase the quantity?",
                        "Update Quantity",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (response == JOptionPane.OK_OPTION) {
                    // Update quantity if user confirms
                    found.setQty(found.getQty() + quantity);
                    loadGRNItems();
                    jButton4.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(this, "GRN item already exists with different dates and prices", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jFormattedTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyReleased
        /**
         * String total = jLabel18.getText(); String payment =
         * jFormattedTextField4.getText();
         *
         * if (payment.isEmpty()) { payment = "0"; } else if
         * (!payment.matches("^(0|[1-9]\\d*)?(\\.\\d+)?(?<=\\d)$")) {
         *
         * jLabel19.setText("INVALID"); jLabel19.setForeground(Color.RED); }
         * else {
         *
         * double balance = Double.parseDouble(payment) -
         * Double.parseDouble(total); jLabel19.setText(String.valueOf(balance));
         * jLabel19.setForeground(Color.BLACK); }
         *
         */
    }//GEN-LAST:event_jFormattedTextField3KeyReleased

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed

    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        loadProduct stock = new loadProduct();
        stock.setVisible(true);
        stock.setgrn(this);
        jFormattedTextField6.setText("");
        jDateChooser4.setDate(null);
        jDateChooser3.setDate(null);
        jFormattedTextField3.setText("");
        jFormattedTextField4.setText("");

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        loadSupplier sr = new loadSupplier();
        sr.setVisible(true);
        sr.setgrn(this);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        loadStock();
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox5ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        resetGRN();
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jFormattedTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField3KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }

    }//GEN-LAST:event_jFormattedTextField3KeyTyped

    private void jFormattedTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField4ActionPerformed

    private void jFormattedTextField4KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField4KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }

    }//GEN-LAST:event_jFormattedTextField4KeyTyped

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }

    }//GEN-LAST:event_jTextField7KeyTyped

    private void jFormattedTextField6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField6KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextField6KeyTyped

    private void jDateChooser6PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser6PropertyChange
        loadSelling1();
    }//GEN-LAST:event_jDateChooser6PropertyChange

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        jDateChooser6.setDate(null);

        loadSelling1();
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jFormattedTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextField1KeyTyped

    private void jFormattedTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField2KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextField2KeyTyped

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        jTabbedPane8.setSelectedIndex(7);
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        jTabbedPane8.setSelectedIndex(8);
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jTextField12MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField12MouseReleased
        loadProductall();
    }//GEN-LAST:event_jTextField12MouseReleased

    private void jTextField12KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyReleased
        loadProductall();
    }//GEN-LAST:event_jTextField12KeyReleased

    private void jTextField12KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyTyped
        loadProductall();
    }//GEN-LAST:event_jTextField12KeyTyped

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        jTextField12.setText("");
        loadProductall();
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jComboBox6ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox6ItemStateChanged
        loadStock1();
    }//GEN-LAST:event_jComboBox6ItemStateChanged

    private void jComboBox6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox6KeyReleased
        loadStock1();

    }//GEN-LAST:event_jComboBox6KeyReleased

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        jDateChooser7.setDate(null);
        jDateChooser8.setDate(null);
        loadStock1();

    }//GEN-LAST:event_jButton35ActionPerformed

    private void jDateChooser7PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser7PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser7PropertyChange

    private void jDateChooser7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser7KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser7KeyReleased

    private void jDateChooser8PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser8PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser8PropertyChange

    private void jDateChooser8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser8KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser8KeyReleased

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        jComboBox6.setSelectedItem(0);
        jFormattedTextField7.setText("");
        jFormattedTextField8.setText("");
        loadStock1();
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        resetStocknew();
        loadStock1();
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jFormattedTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField3ActionPerformed

    private void jFormattedTextField7KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField7KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextField7KeyTyped

    private void jFormattedTextField8KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField8KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jFormattedTextField8KeyTyped

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse", "root", "#Lucky2003sql");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", date);

            JasperPrint report = JasperFillManager.fillReport("src/report/MedipluseSelling.jasper", params, connection);

            JasperViewer.viewReport(report, false);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse", "root", "#Lucky2003sql");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", date);

            JasperPrint report = JasperFillManager.fillReport("src/report/MedipluseViewGRN.jasper", params, connection);

            JasperViewer.viewReport(report, false);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse", "root", "#Lucky2003sql");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", date);

            JasperPrint report = JasperFillManager.fillReport("src/report/MedipluseProduct.jasper", params, connection);

            JasperViewer.viewReport(report, false);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse", "root", "#Lucky2003sql");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", date);

            JasperPrint report = JasperFillManager.fillReport("src/report/MedipluseStock.jasper", params, connection);

            JasperViewer.viewReport(report, false);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse", "root", "#Lucky2003sql");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", date);

            JasperPrint report = JasperFillManager.fillReport("src/report/MedipluseEmployee.jasper", params, connection);

            JasperViewer.viewReport(report, false);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/medipluse", "root", "#Lucky2003sql");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", date);

            JasperPrint report = JasperFillManager.fillReport("src/report/MedipluseSupplier.jasper", params, connection);

            JasperViewer.viewReport(report, false);

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        jTabbedPane8.setSelectedIndex(9);

    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        backup Backup = new backup(null, true);
        Backup.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        restore Restore = new restore(null, true);
        Restore.setVisible(true);    }//GEN-LAST:event_jButton17ActionPerformed

    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new adminDashboard().setVisible(true);
            }
        });
    }
    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
    private com.toedter.calendar.JDateChooser jDateChooser6;
    private com.toedter.calendar.JDateChooser jDateChooser7;
    private com.toedter.calendar.JDateChooser jDateChooser8;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField4;
    private javax.swing.JFormattedTextField jFormattedTextField6;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables

    private void resetProduct() {

        jTextField1.setText("");
        jTextField2.setText("");
        jComboBox3.setSelectedIndex(0);
        jComboBox5.setSelectedIndex(0);
        jTextField1.setEditable(true);
        jButton14.setEnabled(true);
        productId();
    }

    private void resetStock() {
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jFormattedTextField1.setText("");
        jFormattedTextField2.setText("");
        jComboBox4.setSelectedIndex(0);
    }

    private void resetStocknew() {
        jDateChooser7.setDate(null);
        jDateChooser8.setDate(null);
        jFormattedTextField7.setText("");
        jFormattedTextField8.setText("");
        jComboBox6.setSelectedIndex(0);
    }

    private void search() {

        if (jComboBox2.getSelectedIndex() == 0) {
            loadSpplier("first_name", "ASC");
        } else if (jComboBox2.getSelectedIndex() == 1) {
            loadSpplier("first_name", "DESC");
        } else if (jComboBox2.getSelectedIndex() == 2) {
            loadSpplier("company`.`name", "ASC");
        } else if (jComboBox2.getSelectedIndex() == 3) {
            loadSpplier("company`.`name", "DESC");
        }
    }

    private void resetGRN() {
        Long id = System.currentTimeMillis();
        jTextField8.setText(String.valueOf(id));

        jTextField5.setText("");
        jTextField6.setText("");
        jFormattedTextField6.setText("");
        jDateChooser4.setDate(null);
        jDateChooser3.setDate(null);
        jLabel20.setText("");
        jLabel21.setText("");
        jFormattedTextField3.setText("");
        jFormattedTextField4.setText("");
        jTextField10.setText("0");
        jButton4.setEnabled(true);

        DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
        model.setRowCount(0);

        grnItemMap.clear();
    }

}
