package de.eknoes.speakerslist;

import de.eknoes.speakerslist.model.APIError;
import de.eknoes.speakerslist.model.Gender;
import de.eknoes.speakerslist.model.Speaker;
import de.eknoes.speakerslist.model.SpeakerList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * speakerslist
 * Created by soenke on 21.01.16.
 */
public class ListController {
    public static final int VERSION = 0;
    public static final String PREFIX = "/v" + VERSION;
    public Map<String, SpeakerList> lists = new HashMap<>();
    public genderIdentification gId = genderIdentification.getInstance();

    public ListController() {


        /***************
         * List Config *
         ***************/

        /**
         * Creates a new List
         */
        get(PREFIX + "/newList", (request, response) -> {
            response.type("application/json");
            SpeakerList newList = new SpeakerList();
            lists.put(newList.getId(), newList);
            return newList;
        }, JsonUtil.json());


        /**
         * Returns an SpeakerList Object with the given ID
         */
        get(PREFIX + "/list/:id", (request, response) -> {
            response.type("application/json");
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    return lists.get(id);
                } else {
                    response.status(404);
                    return new APIError(404, "No list with id " + id);
                }
            }
            response.status(400);

            return new APIError(400, "No list id given");
        }, JsonUtil.json());

        /**
         * Changes the specified settings of list
         */
        get(PREFIX + "/list/:id/changeList", (request, response) -> {
            response.type("application/json");
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    boolean answered = false;
                    SpeakerList s = lists.get(id);
                    if(request.queryParams("sexBalanced") != null) {
                        s.setSexBalanced(Boolean.valueOf(request.queryParams("sexBalanced")));
                        answered = true;
                    }
                    if(request.queryParams("preferNewSpeaker") != null) {
                        s.setPreferNewSpeaker(Boolean.valueOf(request.queryParams("preferNewSpeaker")));
                        answered = true;
                    }
                    if(!answered) {
                        response.status(400);
                        return new APIError(400, "No Setting given.");
                    } else {
                        return s;
                    }
                } else {
                    response.status(404);
                    return new APIError(404, "No list with id " + id);
                }
            }
            response.status(400);
            return new APIError(400, "No list id given");
        }, JsonUtil.json());

        get(PREFIX + "/list/:id/clear", (request, response) -> {
            response.type("application/json");
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    lists.get(id).clear();
                    return lists.get(id);
                } else {
                    response.status(404);
                    return new APIError(404, "No list with id " + id);
                }
            }
            response.status(400);
            return new APIError(400, "No list id given");
        }, JsonUtil.json());


        /*********************
         * SPEAKER FUNCTIONS *
         *********************/

        /* Adding new Speaker */
        get(PREFIX + "/list/:id/addSpeaker", (request, response) -> {
            response.type("application/json");
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    SpeakerList l = lists.get(id);
                    if(request.queryParams("uid") != null) {
                        if(l.addSpeaker(request.queryParams("uid"))) {
                            return l;
                        } else {
                            response.status(400);
                            return new APIError(400, "Either the uid is invalid or the speaker is already on the list");
                        }
                    } else if(request.queryParams("name") != null) {
                        Speaker s = null;

                        for(Speaker spk : l.getKnownSpeakers()) {
                            if(spk.getName().equals(request.queryParams("name"))) {
                                s = spk;
                                break;
                            }
                        }
                        if(s == null) {
                            s = new Speaker(request.queryParams("name"), gId.getGender(request.queryParams("name")));
                        }

                        if(l.addSpeaker(s)) {
                            return l;
                        } else {
                            response.status(400);
                            return new APIError(400, "There is already a speaker with this id on the list");
                        }
                    } else {
                        response.status(400);
                        return new APIError(400, "You have to specify an id or name");
                    }
                } else {
                    response.status(400);
                    return new APIError(404, "No list with id " + id);
                }
            }

            response.status(400);
            return new APIError(400, "No list id given");

        }, JsonUtil.json());

        /* Removing a Speaker */
        get(PREFIX + "/list/:id/removeSpeaker", (request, response) -> {
            response.type("application/json");
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    SpeakerList l = lists.get(id);
                    if(request.queryParams("uid") != null) {
                        l.removeSpeaker(request.queryParams("uid"));
                        return l;
                    } else {
                        response.status(400);
                        return new APIError(400, "You have to specify an id");
                    }
                } else {
                    response.status(400);
                    return new APIError(404, "No list with id " + id);
                }
            }

            response.status(400);
            return new APIError(400, "No list id given");

        }, JsonUtil.json());

        /* Changing Speaker */
        get(PREFIX + "/list/:id/changeSpeaker", (request, response) -> {
            response.type("application/json");
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    SpeakerList l = lists.get(id);
                    if(request.queryParams("uid") != null) {
                        if(request.queryParams("gender") != null) {

                            boolean found = false;
                            for(Speaker s : l.getKnownSpeakers()) {
                                if(s.getUid().equals(request.queryParams("uid"))) {
                                    switch (request.queryParams("gender")) {
                                        case "male":
                                            s.setSex(Gender.male);
                                            found = true;
                                            break;
                                        case "female":
                                            s.setSex(Gender.female);
                                            found = true;
                                            break;
                                        case "mixed":
                                            found = true;
                                            s.setSex(Gender.mixed);
                                            break;
                                        default:
                                            response.status(400);
                                            return new APIError(400, "Gender has to be male, female or mixed. " + request.queryParams("gender") + " is not allowed here.");

                                    }
                                }
                            }

                            if(found) {
                                return l;
                            } else {
                                response.status(400);
                                return new APIError(400, "No Speaker found with ID " + request.queryParams("uid"));
                            }
                        } else {
                            response.status(400);
                            return new APIError(400, "You have to specify a new gender");
                        }

                    } else {
                        response.status(400);
                        return new APIError(400, "You have to specify an id");
                    }
                } else {
                    response.status(400);
                    return new APIError(404, "No list with id " + id);
                }
            }

            response.status(400);
            return new APIError(400, "No list id given");

        }, JsonUtil.json());
    }

}
