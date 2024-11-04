package nl.han.fswd.travelexpensepro.expenseclaim;

public class ExpenseClaim {
    private String title;
    private String description;
    private double amount;

    public ExpenseClaim(String title, String description, double amount) {
        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }
}