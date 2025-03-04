/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jframesource;

import javax.swing.JOptionPane;
/**
 *
 * @author user
 */
public class NewException extends Exception {
    public NewException(String information) {
        JOptionPane.showMessageDialog(null, information, "Error 404", JOptionPane.ERROR_MESSAGE);
    }
}
