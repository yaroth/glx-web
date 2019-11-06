package ch.yaro.geologix.rest.pojos;

public class Lunch extends BasicStorable {

    public static String BADFOOD = "Bad food";

    private String food;
    private String beverage;

    /**
     * In the context of REST it is necessary to have a default constructor written in the class.
     * (Otherwise object mappers may fail when trying to create an object from a giving payload.)
     */
    public Lunch() {

    }

    public Lunch(String food, String beverage) {
        this.food = food;
        this.beverage = beverage;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getBeverage() {
        return beverage;
    }

    public void setBeverage(String beverage) {
        this.beverage = beverage;
    }

    @Override
    public String toString(){
        return "Food: " + food +", " + "beverage: " + beverage + "\n";
    }
}