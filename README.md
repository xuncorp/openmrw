![OpenMrw](res/openmrw.png)

[![](https://jitpack.io/v/xuncorp/openmrw.svg)](https://jitpack.io/#xuncorp/openmrw)

OpenMrw (Open Metadata Reader Writer), a tool library for the JVM platform.

- **Identify** media files.
- **Read** media files stream information, metadata, and other information.
- **Write (TODO)** media files metadata, and other information.

**This project is still in a very early stage and is not recommended for use in production
environments.**

## Media Format Support

FLAC, APE, MP3, etc.

See [MrwFileType.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/MrwFileType.kt).

## Project Structure

The project core code is located in the [core](core/src/main/kotlin/com/xuncorp/openmrw/core)
folder.

[OpenMrw.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/OpenMrw.kt) is a singleton that directly
parses files using the read method.

- [format](core/src/main/kotlin/com/xuncorp/openmrw/core/format) folder contains the handling of
  various file types.
    - [MrwComment.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwComment.kt) is for
      audio tag information; for instance, FLAC comments read will be
      converted into this.
    - [MrwStreamInfo.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/format/MrwStreamInfo.kt) is
      for stream information.
- [rw](core/src/main/kotlin/com/xuncorp/openmrw/core/rw) folder is for read-write operations, which
  includes abstract classes for readers and
  writers.
    - [tag](core/src/main/kotlin/com/xuncorp/openmrw/core/rw/tag)
        - [id3v2](core/src/main/kotlin/com/xuncorp/openmrw/core/rw/tag/id3v2) ID3v2 tags,
          ID3v2.3.0, ID3v2.4.0.
- [util](core/src/main/kotlin/com/xuncorp/openmrw/core/util) contains utility functions.
- [MrwFile.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/MrwFile.kt) is an
  abstract class for the obtained file information.
- [MrwFileType.kt](core/src/main/kotlin/com/xuncorp/openmrw/core/MrwFileType.kt)
  contains the supported formats.

Please refer to the code comments for detailed instructions.

## Contributors

<a href="https://github.com/xuncorp/openmrw/graphs/contributors">
    <img src="https://contrib.rocks/image?repo=xuncorp/openmrw&columns=15" alt="contributors"/>
</a>

## License

OpenMrw is licensed under the LGPL-2.1 open source license. For details, please refer to
the [LICENSE](LICENSE) file.