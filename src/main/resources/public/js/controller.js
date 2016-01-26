/**
 * Prints the Errormessage
 * @param msg Errormessage
 */
function printError(msg) {
    if(typeof msg === "object") {
        msg = msg.responseJSON.error + ": " + msg.responseJSON.message;
    }

    Materialize.toast(msg, 3000, 'rounded');
}

/**
 * Changes the sex of an Speaker in the DOM
 * @param speaker jQuery Object of the Speaker
 * @param sex The new sex
 */
function changeSpeakerSex(speaker, sex) {
    var oldButton = speaker.find("." + String(sex));
    oldButton.removeClass(sex);

    var sexMainButton = speaker.find(".fixed-action-btn > .btn-large");

    sexMainButton.attr("class").split(" ").forEach(function(oldSex) {
       if(oldSex == "male" || oldSex == "female" || oldSex == "mixed") {
           oldButton.addClass(oldSex);
           sexMainButton.removeClass(oldSex)
       }
    });

    sexMainButton.addClass(sex);

}

/**
 * Switches the gender to the clicked one
 * @param obj
 */
function switchToSex(obj) {
    obj = $("body").find(obj);
    obj.attr("class").split(" ").forEach(function(newSex) {
        if(newSex == "male" || newSex == "female" || newSex == "mixed") {
            var speaker = obj.parents(".speaker");
            var sUID = getUidFromID(speaker.attr("id"));
            changeSpeakerSex(obj.parents(".speaker"), newSex);
            $.ajax(API_URL + "/list/" + LIST_ID + "/changeSpeaker?uid=" + encodeURIComponent(sUID) + "&gender=" + newSex).done(function(updatedList) {
                updateList(updatedList);
            }).fail(printError);


        }
    });
}

/**
 * Creates a new Speaker DOM Element from JSON Object
 * @param speakerObj Speaker JSON Object
 * @param noAppend Boolean, if object should be appended or returned
 */
function createSpeakerObj(speakerObj, noAppend) {
    noAppend = !!noAppend;
    var newObj = $($.parseHTML('<div id="speaker-' + speakerObj.uid + '" class="card-panel light-blue row valign-wrapper dismissable speaker"> \
        <span class="white-text s10 col">' + speakerObj.name + '</span> \
        <div class="fixed-action-btn horizontal" style="bottom: 0; right: 5px; margin: 0; padding: 0; position: relative"> \
        <a class="btn-floating btn-large orange male"></a> \
        <ul> \
        <li><a class="btn-floating orange female" onclick="switchToSex(this)"></a></li> \
        <li><a class="btn-floating orange mixed" onclick="switchToSex(this)"></a></li> \
        </ul> \
        </div> \
        <a class="btn-floating btn-large waves-effect waves-light red darken-4" onclick="removeSpeakerSelf(this)">X</a> \
        </div>'));
    changeSpeakerSex(newObj, speakerObj.sex);

    if(noAppend) {
        return newObj;
    }
    $("#speakers").append(newObj);
}

var API_URL = "/v0";

/**
 * Initializes the list
 */
function initializeList() {
    $.ajax(API_URL + "/list/" + LIST_ID).done(function(listObj) {
        updateList(listObj);
    }).fail(printError);
}

function updateList(listObj) {
    console.log("update");
    var speakers = listObj.queue;
    var speakersList = $("#speakers");
    for(var i = 1; i <= speakers.length; i++) {
        if(speakersList.find(".speaker:nth-child(" + i + ")").length > 0) {
            if(getUidFromID(speakersList.find(".speaker:nth-child(" + i + ")")[0].id) != speakers[i-1].uid) {
                $("#speakers").find(".speaker:nth-child(" + i + ")").replaceWith(createSpeakerObj(speakers[i - 1], true));
            }
        } else {
            createSpeakerObj(speakers[i-1], false);
        }
    }
    while(speakersList.find(".speaker:nth-child(" + (speakers.length + 1) + ")").length > 0) {
        $("#speakers").find(".speaker:nth-child(" + (speakers.length + 1) + ")").remove();
    }

    $("#sexBalanced")[0].checked = listObj.sexBalanced;
    $("#preferNewSpeaker")[0].checked = listObj.preferNewSpeaker;
}

$(document).ready(function() {
    if(LIST_ID === undefined) {
        $.ajax(API_URL + "/newList").done(function(newList) {
            LIST_ID = newList.id;
            var link = "https://speakerslist.herokuapp.com/" + LIST_ID;
            $("#list-link").attr("href", link);
            $("#list-link").text(link);
            initializeList();
        }).fail(printError);
    } else {
        initializeList();
    }
});

/**
 * Adds the new Speaker from text input to list
 */

function addSpeaker() {
    $.ajax(API_URL + "/list/" + LIST_ID + "/addSpeaker?name=" + encodeURIComponent($("#new-speaker-input")[0].value)).done(function(updatedList) {
        updateList(updatedList);
    }).fail(printError);
    $("#new-speaker-input")[0].value = "";
    return false;
}

function removeSpeakerSelf(obj) {
    var speaker = $("#speakers").find(obj).parents(".speaker");
    var sUID = getUidFromID(speaker.attr("id"));
    $.ajax(API_URL + "/list/" + LIST_ID + "/removeSpeaker?uid=" + encodeURIComponent(sUID)).done(function(updatedList) {
        updateList(updatedList);
    }).fail(printError);
}

function changePNS() {
    $.ajax(API_URL + "/list/" + LIST_ID + "/changeList?preferNewSpeaker=" + encodeURIComponent($("#preferNewSpeaker")[0].checked)).done(function(updatedList) {
        updateList(updatedList);
    }).fail(printError);
}

function changeSB() {
    $.ajax(API_URL + "/list/" + LIST_ID + "/changeList?sexBalanced=" + encodeURIComponent($("#sexBalanced")[0].checked)).done(function(updatedList) {
        updateList(updatedList);
    }).fail(printError);
}

function clearList() {
    $.ajax(API_URL + "/list/" + LIST_ID + "/clear").done(function(updatedList) {
        updateList(updatedList);
    }).fail(printError);
}

/* Helper Function */
function getUidFromID(cssID) {
    return cssID.substr(8, cssID.length);
}