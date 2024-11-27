package com.xuncorp.openmrw.core.format.ncm

import com.xuncorp.openmrw.core.MrwFile
import com.xuncorp.openmrw.core.format.MrwFormatType
import com.xuncorp.openmrw.core.rw.MrwReader
import com.xuncorp.openmrw.core.rw.ReaderProperties
import kotlinx.io.Source

internal class NcmMrwReader : MrwReader() {
    override fun match(source: Source) {
        NcmHeader(source)
    }

    override fun fetch(source: Source, properties: ReaderProperties): MrwFile {
        return MrwFile(MrwFormatType.Ncm)
    }
}