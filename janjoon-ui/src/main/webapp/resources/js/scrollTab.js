$(function(){

    $(".ui-layout-content").scroll(function(){
        $(".ui-layout-content").scrollTop($(this).scrollTop());    
    });

});

$(function(){

    $(".pe-layout-pane-content").scroll(function(){
        $(".pe-layout-pane-content").scrollTop($(this).scrollTop());    
    });

});

$(function(){
    $(".wmd-view-topscroll").scroll(function(){
        $(".ui-tree")
            .scrollLeft($(".wmd-view-topscroll").scrollLeft());
    });    
});

