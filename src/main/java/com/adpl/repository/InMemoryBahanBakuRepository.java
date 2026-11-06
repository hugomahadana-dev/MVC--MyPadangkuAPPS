package com.adpl.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.adpl.model.BahanBaku;

public class InMemoryBahanBakuRepository implements BahanBakuRepository {
    // TreeMap menjaga urutan berdasarkan ID, sama seperti ORDER BY id_bahan pada versi JDBC.
    private final Map<String, BahanBaku> data = new TreeMap<>();

    @Override
    public List<BahanBaku> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<BahanBaku> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void save(BahanBaku bahan) {
        if (data.containsKey(bahan.getId())) {
            throw new RepositoryException("ID " + bahan.getId() + " sudah digunakan");
        }
        data.put(bahan.getId(), bahan);
    }

    @Override
    public void update(BahanBaku bahan) {
        if (!data.containsKey(bahan.getId())) {
            throw new RepositoryException("Bahan baku dengan ID " + bahan.getId() + " tidak ditemukan");
        }
        data.put(bahan.getId(), bahan);
    }

    @Override
    public void delete(String id) {
        if (data.remove(id) == null) {
            throw new RepositoryException("Bahan baku dengan ID " + id + " tidak ditemukan");
        }
    }
}
