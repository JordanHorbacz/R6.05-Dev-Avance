package org.univ_paris8.iut.montreuil.dev_avance.dto;

import java.sql.Timestamp;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Email;

public class AnnonceDTO {
    private Long id;

    @NotNull(message = "Title is required")
    @Size(min = 3, max = 64, message = "Title must be between 3 and 64 characters")
    private String title;

    @Size(max = 256, message = "Description too long")
    private String description;

    @Size(max = 64)
    private String adress;

    @Email
    @Size(max = 64)
    private String mail;

    private Timestamp date;
    private String status;
    private String authorName;
    private String categoryLabel;

    private Long categoryId;
    private Long authorId;

    public AnnonceDTO() {
    }

    private AnnonceDTO(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.adress = builder.adress;
        this.mail = builder.mail;
        this.date = builder.date;
        this.status = builder.status;
        this.authorName = builder.authorName;
        this.categoryLabel = builder.categoryLabel;
        this.categoryId = builder.categoryId;
        this.authorId = builder.authorId;
    }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private String adress;
        private String mail;
        private Timestamp date;
        private String status;
        private String authorName;
        private String categoryLabel;
        private Long categoryId;
        private Long authorId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder adress(String adress) {
            this.adress = adress;
            return this;
        }

        public Builder mail(String mail) {
            this.mail = mail;
            return this;
        }

        public Builder date(Timestamp date) {
            this.date = date;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Builder categoryLabel(String categoryLabel) {
            this.categoryLabel = categoryLabel;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder authorId(Long authorId) {
            this.authorId = authorId;
            return this;
        }

        public AnnonceDTO build() {
            return new AnnonceDTO(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}
