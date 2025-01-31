package model;

import java.awt.BorderLayout;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import org.jfree.chart.renderer.category.BarRenderer;
import java.awt.Color;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;

/**
 *
 * @author Laky
 */
public class chartAdmin {

    public static void loadChartToJPanel(javax.swing.JPanel panel) {
        ChartPanel chartPanel = createChartPanel(panel.getSize().width, panel.getSize().height);
        if (chartPanel != null) {
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            panel.setOpaque(false);
            panel.add(chartPanel, BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }

    private static ChartPanel createChartPanel(int width, int height) {
        try {
            DefaultCategoryDataset dataset = createDataset();

            JFreeChart chart = ChartFactory.createBarChart(
                    "Monthly Sales Report", "Month", "Sales",
                    dataset, PlotOrientation.VERTICAL, false, true, false
            );

            chart.setTitle(new TextTitle("Monthly Sales Report", new java.awt.Font("Inter", java.awt.Font.BOLD, 14)));
            chart.getTitle().setPaint(new Color(56, 69, 81));

            BarRenderer renderer = new BarRenderer();
            renderer.setSeriesPaint(0, new Color(24, 119, 242));
            chart.getCategoryPlot().setRenderer(renderer);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setSize(width, height);
            chartPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
            return chartPanel;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Map<String, Double> salesData = new HashMap<>();
        DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("MMMM");

        try {
            ResultSet rs = MySQL.executeSearch("SELECT DATE_FORMAT(date, '%Y-%m') as sale_month, SUM(total_amount) as total_sales "
                    + "FROM invoice "
                    + "GROUP BY sale_month "
                    + "ORDER BY sale_month");

            while (rs.next()) {
                String month = rs.getString("sale_month");
                double sales = rs.getDouble("total_sales");
                salesData.put(month, sales);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        for (int i = 0; i < 6; i++) {
            String dbKey = currentMonth.format(dbFormatter);
            String displayKey = currentMonth.format(displayFormatter);

            dataset.addValue(salesData.getOrDefault(dbKey, 0.0), "Sales", displayKey);
            currentMonth = currentMonth.plusMonths(1);
        }

        return dataset;
    }
}
