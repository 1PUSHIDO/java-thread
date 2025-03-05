/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jframesource;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class RecIntegral {
    public double result = 0.0, low = 0.0, high = 0.0, step = 0.0;
    
    public RecIntegral() { }
    
    public RecIntegral(double low, double high, double step) throws NewException {
        // Проверка на вхождение в разрешенный диапазон
        if (low >= 0 && low <= 1000000 &&
                high >= 0 && high <= 1000000 &&
                step >= 0.000001 && step <= 1000000) {
            this.low = low;
            this.high = high;
            this.step = step;
            this.result = 0.0;
        }
        else
            // Исключение
            throw new NewException("Одно из чисел находится вне разрешенного диапазона [0.000001;1000000]");
    }
   
    public double CalculateResult(){
        result = 0.0;
        
        // Делим отрезок интегрирования на 5 равных кусков для каждой нити.
        double length = (high - low) / 5;
        // Массив нитей - всего 5 по варианту.
        ArrayList<NewThread> threads = new ArrayList<>();
        
        // Цикл - каждой нити передаем координаты кусков, которые они вычисляют "параллельно"
        for (double start = low; start + length < high; start += length) {
            // Передаем нити данные
            threads.add(new NewThread(start, start + length, step));
            // Запускаем
            threads.getLast().start();
        }
        
        for (NewThread thread : threads) {
            // Ждем нить, если она еще не посчитала результат
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(RecIntegral.class.getName()).log(Level.SEVERE, null, ex);
            }
            // к общему результату прибавляем вычисленный результат нити
            result += thread.result;
            thread.isAlive();
        }
        
        return result;
        
//        int n = 0;
//        result = 0.0;
//        
//        for (double x = low; x < high; x += step) {
//            result += (Math.exp(-x) + Math.exp(-x+step)) * step/2;
//            n++;
//        }
//        
//        double normalize = high - (low + n * step);
//        if (normalize > 0)
//            result += (Math.exp(low + n * step) + Math.exp(high)) * normalize / 2;
//        return result;
    }
}
