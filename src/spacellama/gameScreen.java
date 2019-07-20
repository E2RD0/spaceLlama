/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacellama;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import java.util.Timer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static spacellama.juego.*;


/**
 *
 * @author Laura
 */
public class gameScreen extends javax.swing.JFrame{
    infoLibros infoLibro = new infoLibros();
    Timer timerCorrer = new Timer();
    Timer timerSalto = new Timer();
    Timer timerCrater = new Timer();
    Timer timerLibro = new Timer();
    
    ImageIcon llamaNormal = new ImageIcon(getClass().getResource("/resources/llamaNormal.png"));
    ImageIcon llamaCorriendo = new ImageIcon(getClass().getResource("/resources/llamaCorriendo.png"));
    ImageIcon llamaSaltando = new ImageIcon(getClass().getResource("/resources/llamaSaltando.png"));
    ImageIcon llamaMuerta = new ImageIcon(getClass().getResource("/resources/llamaMuerta.png"));
    
    public void saltar(){
        timerSalto = new Timer();
        TimerTask taskSalto = new TimerTask() {
        @Override
        public void run() {
            yLlama = yLlama - velYLlama;
            llama.setLocation(llama.getX(), yLlama);
            llama.setIcon(llamaSaltando);
            if (yLlama < 200 || yLlama > 380) {
                velYLlama = -velYLlama;
                if (yLlama == 381) {
                    llama.setLocation(llama.getX(), 380);
                    timerSalto.cancel();
                    isLlamaSaltando = false;
                    llama.setIcon(llamaNormal);
                }
            }
        }
        };
        timerSalto.scheduleAtFixedRate(taskSalto, 0, 5);
    }
    public void moverCrater(){
        timerCrater = new Timer();
        TimerTask taskCrater = new TimerTask() {
        @Override
        public void run() {
                xCrater = xCrater - velXCrater;
                crater.setLocation(xCrater, crater.getY());
                if (xCrater <= -70) {
                    isLibroActive = false;
                    xCrater = 950;
                    if (crateresSaltados % 9 == 0 && crateresSaltados < 31 && crateresSaltados !=0) {
                     velXCrater++;   
                    }
                    crateresSaltados++;
                    lblCrateres.setText(String.valueOf(crateresSaltados));
                }
                else if (crateresSaltados % 4 == 0 && crateresSaltados!=0 && !isLibroActive) {
                        libro.setVisible(true);
                        crater.setVisible(false);
                        moverLibro();
                        timerCrater.cancel();
                        isLibroActive = true;
                    }
                else if (colisionCrater(llama, crater)) {
                    System.out.println("hit");
                    llama.setIcon(llamaMuerta);
                    timerCorrer.cancel();
                    timerCrater.cancel();
                    timerSalto.cancel();
                    perderVida();   
                }
        }
        };
        timerCrater.scheduleAtFixedRate(taskCrater, 0, 10);
    }
    public void moverLibro(){
        timerLibro = new Timer();
        TimerTask taskLibro= new TimerTask() {
        @Override
        public void run() {
                isLlamaSaltando = true;
                xLibro = xLibro - velXCrater;
                libro.setLocation(xLibro, libro.getY());
                if (colisionLibro(llama, libro)) {
                    librosRecolectados++;
                    lblLibros.setText(String.valueOf(librosRecolectados) + " / 8");
                    xLibro = 950;
                    timerCorrer.cancel();
                    timerCrater.cancel();
                    timerSalto.cancel();
                    timerLibro.cancel();
                    txtInfo.setText(infoLibro.informacion[librosRecolectados-1]);
                    pnlInfo.setVisible(true);
                    isLlamaSaltando = true;
                }
        }
        };
        timerLibro.scheduleAtFixedRate(taskLibro, 0, 10);
    }
    private void perderVida(){
    int option = JOptionPane.showConfirmDialog(this, "Pierdes una vida", "Intentalo de nuevo", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            if (quitarVida()) {
                lblVidas.setText(String.valueOf(vidas) + " / 3");
                resetearElementos();
                        correr();
                        moverCrater();
            }
            else{
                Object[] options = {"Volver a jugar", "Ir a inicio"};
                int n = JOptionPane.showOptionDialog(this,
                      "Has perdido el juego","Game Over",
                      JOptionPane.PLAIN_MESSAGE,
                      JOptionPane.ERROR_MESSAGE,
                      null,
                      options,
                      options[0]);
                if (n == 0) {
                    resetearJuego();
                    correr();
                    moverCrater();
                }
                else if (n == 1) {
                    resetearJuego();
                    inicioScreen f = new inicioScreen();
                    this.setVisible(false);
                    f.setLocationRelativeTo(null);
                    f.setResizable(false);
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setVisible(true);
                    this.dispose();
                }
            }
        }
    }
    private void resetearJuego(){
        resetearElementos();
        crateresSaltados = 0;
        librosRecolectados = 0;
        vidas = 3;
        yLlama = 380; velYLlama = 1;
        xCrater = 1000;
        velXCrater=2;
        xLibro = 1000;
        isLibroActive = false;
        isLlamaCorriendo = false; 
        isLlamaSaltando = false;
        lblVidas.setText(String.valueOf(vidas) + " / 3");
        lblLibros.setText(String.valueOf(librosRecolectados) + " / 8");
        lblCrateres.setText(String.valueOf(crateresSaltados));
        
    }
    private void resetearElementos(){
        isLlamaSaltando = false;
        yLlama = 380;
        xCrater = 1000;
        timerCrater.cancel();
        timerSalto.cancel();
        timerCorrer.cancel();
        timerLibro.cancel();
        timerCrater = new Timer();
        timerSalto = new Timer();
        timerCorrer = new Timer();
        timerLibro = new Timer();
        llama.setLocation(40, 380);
        crater.setLocation(950, 420);
    }

