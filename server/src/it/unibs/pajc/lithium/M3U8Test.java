package it.unibs.pajc.lithium;

import io.lindstrom.m3u8.model.MediaPlaylist;
import io.lindstrom.m3u8.model.MediaSegment;
import io.lindstrom.m3u8.parser.MediaPlaylistParser;

public class M3U8Test {
    public static String getPlaylist() {
        MediaPlaylist playlist = MediaPlaylist.builder()
                .version(1)
                .targetDuration(10)
                .allowCache(true)
                .addMediaSegments(
                        MediaSegment.builder().uri("syl00.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl01.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl02.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl03.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl04.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl05.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl06.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl07.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl08.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl09.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl10.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl11.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl12.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl13.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl14.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl15.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl16.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl17.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl18.aac").duration(8.99).build(),
                        MediaSegment.builder().uri("syl19.aac").duration(2.95).build()
                ).build();
        MediaPlaylistParser parser = new MediaPlaylistParser();
        return parser.writePlaylistAsString(playlist);
    }
}
