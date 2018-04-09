function checker(){
    var values = [];

    $('input').each(function(){
        values.push($(this).val());
    });

    $('textarea').each(function(){
        values.push($(this).val());
    });

    var sqlSymbols = ["--", ";", "`", "'", "DROP DATABASE", "DROP TABLE"];

    var flag = true;

    values.map(function (x){
        sqlSymbols.map(function(y){
            if(x.indexOf(y) > -1){
                flag =  false;
            }
        })
    });

    if(!flag) {
        $('form').append("<div id=\"message\" class=\"notification pos-right pos-top col-sm-6 col-md-4 col-xs-12 growl-notice notification--reveal\" data-animation=\"from-right\" data-notification-link=\"growl\" style=\"top: 40px;\"><div class=\"boxed boxed--sm bg--dark\"><span>Невалидни символи!</span></div><div class=\"notification-close-cross notification-close\"></div></div>");
    }

    return flag;
}

function initMap() {
    var kostinbrod = {lat: 42.818459, lng: 23.211960};

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 10,
        center: kostinbrod
    });

    var marker = new google.maps.Marker({
        position: kostinbrod,
        map: map
    });
}