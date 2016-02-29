package lggug

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.google.common.reflect.TypeToken
import ratpack.handling.Context
import ratpack.http.TypedData
import ratpack.parse.NoOptParserSupport

class YamlParser extends NoOptParserSupport {

    public static final MIME_TYPE = "application/x-yaml"

    private final YAMLMapper mapper = new YAMLMapper()

    @Override
    protected <T> T parse(Context context, TypedData requestBody, TypeToken<T> type) throws Exception {
        if (requestBody.contentType.type != MIME_TYPE) {
            return null
        }

        mapper.readValue(requestBody.inputStream, toJavaType(type))
    }

    private <T> JavaType toJavaType(TypeToken<T> type) {
        return mapper.typeFactory.constructType(type.type);
    }
}
