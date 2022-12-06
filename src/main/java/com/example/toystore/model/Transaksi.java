package com.example.toystore.model;

public class Transaksi {
    private String id_transaksi;
    private String id_pembeli;
    private String id_mainan;
    private String jml_mainan;
    private String pembayaran;
    private String tgl_transaksi;

    public String getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(String id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getId_pembeli() {
        return id_pembeli;
    }

    public void setId_pembeli(String id_pembeli) {
        this.id_pembeli = id_pembeli;
    }

    public String getId_mainan() {
        return id_mainan;
    }

    public void setId_mainan(String id_mainan) {
        this.id_mainan = id_mainan;
    }

    public String getJml_mainan() {
        return jml_mainan;
    }

    public void setJml_mainan(String jml_mainan) {
        this.jml_mainan = jml_mainan;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public String getTgl_transaksi() {
        return tgl_transaksi;
    }

    public void setTgl_transaksi(String tgl_transaksi) {
        this.tgl_transaksi = tgl_transaksi;
    }
}
