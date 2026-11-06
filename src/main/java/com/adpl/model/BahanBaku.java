package model;

public class BahanBaku {
    private final String id;
    private final String nama;
    private final String satuan;
    private final double stok;
    private final double stokMinimum;

    public BahanBaku(String id, String nama, String satuan, double stok, double stokMinimum) {
        this.id = id;
        this.nama = nama;
        this.satuan = satuan;
        this.stok = stok;
        this.stokMinimum = stokMinimum;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getSatuan() {
        return satuan;
    }

    public double getStok() {
        return stok;
    }

    public double getStokMinimum() {
        return stokMinimum;
    }

    public StatusStok getStatus() {
        if (stok <= 0) {
            return StatusStok.HABIS;
        }
        if (stok <= stokMinimum) {
            return StatusStok.KRITIS;
        }
        return StatusStok.AMAN;
    }
}
