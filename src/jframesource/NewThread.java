/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jframesource;

/**
 *
 * @author user
 */
public class NewThread extends Thread {
    public double result = 0.0, low = 0.0, high = 0.0, step = 0.0;
    
    public NewThread(double low, double high, double step) {
        this.low = low;
        this.high = high;
        this.step = step;
    }
    
    @Override
    public void run() {
        int n = 0;
        
        for (double x = low; x < high; x += step) {
            result += (Math.exp(-x) + Math.exp(-x+step)) * step/2;
            n++;
        }
        
        double normalize = high - (low + n * step);
        if (normalize > 0)
            result += (Math.exp(low + n * step) + Math.exp(high)) * normalize / 2;
    }
}
