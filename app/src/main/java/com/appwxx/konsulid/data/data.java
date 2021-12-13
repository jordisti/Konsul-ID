package com.appwxx.konsulid.data;

import android.graphics.Bitmap;

/**
 * Created by User on 01/11/2017.
 */

public class data {
    private String idAkun, nameAkun, kdAkun, kdTransaksi, dateTransaksi, idAkun1, idAkun2, totalTransaksi;
    private String idPartner, namePartner, emailPartner, tlpnPartner, hpPartner, akunhutangPartner, akunpiutangPartner, pengirimanPartner, penagihanPartner, tipebayarPartner, tipePartner;
    private String idJual, dateJual, idAkunJual, kdJual, totalJual, qtyJual, nameProduk, subtotalJual , idJualdetail, tempoJual;
    private String idProduk, kdProduk, jumlahStock, kategoriAkun, idTransaksi, resellerPartner, hargaProduk;
    private Bitmap image;
    private String urlb,saldoakun,idPesan,datePesan,tujuanPesan,isiPesan, totalAkun, tipePesan;

    public data() {
    }

    public data(String idAkun, String nameAkun , String kdAkun, String kdTransaksi, String dateTransaksi, String idAkun1,
                String idAkun2, String totalTransaksi, String idPartner, String namePartner, String emailPartner, String tlpnPartner
            , String hpPartner, String tipePartner, String akunhutangPartner, String akunpiutangPartner, String pengirimanPartner, String penagihanPartner
            , String tipebayarPartner , String idJual, String dateJual, String idAkunJual, String kdJual, String totalJual
            , String subtotalJual, String nameProduk, String qtyJual, String idJualdetail, String urlb,
                String idProduk, String kdProduk, String jumlahStock, String tempoJual, String kategoriAkun, String saldoakun, String tipePesan,
                String idTransaksi, String resellerPartner, String idPesan, String datePesan, String tujuanPesan, String isiPesan, String totalAkun) {
        this.idAkun = idAkun;
        this.nameAkun = nameAkun;
        this.kdAkun = kdAkun;
        this.kdTransaksi = kdTransaksi;
        this.dateTransaksi = dateTransaksi;
        this.totalTransaksi = totalTransaksi;
        this.idAkun1 = idAkun1;
        this.idAkun2 = idAkun2;
        this.idPartner = idPartner;
        this.namePartner = namePartner;
        this.emailPartner = emailPartner;
        this.tlpnPartner = tlpnPartner;
        this.hpPartner = hpPartner;
        this.akunhutangPartner = akunhutangPartner;
        this.akunpiutangPartner = akunpiutangPartner;
        this.pengirimanPartner = pengirimanPartner;
        this.penagihanPartner = penagihanPartner;
        this.tipebayarPartner = tipebayarPartner;
        this.tipePartner = tipePartner;
        this.idJual = idJual;
        this.dateJual = dateJual;
        this.idAkunJual = idAkunJual;
        this.kdJual = kdJual;
        this.totalJual = totalJual;
        this.subtotalJual = subtotalJual;
        this.qtyJual = qtyJual;
        this.nameProduk = nameProduk;
        this.idJualdetail = idJualdetail;
        this.idProduk = idProduk;
        this.kdProduk = kdProduk;
        this.jumlahStock = jumlahStock;
        this.tempoJual = tempoJual;
        this.kategoriAkun = kategoriAkun;
        this.idTransaksi = idTransaksi;
        this.resellerPartner = resellerPartner;
        this.urlb = urlb;
        this.hargaProduk = hargaProduk;
        this.saldoakun = saldoakun;
        this.idPesan = idPesan;
        this.datePesan = datePesan;
        this.tujuanPesan = tujuanPesan;
        this.isiPesan = isiPesan;
        this.totalAkun = totalAkun;
        this.tipePesan = tipePesan;
    }

    public String getTotalAkun() {
        return totalAkun;
    }

    public void setTotalAkun(String totalAkun) {
        this.totalAkun = totalAkun;
    }

    public String getTipePesan() {
        return tipePesan;
    }

    public void setTipePesan(String tipePesan) {
        this.tipePesan = tipePesan;
    }

    public String getIdPesan() {
        return idPesan;
    }

    public void setIdPesan(String idPesan) {
        this.idPesan = idPesan;
    }

    public String getDatePesan() {
        return datePesan;
    }

    public void setDatePesan(String datePesan) {
        this.datePesan = datePesan;
    }

    public String getTujuanPesan() {
        return tujuanPesan;
    }

    public void setTujuanPesan(String tujuanPesan) {
        this.tujuanPesan = tujuanPesan;
    }

    public String getIsiPesan() {
        return isiPesan;
    }

    public void setIsiPesan(String isiPesan) {
        this.isiPesan = isiPesan;
    }

    public String getSaldoAkun() {
        return saldoakun;
    }

    public void setSaldoAkun(String saldoakun) {
        this.saldoakun = saldoakun;
    }

    public String getIdAkun() {
        return idAkun;
    }

    public void setIdAkun(String idAkun) {
        this.idAkun = idAkun;
    }

    public String getNameAkun() {
        return nameAkun;
    }

    public void setNameAkun(String nameAkun) {
        this.nameAkun = nameAkun;
    }

    public String getKdAkun() {
        return kdAkun;
    }

