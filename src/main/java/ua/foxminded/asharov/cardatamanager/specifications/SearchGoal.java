package ua.foxminded.asharov.cardatamanager.specifications;

public enum SearchGoal {

    MANUFACTURER("manufacturer.name"), MODEL("model.name"), CATEGORY("category.name"), YEAR("year.year");

    String searchGoal;

    SearchGoal(String searchGoal) {
        this.searchGoal = searchGoal;
    }

}
