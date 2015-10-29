$(document).ready(function() {
    selsect(".select");
});

//function tabs(){
//    $(".tabs-box").eq($(".tabs-nav li.active").index()).show();
//    $(".tabs-nav li").click(function(){
//        var index = $(this).index();
//        $(this).addClass("active").siblings().removeClass("active");
//        $(".tabs-box").hide().eq(index).show();
//    });
//}

//Ä£Äâselsect±êÇ©
function selsect(cls){
    $selsect = $(cls);
    $selsect.find(".default").click(function(){
        $selsect.find("ul").hide();
        $(this).next().show();
    });

    $selsect.find("li").click(function(){
        $(this).parent().hide().prev().text($(this).text());
    });

    $(document).bind("click",function(e){
        var target = $(e.target);
        if(target.closest(cls).length == 0){
            $selsect.find("ul").hide();
        }
    })
}