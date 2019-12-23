package eu.barchfeld;

import javax.persistence.*;

@Entity
public class PatImage {

    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] patPicture;


    public PatImage() {

    }
    public PatImage(byte[] patPicture) {
        this.patPicture = patPicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getPatPicture() {
        return patPicture;
    }

    public void setPatPicture(byte[] patPicture) {
        this.patPicture = patPicture;
    }
}
