import lggug.GreetingService
import ratpack.handlebars.HandlebarsModule

import static ratpack.groovy.Groovy.ratpack
import static ratpack.handlebars.Template.handlebarsTemplate

ratpack {
    bindings {
        module(HandlebarsModule) { it.templatesPath("handlebars") }
        bind(GreetingService)
    }
    handlers {
        get ":name", {
            def greeting = get(GreetingService).greet(pathTokens.name)
            render handlebarsTemplate("main.html", greeting: greeting)
        }
    }
}

