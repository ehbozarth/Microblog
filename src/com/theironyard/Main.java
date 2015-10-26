package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Post> postList = new ArrayList<>();
        User user = new User();
        Spark.staticFileLocation("/public");
        Spark.init();

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    user.name = request.queryParams("userName");
                    response.redirect("/posts");
                    return "";
                })
        );//End of Create User

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.textArea = request.queryParams("postName");
                    postList.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );//End of Create Post

        Spark.get(
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("name", user.name);
                    m.put("posts", postList);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()
        );//End of Get Posts

    }//End of of Main Method

}//End of Main class