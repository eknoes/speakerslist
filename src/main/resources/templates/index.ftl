<#-- @ftlvariable name="listID" type="String" -->
<html>
    <head>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/jquery.ui/1.11.4/jquery-ui.min.css">
        <link rel="stylesheet" href="/css/main.css" />
    </head>
    <body>
        <main class="container row">
            <section class="s8 m6 offset-s2 offset-m3 col">
                <h1>Rednerliste</h1>
                <div id="new-speaker" class="row valign-wrapper">
                    <div class="input-field col s10">
                        <input id="new-speaker-input" type="text">
                        <label for="new-speaker-input">Neuer Redner</label>
                    </div>
                    <div class="col s2">
                        <a class="waves-effect waves-light btn" onclick="addSpeaker()">Hinzuf&uuml;gen</a>
                    </div>
                </div>
                <div id="speakers" class="row">
                </div>
                <div class="row footer">
                    <fieldset class="s12 col preferences">
                        <legend>Einstellungen</legend>
                        <div class="switch s6 col left-align">
                            <label><input id="sexBalanced" type="checkbox">Geschlechterausgeglichenheit
                                <span class="lever"></span>
                            </label>
                        </div>
                        <div class="switch s6 col right-align">
                            <label><input id="preferNewSpeaker" type="checkbox">Erstrederecht
                                <span class="lever"></span>
                            </label>
                        </div>
                    </fieldset>
                </div>

            </section>

        </main>

        <script src="https://cdn.jsdelivr.net/jquery/3.0.0-beta1/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/jquery.ui/1.11.4/jquery-ui.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js"></script>
        <script src="/js/controller.js"></script>
        <script>
              var LIST_ID;
              <#if listID??>
                LIST_ID = "${listID}";
              </#if>
              $(function() {
                  var speakersList = $( "#speakers" );
                  speakersList.sortable();
                  speakersList.disableSelection();
            });
        </script>
    </body>
</html>
