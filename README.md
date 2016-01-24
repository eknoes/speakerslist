# Speakerslist
Speakerslist is a project, to simplify meetings by a digital speakerslist. It is divided into two components, on the one hand the public API and on the other hand the flexible GUI.

## API Specification
Speakerslist provides a simple API.

### Objects
* __SpeakerList__
    *String* __id__: Unique identifier of a list
    *Array<Speaker>* __knownSpeakers__: Array of already added Speakers, e.g. for autocompletion
    *Array<Speaker>* __queue__: Speakers in the order they are allowed to speak
    *boolean* __sexBalanced__: if true, the speakers are tried to order balanced by gender. default *false*
    *boolean* __preferNewSpeaker__: if true, new speaker are preferred. default *false*

* __Speaker__
    *String* __uid__: Unique identifier of a Speaker
    *String* __name__: Name of the Speaker
    *String* __sex__: Gender of the speaker (male|female|mixed)
    *boolean* __hasSpoken__: if true, the speaker has already spoken to the current topic

### v0 Methods

#### Creating a list
* __/newList__
    **return** SpeakerList-Object
    Creates a new list. 
    
### List operations
List operations are called by /list/$id

* __(without operation)__
    Gets the list. 
    **return** SpeakerList Object with id = $id
    
* __/addSpeaker__
    Adds a speaker to the queue.
    **return** Updated SpeakerList Object

    _Parameter_ $name
    When called with name as parameter, a new Speaker is created and added to the queue. If a speaker with this name already exists, the existing Speaker is added to the queue.
    
    _Parameter_ $uid
    When called with uid as parameter, the Speaker with uid = $uid is added to the queue.

* __/changeSpeaker__
    Changes a speakers gender.
    **return** Updated SpeakerList Object

    _Parameters_ $uid, $gender
    Changes the Speaker with uid = $uid to the given gender. $gender = (male|female|mixed)
    