    private void correr(){
        timerCorrer = new Timer();
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                if(!isLlamaSaltando){
                    if (isLlamaCorriendo) {
                    llama.setIcon(llamaNormal);
                    isLlamaCorriendo = false;
                    }
                else{
                    llama.setIcon(llamaCorriendo);
                    isLlamaCorriendo = true;
                    }
                }
            }
        };
        timerCorrer.scheduleAtFixedRate(task, 0, 200);
    }
    
    /**
     * Creates new form gameScreen
     */
    public gameScreen() {
        initComponents();
        correr();
        KeyListener listener = new MyKeyListener();
	addKeyListener(listener);
	setFocusable(true);
        moverCrater();
        libro.setVisible(false);
        pnlInfo.setVisible(false);
    }
    public class MyKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {
                    if (!isLlamaSaltando) {
   
                    if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_SPACE) {
                        isLlamaSaltando = true; 
                        
                        saltar();
                        System.out.println("Salto");
                    }
                    }
		}

		@Override
		public void keyReleased(KeyEvent e){
                }
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lblLibros = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblCrateres = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblVidas = new javax.swing.JLabel();
        pnlInfo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtInfo = new javax.swing.JTextArea();
        btnInfoOk = new javax.swing.JButton();
        libro = new javax.swing.JLabel();
        crater = new javax.swing.JLabel();
        llama = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/book(1).png"))); // NOI18N

        lblLibros.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblLibros.setText("0 / 8");

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/crater1(1).png"))); // NOI18N

        lblCrateres.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblCrateres.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(lblLibros)
                .addGap(25, 25, 25)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(lblCrateres)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblLibros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(lblCrateres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(824, 10, 200, 40));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/heart.png"))); // NOI18N

        lblVidas.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblVidas.setText("3 / 3");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(lblVidas)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
            .addComponent(lblVidas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(894, 60, 130, 40));

        pnlInfo.setBackground(new java.awt.Color(102, 102, 102));

        txtInfo.setEditable(false);
        txtInfo.setBackground(new java.awt.Color(102, 102, 102));
        txtInfo.setColumns(20);
        txtInfo.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtInfo.setForeground(new java.awt.Color(255, 255, 255));
        txtInfo.setLineWrap(true);
        txtInfo.setRows(5);
        txtInfo.setText("Es el planeta mas próximo al sol y el mas pequeño, forma parte de los planetas interiores o terrestres, carece de satélites. Tiene cuerpo rocoso, formado por 70% de elementos metálicos y un 30% de silicatos. La densidad de este planeta es la segunda más grande de todo el sistema solar. Se estima que su núcleo ocupa un 42% de su volumen total. ¿Sabías que Mercurio es el planeta más caliente del sistema solar? Esto se debe a que se encuentra a una muy corta distancia del Sol y también por esto es que su núcleo de magma ocupa el 42% de su volumen total.");
        txtInfo.setWrapStyleWord(true);
        jScrollPane1.setViewportView(txtInfo);

        btnInfoOk.setText("OK");
        btnInfoOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jLabel1))
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnInfoOk)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnInfoOk)
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addGap(147, 147, 147))
        );

        getContentPane().add(pnlInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 390, 180));

        libro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/book.png"))); // NOI18N
        getContentPane().add(libro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 420, -1, -1));

        crater.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/crater1.png"))); // NOI18N
        getContentPane().add(crater, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 420, 70, 80));

        llama.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/llamaMuerta.png"))); // NOI18N
        getContentPane().add(llama, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 123, 120));

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/bgMars.png"))); // NOI18N
        getContentPane().add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInfoOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoOkActionPerformed
        libro.setVisible(false);
        crater.setVisible(true);
        pnlInfo.setVisible(false);
        if (librosRecolectados == 8) {
            Object[] options = {"Volver a jugar", "Ir a inicio"};
                int n = JOptionPane.showOptionDialog(this,
                      "Has recolectado todos los libros","Has ganado!",
                      JOptionPane.PLAIN_MESSAGE,
                      JOptionPane.INFORMATION_MESSAGE,
                      null,
                      options,
                      options[0]);
                if (n == 0) {
                    resetearJuego();
                    correr();
                    moverCrater();
                }
                else if (n == 1) {
                    resetearJuego();
                    inicioScreen f = new inicioScreen();
                    this.setVisible(false);
                    f.setLocationRelativeTo(null);
                    f.setResizable(false);
                    f.setDefaultCloseOperation(EXIT_ON_CLOSE);
                    f.setVisible(true);
                    this.dispose();
                }
        }
        else{
            isLlamaSaltando = false;
            xCrater = 950;
            moverCrater();
            correr();
        }
    }//GEN-LAST:event_btnInfoOkActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gameScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gameScreen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bg;
    private javax.swing.JButton btnInfoOk;
    private javax.swing.JLabel crater;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCrateres;
    private javax.swing.JLabel lblLibros;
    private javax.swing.JLabel lblVidas;
    private javax.swing.JLabel libro;
    private javax.swing.JLabel llama;
    private javax.swing.JPanel pnlInfo;
    private javax.swing.JTextArea txtInfo;
    // End of variables declaration//GEN-END:variables
}
