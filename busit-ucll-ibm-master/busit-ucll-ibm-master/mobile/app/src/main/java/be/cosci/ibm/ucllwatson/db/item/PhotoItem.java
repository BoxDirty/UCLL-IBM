package be.cosci.ibm.ucllwatson.db.item;

import java.util.Objects;

/**
 * Created by Petr on 27-Mar-18.
 */
public class PhotoItem {

    private long id;
    private String ingredientName;
    private String path;
    private double time;


    public PhotoItem(String ingredientName, String path, double time) {
        this.ingredientName = ingredientName;
        this.path = path;
        this.time = time;
    }

    public PhotoItem(long id, String ingredientName, String path, double time) {
        this.id = id;
        this.ingredientName = ingredientName;
        this.path = path;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PhotoItem{" +
                "id=" + id +
                ", ingredientName='" + ingredientName + '\'' +
                ", path='" + path + '\'' +
                ", time=" + time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhotoItem photoItem = (PhotoItem) o;
        return id == photoItem.id &&
                Double.compare(photoItem.time, time) == 0 &&
                Objects.equals(ingredientName, photoItem.ingredientName) &&
                Objects.equals(path, photoItem.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ingredientName, path, time);
    }
}