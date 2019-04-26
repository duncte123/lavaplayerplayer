package me.duncte123.lavaplayerplayer;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import static com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats.COMMON_PCM_S16_BE;

public class Player extends AudioEventAdapter {

    private final AudioPlayer audioPlayer;
    private final AudioPlayerManager playerManager;

    public Player() throws Exception {
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.getConfiguration().setOutputFormat(COMMON_PCM_S16_BE);
        AudioSourceManagers.registerRemoteSources(this.playerManager);
        AudioSourceManagers.registerLocalSource(this.playerManager);

        this.audioPlayer = this.playerManager.createPlayer();
        this.audioPlayer.addListener(this);
        this.audioPlayer.setVolume(35);

        AudioDataFormat format = playerManager.getConfiguration().getOutputFormat();
        AudioInputStream stream = AudioPlayerInputStream.createStream(this.audioPlayer, format, 10000L, false);
        SourceDataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

        line.open(stream.getFormat());
        line.start();

        new Thread(() -> {
            try {
                byte[] buffer = new byte[COMMON_PCM_S16_BE.maximumChunkSize()];
                int chunkSize;

                while ((chunkSize = stream.read(buffer)) >= 0) {
                    line.write(buffer, 0, chunkSize);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println("Finished the track");
    }

    public void load(final String url) {
        this.playerManager.loadItem(url,
                new FunctionalResultHandler((track) -> {
                    this.audioPlayer.playTrack(track);

                    System.out.println(track);
                }, null, null, null));
    }
}
