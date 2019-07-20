/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacellama;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;

/**
 *
 * @author Laura
 */
public class juego {
    public static int crateresSaltados = 0, librosRecolectados = 7, vidas = 3;
    public static int yLlama = 380, velYLlama = 1, xCrater = 1000, velXCrater=2, xLibro = 1000;
    public static boolean isLibroActive = false, isLlamaCorriendo = false, isLlamaSaltando = false;
    
    public static boolean colisionCrater(JLabel jugador, JLabel objeto){
       int widthJugador = 100;
       int heightJugador = 120;
       int widthCrater = 60;
       int heightCrater = 70;
       int x1 = jugador.getX(), y1 = jugador.getY();
       int x2 = objeto.getX(), y2 = objeto.getY();
       Rectangle rJugador = new Rectangle(x1,y1,widthJugador,heightJugador);
       Rectangle rCrater = new Rectangle(x2,y2, widthCrater, heightCrater);
       return rJugador.intersects(rCrater);
    }
    public static boolean colisionLibro(JLabel jugador, JLabel objeto){
       int widthJugador = 106;
       int heightJugador = 120;
       int widthCrater = objeto.getWidth();
       int heightCrater = objeto.getHeight();
       int x1 = jugador.getX(), y1 = jugador.getY();
       int x2 = objeto.getX(), y2 = objeto.getY();
       Rectangle rJugador = new Rectangle(x1,y1,widthJugador,heightJugador);
       Rectangle rCrater = new Rectangle(x2,y2, widthCrater, heightCrater);
       return rJugador.intersects(rCrater);
    }
    public static boolean quitarVida(){
        boolean resultado = false;
        if (vidas > 1) {
            vidas -= 1;
            resultado = true;
        }
        else{
            resultado = false;
        }
        return resultado;
    }
    
}
