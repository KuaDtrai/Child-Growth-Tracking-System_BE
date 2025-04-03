package G5_SWP391.ChildGrownTracking.models;

public enum Rating {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Tìm enum từ giá trị int
    public static Rating fromValue(int value) {
        for (Rating point : values()) {
            if (point.value == value) {
                return point;
            }
        }
        throw new IllegalArgumentException("Invalid rating value: " + value);
    }
}
