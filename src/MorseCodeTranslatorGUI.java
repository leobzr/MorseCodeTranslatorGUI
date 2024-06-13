import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MorseCodeTranslatorGUI extends JFrame implements KeyListener {
    private MorseCodeController morseCodeController;
    private JTextArea textInputArea, morseCodeArea;

    public MorseCodeTranslatorGUI() {
        super("Morse Code Translator");
        setSize(new Dimension(540, 750));
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.decode("#362653"));
        setLocationRelativeTo(null);

        morseCodeController = new MorseCodeController();
        addGuiComponents();
    }

    private void addGuiComponents() {
        // Title Label
        JLabel titleLabel = new JLabel("Morse Code Translator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 31));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 0, 540, 100);

        // Text Input:
        JLabel inputLabel = new JLabel("Enter text to translate:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setBounds(20, 100, 500, 30);

        // Text Input Area:
        textInputArea = new JTextArea();
        textInputArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textInputArea.addKeyListener(this);
        textInputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textInputArea.setLineWrap(true);
        textInputArea.setWrapStyleWord(true);

        JScrollPane textInputScroll = new JScrollPane(textInputArea);
        textInputScroll.setBounds(20, 132, 484, 236);

        // morse code input:
        JLabel morseCodeInput = new JLabel("Morse Code");
        morseCodeInput.setFont(new Font("Arial", Font.BOLD, 20));
        morseCodeInput.setForeground(Color.WHITE);
        morseCodeInput.setBounds(20, 380, 200, 30);

        morseCodeArea = new JTextArea();
        morseCodeArea.setFont(new Font("Arial", Font.PLAIN, 16));
        morseCodeArea.setEditable(false);
        morseCodeArea.setLineWrap(true);
        morseCodeArea.setWrapStyleWord(true);
        morseCodeArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane morseCodeScroll = new JScrollPane(morseCodeArea);
        morseCodeScroll.setBounds(20, 430, 484, 236);

        // play sound button:
        JButton playSoundButton = new JButton("Play Sound");
        playSoundButton.setFont(new Font("Arial", Font.BOLD, 20));
        playSoundButton.setBounds(20, 680, 200, 30);
        playSoundButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                // disable the play button (prevents the sound from getting interrupted)
                playSoundButton.setEnabled(false);

                Thread playMorseCodeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // attempt to play the morse code sound
                        try{
                            String[] morseCodeMessage = morseCodeArea.getText().split(" ");
                            morseCodeController.playSound(morseCodeMessage);
                        }catch(LineUnavailableException lineUnavailableException){
                            lineUnavailableException.printStackTrace();
                        }catch(InterruptedException interruptedException){
                            interruptedException.printStackTrace();
                        }finally{
                            // enable play sound button
                            playSoundButton.setEnabled(true);
                        }
                    }
                });
                playMorseCodeThread.start();
            }
        });

        // add to gui all:
        add(titleLabel);
        add(inputLabel);
        add(textInputScroll);
        add(morseCodeScroll);
        add(playSoundButton);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() != KeyEvent.VK_SHIFT) {
            String text = textInputArea.getText();
            String morseCode = morseCodeController.translateToMorseCode(text);
            morseCodeArea.setText(morseCode);
        }
    }
}
