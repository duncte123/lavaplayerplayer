package me.duncte123.lavaplayerplayer.gui;

import me.duncte123.lavaplayerplayer.Player;

import javax.swing.*;
import java.awt.*;

public class MainGui extends JFrame {
    private Player player;

    public MainGui() {
        initUI();
    }

    private void initUI() {
        var input = new JTextField();
        input.setMaximumSize(new Dimension(250, 25));

        var playBtn = new JButton("Play track");
        playBtn.addActionListener((event) -> loadTrack(input.getText()));
        playBtn.setToolTipText("Plays the track in the input field.");
        playBtn.setSize(100, 50);


        createLayout(input, playBtn);

        setTitle("Lavaplayer player");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void createLayout(JComponent... arg) {
        var pane = getContentPane();
        var gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        var g1 = gl.createParallelGroup();
        var g2 = gl.createSequentialGroup();

        g1.addGroup(
                gl.createSequentialGroup()
                        .addComponent(arg[0])
                        .addComponent(arg[1])
        );

        g2.addGroup(
                gl.createParallelGroup()
                        .addComponent(arg[0])
                        .addComponent(arg[1])
        );

        gl.setHorizontalGroup(g1);
        gl.setVerticalGroup(g2);
    }

    private void loadTrack(String track) {
        if (this.player == null) {
            try {
                this.player = new Player();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.player.load(track);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                var ex = new MainGui();
                ex.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
