package com.adpl.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.adpl.model.BahanBaku;

public class JdbcBahanBakuRepository implements BahanBakuRepository {
    private static final String KOLOM = "id_bahan, nama, satuan, stok, stok_minimum";

    private final Database database;

    public JdbcBahanBakuRepository(Database database) {
        this.database = database;
    }

    @Override
    public List<BahanBaku> findAll() {
        String sql = "SELECT " + KOLOM + " FROM bahan_baku ORDER BY id_bahan";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            List<BahanBaku> hasil = new ArrayList<>();
            while (rs.next()) {
                hasil.add(petakan(rs));
            }
            return hasil;
        } catch (SQLException e) {
            throw new RepositoryException("Gagal membaca data bahan baku", e);
        }
    }

    @Override
    public Optional<BahanBaku> findById(String id) {
        String sql = "SELECT " + KOLOM + " FROM bahan_baku WHERE id_bahan = ?";
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(petakan(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Gagal membaca data bahan baku", e);
        }
    }

    @Override
    public void save(BahanBaku b) {
        eksekusi("INSERT INTO bahan_baku (" + KOLOM + ") VALUES (?, ?, ?, ?, ?)",
                "Gagal menyimpan bahan baku",
                b.getId(), b.getNama(), b.getSatuan(), b.getStock(), b.getStokMinimum());
    }

    @Override
    public void update(BahanBaku b) {
        int baris = eksekusi(
                "UPDATE bahan_baku SET nama = ?, satuan = ?, stok = ?, stok_minimum = ? WHERE id_bahan = ?",
                "Gagal mengubah bahan baku",
                b.getNama(), b.getSatuan(), b.getStock(), b.getStokMinimum(), b.getId());
        pastikanAdaYangTerpengaruh(baris, b.getId());
    }

    @Override
    public void delete(String id) {
        int baris = eksekusi("DELETE FROM bahan_baku WHERE id_bahan = ?",
                "Gagal menghapus bahan baku", id);
        pastikanAdaYangTerpengaruh(baris, id);
    }

    private int eksekusi(String sql, String pesanGagal, Object... parameter) {
        try (Connection conn = database.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < parameter.length; i++) {
                ps.setObject(i + 1, parameter[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(pesanGagal, e);
        }
    }

    private void pastikanAdaYangTerpengaruh(int baris, String id) {
        if (baris == 0) {
            throw new RepositoryException("Bahan baku dengan ID " + id + " tidak ditemukan");
        }
    }

    private BahanBaku petakan(ResultSet rs) throws SQLException {
        return new BahanBaku(
                rs.getString("id_bahan"),
                rs.getString("nama"),
                rs.getString("satuan"),
                rs.getDouble("stok"),
                rs.getDouble("stok_minimum"));
    }
}
