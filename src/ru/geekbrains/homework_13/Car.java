package ru.geekbrains.homework_13;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static boolean winnerIsHere = false; // Переменная - флаг обнаружения победителя

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);

            if (race.getStages().get(i).equals(race.getStages().get(race.getStages().size() - 1)) && !winnerIsHere) {
                readWriteLock.writeLock().lock(); // Блокировка записи при одновременном приходе двух машин к финишу
                System.out.println(name + " WIN"); // Вывод сообщения о победителе
                winnerIsHere = true; // Флаг обнаружения победителя
                readWriteLock.writeLock().unlock();
            }
        }
    }

    public void preparation() { // Вынесение подготовки в отдельный метод
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