    public void setKdAkun(String kdAkun) {
        this.kdAkun = kdAkun;
    }

    public String getkdTransaksi() {
        return kdTransaksi;
    }

    public void setkdTransaksi(String kdTransaksi) {
        this.kdTransaksi = kdTransaksi;
    }

    public String getDateTransaksi() {
        return dateTransaksi;
    }

    public void setDateTransaksi(String dateTransaksi) {
        this.dateTransaksi = dateTransaksi;
    }

    public String getIdAkun1() {
        return idAkun1;
    }

    public void setIdAkun1(String idAkun1) {
        this.idAkun1 = idAkun1;
    }

    public String getIdAkun2() {
        return idAkun2;
    }

    public void setIdAkun2(String idAkun2) {
        this.idAkun2 = idAkun2;
    }

    public String getTotalTransaksi() {
        return totalTransaksi;
    }

    public void setTotalTransaksi(String totalTransaksi) {
        this.totalTransaksi = totalTransaksi;
    }


    public String getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(String idPartner) {
        this.idPartner = idPartner;
    }

    public String getNamePartner() {
        return namePartner;
    }

    public void setNamePartner(String namePartner) {
        this.namePartner = namePartner;
    }

    public String getEmailPartner() {
        return emailPartner;
    }

    public void setEmailPartner(String emailPartner) {
        this.totalTransaksi = emailPartner;
    }

    public String getTlpnPartner() {
        return tlpnPartner;
    }

    public void setTlpnPartner(String tlpnPartner) {
        this.tlpnPartner = tlpnPartner;
    }

    public String getHpPartner() {
        return hpPartner;
    }

    public void setHpPartner(String hpPartner) {
        this.hpPartner = hpPartner;
    }

    public String getAkunhutangPartner() {
        return akunhutangPartner;
    }

    public void setAkunhutangPartner(String akunhutangPartner) {
        this.akunhutangPartner = akunhutangPartner;
    }

    public String getAkunpiutangPartner() {
        return akunpiutangPartner;
    }

    public void setAkunpiutangPartner(String akunpiutangPartner) {
        this.akunpiutangPartner = akunpiutangPartner;
    }

    public String getPengirimanPartner() {
        return pengirimanPartner;
    }

    public void setPengirimanPartner(String pengirimanPartner) {
        this.pengirimanPartner = pengirimanPartner;
    }


    public String getPenagihanPartner() {
        return penagihanPartner;
    }

    public void setPenagihanPartner(String penagihanPartner) {
        this.penagihanPartner = penagihanPartner;
    }


    public String getTipebayarPartner() {
        return tipebayarPartner;
    }

    public void setTipebayarPartner(String tipebayarPartner) {
        this.tipebayarPartner = tipebayarPartner;
    }


    public String getTipePartner() {
        return tipePartner;
    }

    public void setTipePartner(String tipePartner) {
        this.tipePartner = tipePartner;
    }


    public String getIdJual() {
        return idJual;
    }

    public void setIdJual(String idJual) {
        this.idJual = idJual;
    }


    public String getDateJual() {
        return dateJual;
    }

    public void setDateJual(String dateJual) {
        this.dateJual = dateJual;
    }


    public String getTempoJual() {
        return tempoJual;
    }

    public void setTempoJual(String tempoJual) {
        this.tempoJual = tempoJual;
    }


    public String getKdJual() {
        return kdJual;
    }

    public void setKdJual(String kdJual) {
        this.kdJual = kdJual;
    }

    public String getTotalJual() {
        return totalJual;
    }

    public void setTotalJual(String totalJual) {
        this.totalJual = totalJual;
    }

    public String getIdAkunJual() {
        return idAkunJual;
    }

    public void setIdAkunJual(String idAkunJual) {
        this.idAkunJual = idAkunJual;
    }

    public String getNameProduk() {
        return nameProduk;
    }

    public void setNameProduk(String nameProduk) {
        this.nameProduk = nameProduk;
    }

    public String getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(String hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public String getQtyJual() {
        return qtyJual;
    }

    public void setQtyJual(String qtyJual) {
        this.qtyJual = qtyJual;
    }

    public String getSubtotalJual() {
        return subtotalJual;
    }

    public void setSubtotalJual(String subtotalJual) {
        this.subtotalJual = subtotalJual;
    }

    public String getIdJualdetail() {
        return idJualdetail;
    }

    public void setIdJualdetail(String idJualdetail) {
        this.idJualdetail = idJualdetail;
    }


    public String getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(String idProduk) {
        this.idProduk = idProduk;
    }

    public String getKdProduk() {
        return kdProduk;
    }

    public void setKdProduk(String kdProduk) {
        this.kdProduk = kdProduk;
    }

    public String getJumlahStock() {
        return jumlahStock;
    }

    public void setJumlahStock(String jumlahStock) {
        this.jumlahStock = jumlahStock;
    }

    public String getKategoriAkun() {
        return kategoriAkun;
    }

    public void setKategoriAkun(String kategoriAkun) {
        this.kategoriAkun = kategoriAkun;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getResellerPartner() {
        return resellerPartner;
    }

    public void setResellerPartner(String resellerPartner) {
        this.resellerPartner = resellerPartner;
    }

    public String getUrl() {
        return urlb;
    }

    public void setUrl(String urlb) {
        this.urlb = urlb;
    }
}
