package com.adpl.repository;

import com.adpl.model.BahanBaku;

public interface BahanBakuWriter {

    void save(BahanBaku bahan);

    void update(BahanBaku bahan);

    void delete(String id);
}
