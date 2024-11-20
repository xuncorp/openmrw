# OpenMrw

OpenMrw (Open Metadata Reader Writer), A tool library for the JVM platform.

- `Identify` media files.
- `Read` media files stream information, metadata, and other information.
- `Write` media files metadata, and other information.

# Project Structure Description

The project primarily uses kotlinx-io for file operations, core code is located in the ‘core’ folder (module).

`OpenMrw.kt` is a singleton that directly parses files using the read method, and `UnstableOpenMrwApi.kt` is used to mark classes or methods as unstable.

- `format` folder contains the handling of various file types.
  - `flac`
  - `MrwComment.kt` is for audio tag information; for instance, FLAC comments read will be converted into this.
  - `MrwFormat.kt` is an abstract class for the obtained file information.
  - `MrwFormatType.kt` contains the supported formats.
- `rw` folder is for read-write operations, which includes abstract classes for readers and writers.