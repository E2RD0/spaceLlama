/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacellama;

import javax.swing.JFrame;

/**
 *
 * @author Laura
 */
public class SpaceLlama {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        inicioScreen f = new inicioScreen();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
    }
    
}
