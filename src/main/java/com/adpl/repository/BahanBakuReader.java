package repository;

import java.util.List;
import java.util.Optional;

import model.BahanBaku;

public interface BahanBakuReader {

    List<BahanBaku> findAll();

    Optional<BahanBaku> findById(String id);
}
