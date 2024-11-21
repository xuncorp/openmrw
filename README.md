![OpenMrw](res/openmrw.png)

OpenMrw (Open Metadata Reader Writer), a tool library for the JVM platform.

- **Identify** media files.
- **Read** media files stream information, metadata, and other information.
- **Write** media files metadata, and other information.

**This project is still in a very early stage and is not recommended for use in production
environments.**

## Media Format Support

FLAC, APE, MP3.

See [MrwFormatType.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwFormatType.kt).

## Project Structure

The project core code is located in the [core](core/src/main/kotlin/com/xuncorp/openmrw/core)
folder.

[OpenMrw.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/OpenMrw.kt) is a singleton that directly
parses files using the read method.

- [format](core/src/main/kotlin/com/xuncorp/openmrw/core/format) folder contains the handling of
  various file types.
    - [ape](core/src/main/kotlin/com/xuncorp/openmrw/core/format/ape) Monkey's Audio.
    - [flac](core/src/main/kotlin/com/xuncorp/openmrw/core/format/flac) Free Lossless Audio Codec.
    - [mp3](core/src/main/kotlin/com/xuncorp/openmrw/core/format/mp3) MPEG-1 Audio Layer 3.
      - [Id3v2.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/mp3/Id3v2.kt) ID3v2 tags.
    - [MrwComment.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwComment.kt) is for
      audio tag information; for instance, FLAC comments read will be converted into this.
    - [MrwFormat.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwFormat.kt) is an
      abstract class for the obtained file information.
    - [MrwFormatType.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwFormatType.kt)
      contains the supported formats.
    - [MrwStreamInfo.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwStreamInfo.kt) is
      for stream information.
- [rw](core/src/main/kotlin/com/xuncorp/openmrw/core/rw) folder is for read-write operations, which
  includes abstract classes for readers and writers.

Please refer to the code comments for detailed instructions.

## Contributors

<a href="https://github.com/xuncorp/openmrw/graphs/contributors">
    <img src="https://contrib.rocks/image?repo=xuncorp/openmrw&columns=12" alt="contributors"/>
</a>

## License

OpenMrw is licensed under the LGPL-2.1 open source license. For details, please refer to
the [LICENSE](LICENSE) file.