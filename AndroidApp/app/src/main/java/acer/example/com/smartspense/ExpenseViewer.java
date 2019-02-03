package acer.example.com.smartspense;

/**
 * Created by Acer on 02/02/2019.
 */

public class ExpenseViewer
{
    public String itemName;
    public String price;
    public String category;
    public String note;
    public String date;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ExpenseViewer(String itemName, String price, String category, String note, String date) {
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.note = note;
        this.date = date;
    }
}
