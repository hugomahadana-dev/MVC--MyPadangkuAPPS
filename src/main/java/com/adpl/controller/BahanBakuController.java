package controller;

import repository.RepositoryException;
import service.BahanBakuService;
import service.PemantauStokService;
import view.BahanBakuView;

/**
 * Penghubung View dan Service: menangkap aksi pengguna, mengubah input teks menjadi angka,
 * memanggil service, lalu memperbarui tampilan. Tidak berisi SQL maupun aturan bisnis.
 */
public class BahanBakuController {
    private final BahanBakuService service;
    private final PemantauStokService pemantau;
    private final BahanBakuView view;

    public BahanBakuController(BahanBakuService service, PemantauStokService pemantau, BahanBakuView view) {
        this.service = service;
        this.pemantau = pemantau;
        this.view = view;

        view.addTambahListener(e -> jalankan(this::tambah));
        view.addUbahListener(e -> jalankan(this::ubah));
        view.addHapusListener(e -> jalankan(this::hapus));
        view.addBersihkanListener(e -> view.kosongkanForm());

        jalankan(this::muatData);
    }

    private void tambah() {
        service.tambah(view.getNama(), view.getSatuan(),
                parseAngka(view.getStok(), "Stok"),
                parseAngka(view.getStokMinimum(), "Batas stok kritis"));
        view.kosongkanForm();
        muatData();
    }

    private void ubah() {
        service.ubah(idTerpilih(), view.getNama(), view.getSatuan(),
                parseAngka(view.getStok(), "Stok"),
                parseAngka(view.getStokMinimum(), "Batas stok kritis"));
        view.kosongkanForm();
        muatData();
    }

    private void hapus() {
        String id = idTerpilih();
        if (!view.konfirmasiHapus()) {
            return;
        }
        service.hapus(id);
        view.kosongkanForm();
        muatData();
    }

    private void muatData() {
        view.tampilkanData(service.ambilSemua());
        view.tampilkanRingkasanKritis(pemantau.ambilStokKritis().size());
    }

    /** Satu tempat untuk menangani error, supaya setiap aksi tidak mengulang try-catch. */
    private void jalankan(Runnable aksi) {
        try {
            aksi.run();
        } catch (IllegalArgumentException ex) {
            view.tampilkanError("Data Input Tidak Valid", ex.getMessage());
        } catch (RepositoryException ex) {
            view.tampilkanError("Kesalahan Basis Data", ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String idTerpilih() {
        String id = view.getIdTerpilih();
        if (id == null) {
            throw new IllegalArgumentException("Pilih bahan baku pada tabel terlebih dahulu");
        }
        return id;
    }

    /** Mengubah teks input menjadi angka; menerima koma maupun titik sebagai pemisah desimal. */
    private double parseAngka(String teks, String namaField) {
        try {
            return Double.parseDouble(teks.trim().replace(',', '.'));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(namaField + " harus berupa angka");
        }
    }
}
