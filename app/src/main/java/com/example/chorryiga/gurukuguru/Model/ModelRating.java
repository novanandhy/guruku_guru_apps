package com.example.chorryiga.gurukuguru.Model;

/**
 * Created by Choy on 8/1/2017.
 */

public class ModelRating {
    public String id_user;
    public double rating;
    public String review;
    public String nama;
    public String foto;

    public ModelRating(){}

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
