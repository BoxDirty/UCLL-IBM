package be.cosci.ibm.ucllwatson.utils.item;

import java.util.Objects;

/**
 * Created by Petr on 28-Mar-18.
 */
public class ReceiptItem {

    private String title;
    private String imageURL;
    private String sourceURL;

    public ReceiptItem(String title, String imageURL, String sourceURL) {
        this.title = title;
        this.imageURL = imageURL;
        this.sourceURL = sourceURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    @Override
    public String toString() {
        return "ReceiptItem{" +
                "title='" + title + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", sourceURL='" + sourceURL + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceiptItem that = (ReceiptItem) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(imageURL, that.imageURL) &&
                Objects.equals(sourceURL, that.sourceURL);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, imageURL, sourceURL);
    }
}