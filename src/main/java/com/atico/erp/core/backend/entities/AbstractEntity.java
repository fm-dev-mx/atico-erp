package com.atico.erp.core.backend.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
public abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    private Boolean isDeleted = false;

    //@NotNull
    private LocalDateTime createDateTime = LocalDateTime.now();


    public Long getId() {
        return id;
    }

    public String getIdStr() {
        return String.format("%1$4s", id.toString()).replace(' ', '0');
    }

    public boolean isPersisted() {
        return id != null;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public String getCreateDateTimeStr() {
        return formatDateTime(createDateTime);
    }

    public String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) obj;

        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }
}