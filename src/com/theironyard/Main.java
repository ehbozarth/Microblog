package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Post> postList = new ArrayList<>();
        Spark.staticFileLocation("/public");
        Spark.init();


        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("user_name");
                    if (userName == null) {
                        return new ModelAndView(new HashMap<>(), "not-logged-in.html");
                    }//End of if UserName is null

                    HashMap m = new HashMap();
                    m.put("name", userName);
                    m.put("posts", postList);
                    return new ModelAndView(m, "logged-in.html");
                }),
                new MustacheTemplateEngine()
        );//End of Get Posts

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String userName = request.queryParams("user_name");
                    Session session = request.session();
                    session.attribute("user_name", userName);
                    response.redirect("/");
                    return "";
                })
        );//End of Create User

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.id = postList.size() + 1;
                    post.textArea = request.queryParams("post_text");
                    postList.add(post);
                    response.redirect("/");
                    return "";
                })
        );//End of Create Post

        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("delete_id");
                    try {
                        int idNum = Integer.valueOf(id);
                        postList.remove(idNum-1);
                        for(int i = 0; i < postList.size(); i++){
                            postList.get(i).id = i + 1;
                        }
                    } catch (Exception e){

                    }
                    response.redirect("/");
                    return "";
                })
        );//End of Delete Post

        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String editId = request.queryParams("edit_id");
                    try{
                        int editIdNum = Integer.valueOf(editId);
                        postList.get(editIdNum - 1).textArea = request.queryParams("edit_text");
                        for(int i = 0; i < postList.size(); i++){
                            postList.get(i).id = i + 1;
                        }//End of For Loop
                    }//End of Try
                    catch(Exception e){
                        System.out.println("Error in edit post");
                    }//End of Catch

                    response.redirect("/");
                    return "";
                })
        );//End of Edit Post

    }//End of of Main Method

}//End of Main class