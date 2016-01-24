package de.eknoes.speakerslist;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

import freemarker.template.Configuration;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import static spark.Spark.get;

public class Main {

    public static Configuration cfg = new Configuration();
    public static FreeMarkerEngine engine = new FreeMarkerEngine();

    public static void main(String[] args) {
        port(Integer.valueOf(System.getenv("PORT")));
        staticFileLocation("/public");

        cfg.setClassForTemplateLoading(ListController.class, "/templates");
        engine.setConfiguration(cfg);


        new ListController();

        get("/", (request, response) -> {
            return new ModelAndView(null, "index.ftl");
        }, engine);

        get("/:listID", (request, response) -> {
            String listID = request.params(":listID");
            Map<String, String> att = new HashMap<>();
            att.put("listID", listID);
            return new ModelAndView(att, "index.ftl");
        }, engine);
    }

}
