package lggug

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import ratpack.config.ConfigSource
import ratpack.file.FileSystemBinding

class MapConfigSource implements ConfigSource {

    private final Map<String, ?> config

    MapConfigSource(Map<String, ?> config) {
        this.config = config
    }

    ObjectNode loadConfigData(ObjectMapper objectMapper, FileSystemBinding fileSystemBinding) throws Exception {
        objectMapper.valueToTree(config)
    }

}
