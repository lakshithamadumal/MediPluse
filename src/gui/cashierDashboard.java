/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.InvoiceItem;
import model.MySQL;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Laky
 */
public class cashierDashboard extends javax.swing.JFrame {

    HashMap<String, InvoiceItem> invoiceItemMap = new HashMap<>();
    HashMap<String, String> payementMethodMap = new HashMap<>();

    public cashierDashboard(String email) {
        initComponents();
        jLabel3.setText(email);
        hint();
        jButton2.grabFocus();
        jTextField4.setEditable(false);
        jTextField7.setEditable(false);
        jTextField6.setEditable(false);
        jTextField3.setEditable(false);
        jTextField8.setEditable(false);
        jTextField1.setEditable(false);
        generateInvoiceID();
        loadPaymentMethods();
    }

    private cashierDashboard() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void generateInvoiceID() {

        long id = System.currentTimeMillis();
        jTextField1.setText(String.valueOf(id));

    }

    private void loadPaymentMethods() {

        try {

            ResultSet resultSet = MySQL.executeSearch("SELECT * FROM `payment_method`");

            Vector<String> vector = new Vector<>();

            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
                payementMethodMap.put(resultSet.getString("name"), resultSet.getString("id"));

            }

            DefaultComboBoxModel model = new DefaultComboBoxModel<>(vector);
            jComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void reset() {
        Long id = System.currentTimeMillis();
        jTextField1.setText(String.valueOf(id));

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        invoiceItemMap.clear();

        jTextField8.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField7.setText("");
        jTextField2.setText("");
        jLabel5.setText("0");
        jComboBox1.setSelectedIndex(0);
        jTextField5.setText("");
        jTextField6.setText(paymentMethod);

    }

    private void loadInvoiceItem() {

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        total = 0;

        for (InvoiceItem invoiceItem : invoiceItemMap.values()) {

            Vector<String> vector = new Vector<>();
            vector.add(invoiceItem.getStockID());
            vector.add(invoiceItem.getProductName());
            vector.add(invoiceItem.getSellingPrice());
            vector.add(invoiceItem.getProductQty());

            double itemTotal = Double.parseDouble(invoiceItem.getProductQty()) * Double.parseDouble(invoiceItem.getSellingPrice());
            total += itemTotal;
            vector.add(String.valueOf(itemTotal));

            model.addRow(vector);

        }

        jLabel5.setText(String.valueOf(total));

        //2
        calculate();

    }

    private double total = 0;
    private double payment = 0;
    private double balance = 0;
    private String paymentMethod = "Select";

    private void calculate() {

        if (jTextField5.getText().isEmpty()) {
            payment = 0;

        } else {
            payment = Double.parseDouble(jTextField5.getText());
        }

        total = Double.parseDouble(jLabel5.getText());

        paymentMethod = String.valueOf(jComboBox1.getSelectedItem());

        if (paymentMethod.equals("Cash")) { //Cash

            jTextField5.setEditable(true);
            balance = payment - total;

            if (balance < 0) {
                jButton3.setEnabled(false);

            } else {

                jButton3.setEnabled(true);
            }

        } else { //Card

            payment = total;
            balance = 0;
            jTextField5.setText(String.valueOf(payment));
            jTextField5.setEditable(false);
            jButton3.setEnabled(true);

        }

        jTextField6.setText(String.valueOf(balance));

    }

    private void hint() {
        if (jTextField3 != null) {
            jTextField3.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Product Name");
        }
        if (jTextField2 != null) {
            jTextField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Qty");
        }
        if (jTextField5 != null) {
            jTextField5.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Paid");
        }
        if (jTextField4 != null) {
            jTextField4.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Selling Price");
        }
        if (jTextField7 != null) {
            jTextField7.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "InStock");
        }
        if (jTextField8 != null) {
            jTextField8.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "SID");
        }

    }

    //product id
    public JTextField getjTextField3() {
        return jTextField3;
    }

    //qty
    public JTextField getjTextField2() {
        return jTextField2;
    }

    //sellingPrice
    public JTextField getjTextField4() {
        return jTextField4;
    }

    //Instock qty
    public JTextField getjTextField7() {
        return jTextField7;
    }

    //stockID
    public JTextField getjTextField8() {
        return jTextField8;
    }

    //User Email
    public JLabel getUserEmail() {
        return jLabel3;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jButton9999 = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jTextField8 = new javax.swing.JTextField();
        jPanel30 = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jTextField7 = new javax.swing.JTextField();
        jPanel32 = new javax.swing.JPanel();
        jTextField4 = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel37 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel43 = new javax.swing.JPanel();
        jTextField6 = new javax.swing.JTextField();
        jPanel44 = new javax.swing.JPanel();
        jTextField5 = new javax.swing.JTextField();
        jPanel41 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(44, 73, 114));
        jPanel1.setPreferredSize(new java.awt.Dimension(1099, 70));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(44, 73, 114));
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 70));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_new.png"))); // NOI18N
        jPanel6.add(jLabel1);

        jPanel1.add(jPanel6, java.awt.BorderLayout.LINE_START);

        jPanel7.setBackground(new java.awt.Color(44, 73, 114));
        jPanel7.setPreferredSize(new java.awt.Dimension(250, 70));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(44, 73, 114));
        jPanel13.setPreferredSize(new java.awt.Dimension(80, 70));
        jPanel13.setLayout(new java.awt.GridLayout(1, 0));

        jLabel2.setFont(new java.awt.Font("Inter", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Welcome");
        jPanel13.add(jLabel2);

        jPanel7.add(jPanel13, java.awt.BorderLayout.LINE_START);

        jPanel14.setBackground(new java.awt.Color(44, 73, 114));
        jPanel14.setPreferredSize(new java.awt.Dimension(35, 70));

        jButton9999.setBackground(new java.awt.Color(255, 0, 0));
        jButton9999.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8-logout-20.png"))); // NOI18N
        jButton9999.setBorderPainted(false);
        jButton9999.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9999ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jButton9999, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jButton9999, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel7.add(jPanel14, java.awt.BorderLayout.LINE_END);

        jPanel15.setBackground(new java.awt.Color(44, 73, 114));
        jPanel15.setLayout(new java.awt.GridLayout(1, 0));

        jLabel3.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("abc@gmail.com");
        jPanel15.add(jLabel3);

        jPanel7.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel7, java.awt.BorderLayout.LINE_END);

        jPanel8.setBackground(new java.awt.Color(44, 73, 114));
        jPanel8.setLayout(new java.awt.GridLayout(1, 0));

        jLabel30.setFont(new java.awt.Font("Inter ExtraBold", 1, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("Cashier Dashboard");
        jPanel8.add(jLabel30);

        jPanel1.add(jPanel8, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setPreferredSize(new java.awt.Dimension(1099, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1099, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel4.setPreferredSize(new java.awt.Dimension(20, 700));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(1079, 15));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1079, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel11.setPreferredSize(new java.awt.Dimension(250, 695));
        jPanel11.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(250, 40));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel11.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel19.setPreferredSize(new java.awt.Dimension(250, 20));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel19, java.awt.BorderLayout.PAGE_START);

        jPanel20.setLayout(new java.awt.BorderLayout());

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setPreferredSize(new java.awt.Dimension(250, 160));
        jPanel21.setLayout(new java.awt.BorderLayout());

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel23.setLayout(new java.awt.BorderLayout());

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setPreferredSize(new java.awt.Dimension(40, 40));
        jPanel28.setLayout(new java.awt.CardLayout(3, 3));

        jButton2.setBackground(new java.awt.Color(24, 119, 242));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search-3-16.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel28.add(jButton2, "card2");

        jPanel23.add(jPanel28, java.awt.BorderLayout.LINE_END);

        jPanel29.setBackground(new java.awt.Color(255, 255, 255));
        jPanel29.setPreferredSize(new java.awt.Dimension(60, 40));
        jPanel29.setLayout(new java.awt.CardLayout(3, 3));
        jPanel29.add(jTextField8, "card2");

        jPanel23.add(jPanel29, java.awt.BorderLayout.LINE_START);

        jPanel30.setBackground(new java.awt.Color(255, 255, 255));
        jPanel30.setLayout(new java.awt.CardLayout(3, 3));
        jPanel30.add(jTextField3, "card2");

        jPanel23.add(jPanel30, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel23, java.awt.BorderLayout.PAGE_START);

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel24.setLayout(new java.awt.CardLayout(3, 3));

        jButton1.setBackground(new java.awt.Color(24, 119, 242));
        jButton1.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add to Invoice");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel24.add(jButton1, "card2");

        jPanel21.add(jPanel24, java.awt.BorderLayout.PAGE_END);

        jPanel25.setLayout(new java.awt.BorderLayout());

        jPanel26.setBackground(new java.awt.Color(255, 255, 255));
        jPanel26.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel31.setBackground(new java.awt.Color(255, 255, 255));
        jPanel31.setLayout(new java.awt.CardLayout(3, 3));
        jPanel31.add(jTextField7, "card2");

        jPanel26.add(jPanel31, java.awt.BorderLayout.LINE_END);

        jPanel32.setBackground(new java.awt.Color(255, 255, 255));
        jPanel32.setLayout(new java.awt.CardLayout(3, 3));
        jPanel32.add(jTextField4, "card2");

        jPanel26.add(jPanel32, java.awt.BorderLayout.CENTER);

        jPanel25.add(jPanel26, java.awt.BorderLayout.PAGE_START);

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setLayout(new java.awt.CardLayout(3, 3));

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });
        jPanel27.add(jTextField2, "card2");

        jPanel25.add(jPanel27, java.awt.BorderLayout.CENTER);

        jPanel21.add(jPanel25, java.awt.BorderLayout.CENTER);

        jPanel20.add(jPanel21, java.awt.BorderLayout.PAGE_START);

        jPanel22.setLayout(new java.awt.BorderLayout());

        jPanel33.setPreferredSize(new java.awt.Dimension(250, 20));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel22.add(jPanel33, java.awt.BorderLayout.PAGE_START);

        jPanel34.setBackground(new java.awt.Color(255, 255, 255));
        jPanel34.setPreferredSize(new java.awt.Dimension(250, 80));
        jPanel34.setLayout(new java.awt.BorderLayout());

        jPanel36.setBackground(new java.awt.Color(255, 255, 255));
        jPanel36.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel36.setLayout(new java.awt.CardLayout(3, 3));

        jButton3.setBackground(new java.awt.Color(24, 119, 242));
        jButton3.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/invoice-24.png"))); // NOI18N
        jButton3.setText("Print Invoice");
        jButton3.setBorderPainted(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel36.add(jButton3, "card2");

        jPanel34.add(jPanel36, java.awt.BorderLayout.PAGE_START);

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setLayout(new java.awt.CardLayout(3, 3));

        jButton6.setBackground(new java.awt.Color(255, 0, 0));
        jButton6.setFont(new java.awt.Font("Inter", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/cancel-24.png"))); // NOI18N
        jButton6.setText("Cancel Invoice");
        jButton6.setBorderPainted(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel37.add(jButton6, "card2");

        jPanel34.add(jPanel37, java.awt.BorderLayout.CENTER);

        jPanel22.add(jPanel34, java.awt.BorderLayout.PAGE_END);

        jPanel35.setLayout(new java.awt.BorderLayout());

        jPanel38.setPreferredSize(new java.awt.Dimension(250, 20));

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel35.add(jPanel38, java.awt.BorderLayout.PAGE_END);

        jPanel39.setLayout(new java.awt.BorderLayout());

        jPanel40.setBackground(new java.awt.Color(255, 255, 255));
        jPanel40.setPreferredSize(new java.awt.Dimension(250, 120));
        jPanel40.setLayout(new java.awt.BorderLayout());

        jPanel42.setBackground(new java.awt.Color(255, 255, 255));
        jPanel42.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel42.setLayout(new java.awt.CardLayout(3, 3));

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel42.add(jComboBox1, "card2");

        jPanel40.add(jPanel42, java.awt.BorderLayout.PAGE_START);

        jPanel43.setBackground(new java.awt.Color(255, 255, 255));
        jPanel43.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel43.setLayout(new java.awt.CardLayout());

        jTextField6.setText("Given Amount");
        jPanel43.add(jTextField6, "card2");

        jPanel40.add(jPanel43, java.awt.BorderLayout.PAGE_END);

        jPanel44.setBackground(new java.awt.Color(255, 255, 255));
        jPanel44.setLayout(new java.awt.CardLayout(3, 3));

        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField5KeyTyped(evt);
            }
        });
        jPanel44.add(jTextField5, "card2");

        jPanel40.add(jPanel44, java.awt.BorderLayout.CENTER);

        jPanel39.add(jPanel40, java.awt.BorderLayout.PAGE_END);

        jPanel41.setLayout(new java.awt.BorderLayout());

        jPanel45.setPreferredSize(new java.awt.Dimension(250, 20));

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 250, Short.MAX_VALUE)
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel41.add(jPanel45, java.awt.BorderLayout.PAGE_END);

        jPanel46.setBackground(new java.awt.Color(255, 255, 255));
        jPanel46.setLayout(new java.awt.BorderLayout());

        jPanel48.setBackground(new java.awt.Color(255, 255, 255));
        jPanel48.setLayout(new java.awt.GridLayout(2, 1));

        jLabel4.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Total Price");
        jPanel48.add(jLabel4);

        jLabel5.setBackground(new java.awt.Color(24, 119, 242));
        jLabel5.setFont(new java.awt.Font("Inter", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(24, 119, 242));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("0");
        jPanel48.add(jLabel5);

        jPanel46.add(jPanel48, java.awt.BorderLayout.CENTER);

        jPanel41.add(jPanel46, java.awt.BorderLayout.CENTER);

        jPanel39.add(jPanel41, java.awt.BorderLayout.CENTER);

        jPanel35.add(jPanel39, java.awt.BorderLayout.CENTER);

        jPanel22.add(jPanel35, java.awt.BorderLayout.CENTER);

        jPanel20.add(jPanel22, java.awt.BorderLayout.CENTER);

        jPanel18.add(jPanel20, java.awt.BorderLayout.CENTER);

        jPanel11.add(jPanel18, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel11, java.awt.BorderLayout.LINE_END);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel16.setPreferredSize(new java.awt.Dimension(20, 695));

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 695, Short.MAX_VALUE)
        );

        jPanel12.add(jPanel16, java.awt.BorderLayout.LINE_END);

        jPanel17.setLayout(new java.awt.GridLayout(1, 0));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Product Name", "Price", "Qty", "Total"
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

        jPanel17.add(jScrollPane1);

        jPanel12.add(jPanel17, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel12, java.awt.BorderLayout.CENTER);

        jPanel5.add(jPanel10, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        loadStock stock = new loadStock();
        stock.setVisible(true);
        stock.setinvoice(this);
        jTextField2.setText("");
        jTextField5.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String StockID = jTextField8.getText();
        String ProductName = jTextField3.getText();
        String qty = jTextField2.getText();
        String sellingPrice = jTextField4.getText();
        String currentStock = jTextField7.getText(); // Available stock quantity

        if (sellingPrice.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            jTextField4.grabFocus();
            return;
        }

        if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add product quantity.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            jTextField2.grabFocus();
            return;
        }

        try {
            double quantity = Double.parseDouble(qty);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Product quantity must be greater than zero.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jTextField2.grabFocus();
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            jTextField2.grabFocus();
            return;
        }

        try {
            double availableStock = Double.parseDouble(currentStock);
            double requestedQuantity = Double.parseDouble(qty);

            if (requestedQuantity > availableStock) {
                JOptionPane.showMessageDialog(this, "Quantity exceeds available stock.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jTextField2.grabFocus();
                return;
            }

            jTextField7.setText(String.valueOf(availableStock - requestedQuantity));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid stock value. Please check available stock.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setProductName(ProductName);
        invoiceItem.setProductQty(qty);
        invoiceItem.setSellingPrice(sellingPrice);
        invoiceItem.setStockID(StockID);

        if (invoiceItemMap.get(StockID) == null) {
            invoiceItemMap.put(StockID, invoiceItem);
        } else {
            InvoiceItem found = invoiceItemMap.get(StockID);

            int option = JOptionPane.showConfirmDialog(this, "Do You Want to Update the Quantity of Product :" + ProductName, "Message",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                found.setProductQty(String.valueOf(Double.parseDouble(found.getProductQty()) + Double.parseDouble(qty)));
            }
        }

        loadInvoiceItem();
        jTextField5.grabFocus();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (jTextField1.getText().isEmpty() || jTextField2.getText().isEmpty() || jTextField4.getText().isEmpty() || jTextField5.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String invoiceID = jTextField1.getText();
            String UserEmail = jLabel3.getText();
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String totalAmount = jLabel5.getText();
            String paymentMethodID = payementMethodMap.get(String.valueOf(jComboBox1.getSelectedItem()));

            for (InvoiceItem invoiceItem : invoiceItemMap.values()) {
                ResultSet resultSet = MySQL.executeSearch("SELECT `qty` FROM `stock` WHERE `id` = '" + invoiceItem.getStockID() + "'");
                int availableQty = 0;
                if (resultSet.next()) {
                    availableQty = resultSet.getInt("qty");
                } else {
                    JOptionPane.showMessageDialog(this, "No stock record found for item: " + invoiceItem.getProductName(), "Stock Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int productQty = (int) Double.parseDouble(invoiceItem.getProductQty());

                if (availableQty < productQty) {
                    JOptionPane.showMessageDialog(this, "Insufficient stock for item: " + invoiceItem.getProductName(), "Stock Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            MySQL.executeIUD("INSERT INTO `invoice` VALUES('" + invoiceID + "','" + UserEmail + "','" + paymentMethodID + "',"
                    + "'" + totalAmount + "','" + dateTime + "')");

            for (InvoiceItem invoiceItem : invoiceItemMap.values()) {
                MySQL.executeIUD("INSERT INTO `invoice_item` (`invoice_id`,`stock_id`,`qty`) "
                        + "VALUES ('" + invoiceID + "','" + invoiceItem.getStockID() + "','" + invoiceItem.getProductQty() + "')");

                MySQL.executeIUD("UPDATE `stock` SET `qty` = `qty` - '" + invoiceItem.getProductQty() + "' WHERE `id` = '" + invoiceItem.getStockID() + "'");
            }

            String path = "src//report//MedipluseCahsierInvoice.jasper";

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", jTextField1.getText());
            params.put("Parameter2", dateTime);
            params.put("Parameter3", jLabel3.getText());
            params.put("Parameter4", String.valueOf(jComboBox1.getSelectedItem()));
            params.put("Parameter9", jLabel5.getText());
            params.put("Parameter10", jTextField5.getText());
            params.put("Parameter11", jTextField6.getText());

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());

            JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, dataSource);
            reset();

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        reset();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9999ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9999ActionPerformed
        SignIn signin = new SignIn();
        signin.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton9999ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
         jButton3.grabFocus();
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
         calculate();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        jButton3.grabFocus();
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
       calculate();
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyTyped
        char c = evt.getKeyChar();

        if (!Character.isDigit(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_jTextField5KeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new cashierDashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9999;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
