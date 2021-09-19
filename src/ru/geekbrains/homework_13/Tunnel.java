package ru.geekbrains.homework_13;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private final Semaphore smp; // Семафор для ограничения числа въезжающих в тоннель

    public Tunnel(int threadNumber) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
        this.smp = new Semaphore(threadNumber);
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " +
                        description);
                smp.acquire(); // Ограничение доступа потоков
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " +
                        description);
                smp.release(); // Снятие блокировки после прохождения тоннеля одним из участников
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
