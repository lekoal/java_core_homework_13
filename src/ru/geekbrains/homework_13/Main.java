package ru.geekbrains.homework_13;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static final int CARS_COUNT = 4;
    public static int maxThreatCount; // Переменная для хранения половины числа участников
    public static CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
    public static CountDownLatch cdl2 = new CountDownLatch(CARS_COUNT);
    public static Thread t1;
    public static Thread t2;

    public static void main(String[] args) {
        maxThreatCount = CARS_COUNT / 2; // Получение половины числа участников с отбрасыванием дробной части
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(maxThreatCount), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }

        for (int i = 0; i < cars.length; i++) {
            int finalI = i;
            t1 = new Thread(() -> {
                cars[finalI].preparation();
                cdl.countDown();
                try {
                    cdl.await(); // Ожидание прохождения подготовки всех участников
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            t1.start(); // Запуск подготовки
        }

        try {
            t1.join(); // Приостановка выполнения основного потока до завершения работы t1
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        for (int i = 0; i < cars.length; i++) {
            int finalI = i;
            t2 = new Thread(() -> {
                cars[finalI].run();
                cdl2.countDown();
            });
            t2.start(); // Старт гонки
        }

        try {
            cdl2.await(); // Приостановка выполнения основного потока
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
