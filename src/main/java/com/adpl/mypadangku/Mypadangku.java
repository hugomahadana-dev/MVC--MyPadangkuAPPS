package com.adpl.mypadangku;

import javax.swing.SwingUtilities;

import com.adpl.controller.BahanBakuController;
import com.adpl.repository.BahanBakuRepository;
import com.adpl.repository.Database;
import com.adpl.repository.InMemoryBahanBakuRepository;
import com.adpl.repository.JdbcBahanBakuRepository;
import com.adpl.service.BahanBakuService;
import com.adpl.service.PemantauStockService;
import com.adpl.view.BahanBakuView;

public class Mypadangku {
    public static void main(String[] args) {
        boolean pakaiMemori = args.length > 0 && args[0].equals("--memory");

        BahanBakuRepository repository = pakaiMemori
                ? new InMemoryBahanBakuRepository()
                : new JdbcBahanBakuRepository(new Database());

        BahanBakuService service = new BahanBakuService(repository);
        PemantauStockService pemantau = new PemantauStockService(repository);

        SwingUtilities.invokeLater(() -> {
            BahanBakuView view = new BahanBakuView();
            new BahanBakuController(service, pemantau, view);
            view.setVisible(true);
        });
    }
}
