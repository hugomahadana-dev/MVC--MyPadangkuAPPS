package com.adpl.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.adpl.model.BahanBaku;
import com.adpl.model.StatusStock;

public class BahanBakuView extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Locale LOKAL = Locale.forLanguageTag("id-ID");
    private static final String[] KOLOM = {"ID", "Nama Bahan", "Satuan", "Stok", "Stok Minimum", "Status"};
    private static final String[] SATUAN = {"kg", "gram", "liter", "ml", "butir", "ikat", "buah"};

    private final JTextField txtNama = new JTextField(18);
    private final JComboBox<String> cmbSatuan = new JComboBox<>(SATUAN);
    private final JTextField txtStok = new JTextField(18);
    private final JTextField txtStokMinimum = new JTextField(18);
    private final JButton btnTambah = new JButton("Tambah");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnBersihkan = new JButton("Bersihkan");
    private final JLabel lblRingkasan = new JLabel(" ");
    private final DefaultTableModel tableModel = new DefaultTableModel(KOLOM, 0);
    private final JTable table = new JTable(tableModel);

    private List<BahanBaku> daftarTampil = new ArrayList<>();

    public BahanBakuView() {
        super("Kelola Bahan Baku - RM Padang SON AMPERA");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(740, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        cmbSatuan.setEditable(true);
        lblRingkasan.setBorder(BorderFactory.createEmptyBorder(4, 10, 8, 10));

        add(buatPanelForm(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(lblRingkasan, BorderLayout.SOUTH);

        siapkanTabel();
    }

    private JPanel buatPanelForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tambahBaris(panel, gbc, 0, "Nama Bahan", txtNama);
        tambahBaris(panel, gbc, 1, "Satuan", cmbSatuan);
        tambahBaris(panel, gbc, 2, "Stok", txtStok);
        tambahBaris(panel, gbc, 3, "Batas Stok Kritis", txtStokMinimum);

        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        panelTombol.add(btnTambah);
        panelTombol.add(btnUbah);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBersihkan);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(panelTombol, gbc);
        return panel;
    }

    private void tambahBaris(JPanel panel, GridBagConstraints gbc, int baris, String label, Component input) {
        gbc.gridx = 0;
        gbc.gridy = baris;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(input, gbc);
    }

    private void siapkanTabel() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        table.getTableHeader().setReorderingAllowed(false);
        table.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer dasar = new DefaultTableCellRenderer();
        table.setDefaultRenderer(Object.class, (tbl, nilai, terpilih, fokus, baris, kolom) -> {
            Component komponen = dasar.getTableCellRendererComponent(tbl, nilai, terpilih, fokus, baris, kolom);
            if (!terpilih && baris < daftarTampil.size()) {
                Color warna = warnaStatus(daftarTampil.get(baris).getStatus());
                if (warna != null) {
                    komponen.setBackground(warna);
                    komponen.setForeground(Color.BLACK);
                }
            }
            return komponen;
        });

        // Memilih baris mengisi form agar mudah diubah.
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                isiFormDariBaris(table.getSelectedRow());
            }
        });
    }

    private static Color warnaStatus(StatusStock status) {
        switch (status) {
            case HABIS:
                return new Color(255, 205, 205);
            case KRITIS:
                return new Color(255, 236, 179);
            default:
                return null;
        }
    }

    private void isiFormDariBaris(int baris) {
        if (baris < 0 || baris >= daftarTampil.size()) {
            return;
        }
        BahanBaku bahan = daftarTampil.get(baris);
        txtNama.setText(bahan.getNama());
        cmbSatuan.setSelectedItem(bahan.getSatuan());
        txtStok.setText(formatAngka(bahan.getStock()));
        txtStokMinimum.setText(formatAngka(bahan.getStokMinimum()));
    }

    private static String formatAngka(double angka) {
        return String.format(LOKAL, "%.2f", angka);
    }

    public String getNama() {
        return txtNama.getText();
    }

    public String getSatuan() {
        Object item = cmbSatuan.getEditor().getItem();
        return item == null ? "" : item.toString();
    }

    public String getStok() {
        return txtStok.getText();
    }

    public String getStokMinimum() {
        return txtStokMinimum.getText();
    }

    public String getIdTerpilih() {
        int baris = table.getSelectedRow();
        return (baris >= 0 && baris < daftarTampil.size()) ? daftarTampil.get(baris).getId() : null;
    }

    public void tampilkanData(List<BahanBaku> daftar) {
        daftarTampil = new ArrayList<>(daftar);
        tableModel.setRowCount(0);
        for (BahanBaku bahan : daftar) {
            tableModel.addRow(new Object[] {
                    bahan.getId(),
                    bahan.getNama(),
                    bahan.getSatuan(),
                    formatAngka(bahan.getStock()),
                    formatAngka(bahan.getStokMinimum()),
                    bahan.getStatus().getLabel()
            });
        }
    }

    public void tampilkanRingkasanKritis(int jumlah) {
        lblRingkasan.setText(jumlah == 0
                ? "Semua stok bahan baku aman."
                : "Perhatian: " + jumlah + " bahan baku berstatus kritis atau habis.");
    }

    public void kosongkanForm() {
        txtNama.setText("");
        cmbSatuan.setSelectedIndex(0);
        txtStok.setText("");
        txtStokMinimum.setText("");
        table.clearSelection();
        txtNama.requestFocusInWindow();
    }

    public void tampilkanError(String judul, String pesan) {
        JOptionPane.showMessageDialog(this, pesan, judul, JOptionPane.ERROR_MESSAGE);
    }

    public boolean konfirmasiHapus() {
        int pilihan = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus bahan baku yang dipilih?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        return pilihan == JOptionPane.YES_OPTION;
    }

    public void addTambahListener(ActionListener listener) {
        btnTambah.addActionListener(listener);
    }

    public void addUbahListener(ActionListener listener) {
        btnUbah.addActionListener(listener);
    }

    public void addHapusListener(ActionListener listener) {
        btnHapus.addActionListener(listener);
    }

    public void addBersihkanListener(ActionListener listener) {
        btnBersihkan.addActionListener(listener);
    }
}
