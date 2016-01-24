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
            var sUID = speaker.attr("id").substr(8, speaker.attr("id").length);
            changeSpeakerSex(obj.parents(".speaker"), newSex);
            $.ajax(API_URL + "/lists/" + LIST_ID + "/changeSpeaker?uid=" + encodeURIComponent(sUID) + "&gender=" + newSex).done(function(updatedList) {
                updatedList(updatedList);
            }).fail(printError);


        }
    });
}

/**
 * Creates a new Speaker DOM Element from JSON Object
 * @param speakerObj Speaker JSON Object
 */
function createSpeakerObj(speakerObj) {
    var newObj = $($.parseHTML('<div id="speaker-' + speakerObj.uid + '" class="card-panel light-blue row valign-wrapper dismissable speaker"> \
        <span class="white-text s10 col">' + speakerObj.name + '</span> \
        <div class="fixed-action-btn horizontal" style="bottom: 0; right: 5px; margin: 0; padding: 0; position: relative"> \
        <a class="btn-floating btn-large orange male"></a> \
        <ul> \
        <li><a class="btn-floating orange female" onclick="switchToSex(this)"></a></li> \
        <li><a class="btn-floating orange mixed" onclick="switchToSex(this)"></a></li> \
        </ul> \
        </div> \
        <a class="btn-floating btn-large waves-effect waves-light red darken-4">X</a> \
        </div>'));
    changeSpeakerSex(newObj, speakerObj.sex);
    $("#speakers").append(newObj);
}

var API_URL = "/v0";

/**
 * Initializes the list
 */
function initializeList() {
    $.ajax(API_URL + "/getList/" + LIST_ID).done(function(listObj) {
        listObj.speakers.forEach(function(speakerObj) {
            createSpeakerObj(speakerObj);
        });
    }).fail(printError);
}

function updateList(listObj) {
    var speakers = listObj.speakers;
    speakers.forEach(function(speaker) {
        var spk = $("#speaker-" + speaker.uid);
        if(spk.length == 0) {
            createSpeakerObj(speaker);
        }
    });
}

$(document).ready(function() {
    if(LIST_ID === undefined) {
        $.ajax(API_URL + "/newList").done(function(newList) {
            LIST_ID = newList.id;
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
    $.ajax(API_URL + "/lists/" + LIST_ID + "/addSpeaker?name=" + encodeURIComponent($("#new-speaker-input")[0].value)).done(function(updatedList) {
        $("#new-speaker-input").value = "";
        updateList(updatedList);
    }).fail(printError);
}