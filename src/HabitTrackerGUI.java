import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * HabitTrackerGUI - Java Swing Arayüzü
 * Mevcut HabitTracker sistemine görsel arayüz ekler.
 */
public class HabitTrackerGUI extends JFrame {

    private HabitTracker tracker;
    private DefaultTableModel tableModel;
    private JTable habitTable;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Tablo sütunları
    private static final String[] COLUMNS = {"#", "İsim", "Tip", "Kategori", "Toplam", "Bugün"};

    public HabitTrackerGUI() {
        tracker = new HabitTracker("habits_data.json");
        tracker.loadFromFile();

        setTitle("Alışkanlık Takip Sistemi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);

        initComponents();
        refreshTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));

        // Üst başlık
        JLabel titleLabel = new JLabel("Alışkanlık Takip Sistemi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Orta: tablo
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // tabloya direkt yazı yazılamaz
            }
        };
        habitTable = new JTable(tableModel);
        habitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        habitTable.getColumnModel().getColumn(0).setMaxWidth(35);
        habitTable.getColumnModel().getColumn(4).setMaxWidth(65);
        habitTable.getColumnModel().getColumn(5).setMaxWidth(65);
        habitTable.setRowHeight(22);

        JScrollPane scrollPane = new JScrollPane(habitTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addBtn    = new JButton("+ Yeni Ekle");
        JButton doneBtn   = new JButton("✓ Bugün Tamamla");
        JButton deleteBtn = new JButton("✗ Sil");
        JButton statsBtn  = new JButton("İstatistikler");

        addBtn.setPreferredSize(new Dimension(130, 32));
        doneBtn.setPreferredSize(new Dimension(150, 32));
        deleteBtn.setPreferredSize(new Dimension(80, 32));
        statsBtn.setPreferredSize(new Dimension(120, 32));

        buttonPanel.add(addBtn);
        buttonPanel.add(doneBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(statsBtn);

        add(buttonPanel, BorderLayout.SOUTH);

        // Olay dinleyicileri
        addBtn.addActionListener(e -> showAddDialog());
        doneBtn.addActionListener(e -> markTodayComplete());
        deleteBtn.addActionListener(e -> deleteSelectedHabit());
        statsBtn.addActionListener(e -> showStatsDialog());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        String today = sdf.format(new Date());
        List<Habit> habits = tracker.getAllHabits();

        for (int i = 0; i < habits.size(); i++) {
            Habit h = habits.get(i);
            String bugun = h.isCompletedOn(today) ? "Evet" : "-";
            tableModel.addRow(new Object[]{
                i + 1,
                h.getName(),
                h.getHabitType(),
                h.getCategory().getName(),
                h.getTotalCompletions(),
                bugun
            });
        }
    }

    // Yeni alışkanlık ekle dialog'u
    private void showAddDialog() {
        JDialog dialog = new JDialog(this, "Yeni Alışkanlık Ekle", true);
        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(8, 8));

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        // İsim
        form.add(new JLabel("İsim:"));
        JTextField nameField = new JTextField();
        form.add(nameField);

        // Açıklama
        form.add(new JLabel("Açıklama:"));
        JTextField descField = new JTextField();
        form.add(descField);

        // Tip
        form.add(new JLabel("Tip:"));
        JComboBox<String> typeBox = new JComboBox<>(
            new String[]{"Günlük", "Haftalık", "Sayısal"});
        form.add(typeBox);

        // Kategori
        form.add(new JLabel("Kategori:"));
        List<Category> cats = tracker.getAllCategories();
        String[] catNames = cats.stream().map(Category::getName).toArray(String[]::new);
        JComboBox<String> catBox = new JComboBox<>(catNames);
        form.add(catBox);

        // Ek alan etiketi + değeri (tip seçimine göre değişir)
        JLabel extraLabel = new JLabel("Hedef gün/hafta:");
        JTextField extraField = new JTextField("7");
        form.add(extraLabel);
        form.add(extraField);

        // Birim alanı (sadece Sayısal için aktif)
        form.add(new JLabel("Birim (Sayısal için):"));
        JTextField unitField = new JTextField("birim");
        form.add(unitField);

        // Tip değişince extra label'ı güncelle
        typeBox.addActionListener(e -> {
            int idx = typeBox.getSelectedIndex();
            if (idx == 0) {
                extraLabel.setText("Hedef gün/hafta:");
                extraField.setText("7");
                unitField.setEnabled(false);
            } else if (idx == 1) {
                extraLabel.setText("Hedef günler (virgülle):");
                extraField.setText("Pazartesi,Çarşamba,Cuma");
                unitField.setEnabled(false);
            } else {
                extraLabel.setText("Hedef değer:");
                extraField.setText("10");
                unitField.setEnabled(true);
            }
        });
        unitField.setEnabled(false); // başta Daily seçili

        dialog.add(form, BorderLayout.CENTER);

        // Butonlar
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        JButton saveBtn   = new JButton("Kaydet");
        JButton cancelBtn = new JButton("İptal");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String desc = descField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                    "İsim alanı boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String catName = (String) catBox.getSelectedItem();
            Category category = tracker.findCategoryByName(catName);
            if (category == null) category = new Category(catName, "Gri", "");

            try {
                int typeIdx = typeBox.getSelectedIndex();
                Habit habit;

                if (typeIdx == 0) {
                    // Daily
                    int targetDays = 7;
                    try { targetDays = Integer.parseInt(extraField.getText().trim()); }
                    catch (NumberFormatException ignored) {}
                    habit = new DailyHabit(name, desc, category, targetDays);

                } else if (typeIdx == 1) {
                    // Weekly
                    Set<String> days = new HashSet<>();
                    for (String d : extraField.getText().split(",")) {
                        String trimmed = d.trim();
                        if (!trimmed.isEmpty()) days.add(trimmed);
                    }
                    if (days.isEmpty()) days.add("Pazartesi");
                    habit = new WeeklyHabit(name, desc, category, days);

                } else {
                    // Numeric
                    double target = 10.0;
                    try { target = Double.parseDouble(extraField.getText().trim()); }
                    catch (NumberFormatException ignored) {}
                    String unit = unitField.getText().trim();
                    if (unit.isEmpty()) unit = "birim";
                    habit = new NumericHabit(name, desc, category, target, unit);
                }

                tracker.addHabit(habit);
                tracker.saveToFile();
                refreshTable();
                dialog.dispose();

            } catch (InvalidHabitDataException ex) {
                JOptionPane.showMessageDialog(dialog,
                    "Hata: " + ex.getMessage(), "Geçersiz Veri", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }

    // Seçili alışkanlığı bugün için tamamla
    private void markTodayComplete() {
        int row = habitTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Lütfen bir alışkanlık seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Habit> habits = tracker.getAllHabits();
        Habit habit = habits.get(row);
        String today = sdf.format(new Date());

        if (habit.isCompletedOn(today)) {
            JOptionPane.showMessageDialog(this,
                "\"" + habit.getName() + "\" bugün zaten tamamlandı.",
                "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // NumericHabit için değer sor
        if (habit instanceof NumericHabit) {
            NumericHabit nh = (NumericHabit) habit;
            String input = JOptionPane.showInputDialog(this,
                "Bugün kaç " + nh.getUnit() + " kaydetmek istiyorsunuz?\n(Hedef: "
                    + nh.getTargetValue() + " " + nh.getUnit() + ")",
                "Değer Gir", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                double value = Double.parseDouble(input.trim());
                nh.recordValue(today, value);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Geçerli bir sayı giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            habit.markComplete(today);
        }

        tracker.saveToFile();
        refreshTable();
        JOptionPane.showMessageDialog(this,
            "\"" + habit.getName() + "\" bugün için tamamlandı!",
            "Tamamlandı", JOptionPane.INFORMATION_MESSAGE);
    }

    // Seçili alışkanlığı sil
    private void deleteSelectedHabit() {
        int row = habitTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Lütfen silmek istediğiniz alışkanlığı seçin.",
                "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Habit> habits = tracker.getAllHabits();
        Habit habit = habits.get(row);

        int confirm = JOptionPane.showConfirmDialog(this,
            "\"" + habit.getName() + "\" silinecek. Emin misiniz?",
            "Silme Onayı", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            tracker.removeHabit(habit.getId());
            tracker.saveToFile();
            refreshTable();
        } catch (HabitNotFoundException ex) {
            JOptionPane.showMessageDialog(this,
                "Alışkanlık bulunamadı: " + ex.getMessage(),
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // İstatistikler dialog'u
    private void showStatsDialog() {
        String report = tracker.generateOverallReport();

        JTextArea area = new JTextArea(report);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, sp,
            "Genel İstatistik Raporu", JOptionPane.PLAIN_MESSAGE);
    }

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new HabitTrackerGUI().setVisible(true);
        });
    }
}
