package service;

import java.util.List;
import java.util.stream.Collectors;

import model.BahanBaku;
import model.StatusStock;
import repository.BahanBakuReader;

public class PemantauStockService {
    private final BahanBakuReader reader;

    public PemantauStockService(BahanBakuReader reader) {
        this.reader = reader;
    }

    public List<BahanBaku> ambilStokKritis() {
        return reader.findAll().stream()
                .filter(bahan -> bahan.getStatus() != StatusStock.AMAN)
                .collect(Collectors.toList());
    }
}
