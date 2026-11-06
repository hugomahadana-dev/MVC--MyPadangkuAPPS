package com.adpl.service;

import java.util.List;

import com.adpl.model.BahanBaku;
import com.adpl.repository.BahanBakuRepository;

public class BahanBakuService {
    private static final String PREFIX_ID = "BHN";

    private final BahanBakuRepository repository;

    public BahanBakuService(BahanBakuRepository repository) {
        this.repository = repository;
    }

    public List<BahanBaku> ambilSemua() {
        return repository.findAll();
    }

    public void tambah(String nama, String satuan, double stok, double stokMinimum) {
        validasi(nama, satuan, stok, stokMinimum);
        repository.save(new BahanBaku(buatIdBaru(), nama.trim(), satuan.trim(), stok, stokMinimum));
    }

    public void ubah(String id, String nama, String satuan, double stok, double stokMinimum) {
        validasi(nama, satuan, stok, stokMinimum);
        repository.update(new BahanBaku(id, nama.trim(), satuan.trim(), stok, stokMinimum));
    }

    public void hapus(String id) {
        repository.delete(id);
    }

    private void validasi(String nama, String satuan, double stok, double stokMinimum) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama bahan wajib diisi");
        }
        if (satuan == null || satuan.isBlank()) {
            throw new IllegalArgumentException("Satuan wajib diisi");
        }
        if (tidakValid(stok)) {
            throw new IllegalArgumentException("Stok tidak boleh kosong atau bernilai minus");
        }
        if (tidakValid(stokMinimum)) {
            throw new IllegalArgumentException("Batas stok kritis tidak boleh kosong atau bernilai minus");
        }
    }

    private boolean tidakValid(double angka) {
        return Double.isNaN(angka) || Double.isInfinite(angka) || angka < 0;
    }

    private String buatIdBaru() {
        int terbesar = repository.findAll().stream()
                .map(BahanBaku::getId)
                .filter(id -> id.matches(PREFIX_ID + "\\d+"))
                .mapToInt(id -> Integer.parseInt(id.substring(PREFIX_ID.length())))
                .max()
                .orElse(0);
        return String.format("%s%03d", PREFIX_ID, terbesar + 1);
    }
}
