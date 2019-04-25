package me.duncte123.lavaplayerplayer;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Main extends AudioEventAdapter {

    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    private final AudioPlayerManager playerManager;
    private AudioFrame lastFrame;
    private final AudioPlayer audioPlayer;
    private final AudioFormat format;
    private final Clip clip;
    private ScheduledFuture<?> future;

    private Main() throws Exception {
        this.clip = AudioSystem.getClip();
        this.format = new AudioFormat(Encoding.PCM_UNSIGNED, 44100, 16, 2, 24, 44100, true);


        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.getConfiguration().setOutputFormat(StandardAudioDataFormats.COMMON_PCM_S16_BE);
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        this.audioPlayer = this.playerManager.createPlayer();
        this.audioPlayer.addListener(this);

        this.playerManager.loadItem("https://www.youtube.com/watch?v=aOB8_aD5X88", new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                Main.this.audioPlayer.playTrack(track);
                System.out.println(track.toString());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                //
            }

            @Override
            public void noMatches() {
                //
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                //
            }
        });

        startSending();
    }

    private void startSending() {
        this.future = service.scheduleAtFixedRate(() -> {
            try {
                if (lastFrame == null) {
                    lastFrame = audioPlayer.provide();
                }

                final byte[] data = lastFrame != null ? lastFrame.getData() : null;

                if (data != null) {
                    clip.open(this.format, data, 0, data.length);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }, 0L, 20L, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) throws Exception {
        new Main();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        this.future.cancel(true);
        System.out.println("Finished the track");
    }
}
