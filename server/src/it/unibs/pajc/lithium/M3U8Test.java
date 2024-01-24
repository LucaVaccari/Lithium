package it.unibs.pajc.lithium;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;

public class M3U8Test {
    public static byte[] getPlaylist() {
        MediaPlaylist playlist = MediaPlaylist.builder()
                .version(1)
                .targetDuration(10)
                .allowCache(true)
                .addMediaSegments(
                        MediaSegment.builder().uri("audio/syl00.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl01.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl02.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl03.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl04.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl05.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl06.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl07.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl08.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl09.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl10.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl11.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl12.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl13.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl14.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl15.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl16.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl17.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl18.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("audio/syl19.aac").duration(2.95).build()
                ).build();
        MediaPlaylistParser parser = new MediaPlaylistParser();
        return parser.writePlaylistAsBytes(playlist);
    }
}
