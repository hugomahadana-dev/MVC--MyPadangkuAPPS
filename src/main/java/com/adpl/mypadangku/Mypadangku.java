package main;

import javax.swing.SwingUtilities;

import controller.BahanBakuController;
import repository.BahanBakuRepository;
import repository.Database;
import repository.InMemoryBahanBakuRepository;
import repository.JdbcBahanBakuRepository;
import service.BahanBakuService;
import service.PemantauStokService;
import view.BahanBakuView;

public class Mypadangku {
    public static void main(String[] args) {
        boolean pakaiMemori = args.length > 0 && args[0].equals("--memory");

        BahanBakuRepository repository = pakaiMemori
                ? new InMemoryBahanBakuRepository()
                : new JdbcBahanBakuRepository(new Database());

        BahanBakuService service = new BahanBakuService(repository);
        PemantauStokService pemantau = new PemantauStokService(repository);

        SwingUtilities.invokeLater(() -> {
            BahanBakuView view = new BahanBakuView();
            new BahanBakuController(service, pemantau, view);
            view.setVisible(true);
        });
    }
}
