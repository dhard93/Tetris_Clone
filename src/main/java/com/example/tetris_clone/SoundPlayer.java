package com.example.tetris_clone;

import java.net.URL;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 * This class converts all of the game's sound files into MediaPlayers and is used to play sound effects as well as start, stop, and resume music.
 */

public class SoundPlayer
{
    private static final SoundPlayer instance = new SoundPlayer();
    private final int SOUND_COUNT = 15;
    private final int MUSIC_INDEX = 14;
    private final AudioClip[] audioClipList;
    private MediaPlayer musicPlayer;
    private Duration musicCyclePosition = Duration.ZERO;

    private SoundPlayer()
    {
        audioClipList = new AudioClip[SOUND_COUNT];
    }

    public static SoundPlayer getInstance()
    {
        return instance;
    }

    /**
     * Iterate audio files and convert each one into a MediaPlayer object that can be used to play them.
     */
    public void getSounds()
    {
        String filePath;
        URL soundUrl = SoundPlayer.class.getResource("/com/example/tetris_clone/Tetris_Sounds");

        for (int i = 0; i < SOUND_COUNT; i++)
        {
            assert soundUrl != null;
            filePath = String.format("%s/sound%d.wav", soundUrl, i);

            if (i == MUSIC_INDEX)
            {
                Media musicMedia = new Media(filePath);
                musicPlayer = new MediaPlayer(musicMedia);
            }
            else
                audioClipList[i] = new AudioClip(filePath);
        }
    }

    /**
     * Play the sound of the MediaPlayer indicated by the passed in index.
     * @param index The index indicating which MediaPlayer to play.
     */
    public void playSound(int index)
    {
        AudioClip audioClip = audioClipList[index];
        audioClip.stop();
        audioClip.play();
    }

    /**
     * Start the game music loop from the beginning.
     */
    public void startMusic()
    {
        musicPlayer.setAutoPlay(true);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.seek(Duration.ZERO);
        musicPlayer.play();
    }

    /**
     * Stop game music.
     */
    public void stopMusic()
    {
        musicPlayer.pause();
        musicCyclePosition = musicPlayer.getCurrentTime();
    }

    /**
     * Resume game music from where it was last stopped.
     */
    public void resumeMusic()
    {
        musicPlayer.seek(musicCyclePosition);
        musicPlayer.play();
    }
}
