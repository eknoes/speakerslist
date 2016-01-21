import model.APIError;
import model.Speaker;
import model.SpeakerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Created by soenke on 21.01.16.
 */
public class ListController {
    public static final int VERSION = 0;
    public static final String PREFIX = "/v" + VERSION;
    public Map<String, SpeakerList> lists = new HashMap<>();

    public ListController() {


        /* Getting and Creating Lists */
        get(PREFIX + "/getList/:id", (request, response) -> {
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    return lists.get(id);
                } else {
                    return new APIError(404, "No list with id " + id);
                }
            }

            return new APIError(400, "No list id given");
        }, JsonUtil.json());

        get(PREFIX + "/newList", (request, response) -> {
            SpeakerList newList = new SpeakerList();
            lists.put(newList.getId(), newList);
            return newList;
        }, JsonUtil.json());

        /* Adding new Speaker */
        get(PREFIX + "/lists/:id/addSpeaker", (request, response) -> {
            String id = request.params(":id");
            if(id != null) {
                if(lists.get(id) != null) {
                    SpeakerList l = lists.get(id);
                    if(request.queryParams("uid") != null) {
                        if(addSpeaker(l, request.queryParams("uid"))) {
                            return l;
                        } else {
                            return new APIError(400, "Either the uid is invalid or the speaker is already on the list");
                        }
                    } else if(request.queryParams("name") != null) {
                        Speaker s = new Speaker(request.queryParams("name"), null);
                        if(addSpeaker(l, s)) {
                            return l;
                        } else {
                            return new APIError(400, "There is already a speaker with this id on the list");
                        }
                    } else {
                        return new APIError(400, "You have to specify an id or name");
                    }
                } else {
                    return new APIError(404, "No list with id " + id);
                }
            }

            return new APIError(400, "No list id given");

        }, JsonUtil.json());

    }

    public boolean addSpeaker(SpeakerList list, String uid) {
        for(Speaker s : list.getKnownSpeakers()) {
            if(s.getUid().equals(uid)) {
                return addSpeaker(list, s);
            }
        }
        return false;
    }

    public boolean addSpeaker(SpeakerList list, Speaker speaker) {
        if(list.getSpeakers().contains(speaker)) {
            return false;
        } else {
            list.getSpeakers().add(speaker);
            if(!list.getKnownSpeakers().contains(speaker)) {
                list.getKnownSpeakers().add(speaker);
            }
            return true;
        }
    }
}
