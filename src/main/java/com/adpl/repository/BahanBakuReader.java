package com.adpl.repository;

import java.util.List;
import java.util.Optional;

import com.adpl.model.BahanBaku;

public interface BahanBakuReader {

    List<BahanBaku> findAll();

    Optional<BahanBaku> findById(String id);
}
