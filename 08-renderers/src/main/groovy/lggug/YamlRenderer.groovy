package lggug

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import ratpack.handling.Context
import ratpack.render.RendererSupport

class YamlRenderer extends RendererSupport<YamlRender> {

    private final YAMLMapper mapper = new YAMLMapper()

    void render(Context context, YamlRender yamlRender) throws Exception {
        context.response
            .contentTypeIfNotSet(YamlRender.MIME_TYPE)
            .send(mapper.writeValueAsString(yamlRender.object))
    }

}
