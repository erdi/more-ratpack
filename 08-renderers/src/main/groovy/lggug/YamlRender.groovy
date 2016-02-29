package lggug

class YamlRender {

    public static final MIME_TYPE = "application/x-yaml"

    final Object object

    private YamlRender(Object object) {
        this.object = object
    }

    static YamlRender yaml(Object object) {
        new YamlRender(object)
    }
}
