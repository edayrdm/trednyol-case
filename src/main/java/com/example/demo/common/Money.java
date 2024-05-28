package com.example.demo.common;

public class Money {

    private final double money;

    public Money(double money) {

        if (money < 0) {
            throw new IllegalArgumentException("Money cannot be less than zero.");
        }
        this.money = money;
    }

    public Money add(Money sum) {
        return new Money(money + sum.value());
    }

    public Money sub(Money sub) {
        return new Money(money - sub.value());
    }

    public Money multi(double factor) {
        return new Money(money * factor);
    }

    public boolean isGreaterThan(Money other) {
        return this.money > other.money;
    }

    public boolean isEqualOrGreaterThan(Money other) {
        return this.money >= other.money;
    }

    public boolean isLessThan(Money other) {
        return this.money < other.money;
    }

    public double value() {
        return money;
    }
}
