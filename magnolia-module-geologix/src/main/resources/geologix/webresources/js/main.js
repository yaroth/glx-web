var boxOpened = false;

// Mobile Safari in standalone mode
if (("standalone" in window.navigator) && window.navigator.standalone) {

    // If you want to prevent remote links in standalone web apps opening Mobile Safari, change 'remotes' to true
    var noddy, remotes = false;

    document.addEventListener('click', function (event) {

        noddy = event.target;

        // Bubble up until we hit link or top HTML element. Warning: BODY element is not compulsory so better to stop on HTML
        while (noddy.nodeName !== "A" && noddy.nodeName !== "HTML") {
            noddy = noddy.parentNode;
        }

        if ('href' in noddy && noddy.href.indexOf('http') !== -1 && (noddy.href.indexOf(document.location.host) !== -1 || remotes)) {
            event.preventDefault();
            document.location.href = noddy.href;
        }

    }, false);
}

var isMobile = false;
if (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
    var isMobile = true;
}

$(document).ready(function () {
    //initialize swiper when document ready
    var swiper2 = new Swiper('.swiper-p', {
        pagination: '.swiper-p-pagination',
        paginationClickable: true,
        autoplay: 15000,
        speed: 1500
    });

    var swiper3 = new Swiper('.swiper-h', {
        pagination: '.swiper-h-pagination',
        paginationClickable: true,
        autoplay: 4000,
        speed: 1500
    });

    $("p:has(img)").css("padding-right", "0");

    $("#navmenu").height($("#container").height());
    // Off-Canvas Navigation
    $("#navbar-menu").click(function () {
        if ($(this).hasClass("active")) {

            $("#navmenu").animate({"right": "-350px"}, 250);
            $("#main-content").animate({"left": "0px"}, 250);
            $(this).find(".close").hide();
            $(this).find(".menu").show();
        } else {

            $("#navmenu").animate({"right": "0px"}, 250);
            $("#main-content").animate({"left": "-350px"}, 250);
            $(this).find(".menu").hide();
            $(this).find(".close").show();
        }
        $(this).toggleClass("active");

    });

    // Startsite
    if (!isMobile) {
        $(".hoverbox").hover(function () {
            $(this).children(".hover").stop().fadeIn();
        }, function () {
            $(this).children(".hover").stop().fadeOut();
        });
    } else {
        $(".hoverbox").children(".hover").show();
    }


    // Hover function tables
    hoverTables();

    // Build Flowgrid
    $(".row-grid").hide();
    $(".box.bgbox").each(function (index, element) {
        var bg = $(this).children(".bg");
        if (bg.length) {
            $(this).css("background-image", bg.css("background-image"));
            $(this).css("color", "#fff");
        }
    });
    buildFlowgrid();


    // Youtubelinks

    $('.youtube').click(function (ev) {
        $('#modalwindow').hide();
        var youtubeid = $(this).attr('id');
        var ytlink = "<div class='video-player'><div class='videocontent'><iframe id='ytvideo' src='https://www.youtube.com/embed/" + youtubeid + "?rel=0&feature=oembed' frameborder='0' allowfullscreen></iframe></div></div>";
        $('#modalwindow').html(ytlink);
        //$('iframe').left(299);
        $('#modalwindow').show();
        $('#modalwindow').css("display", "table");

        ev.preventDefault();

        return false;
    });

    $('#modalwindow').click(function (ev) {
        var url = $('#ytvideo').attr('src');
        $('#ytvideo').attr('src', '');
        $('#ytvideo').attr('src', url);

        $(this).hide();
    });

    $(document).keydown(function (e) {
        if (e.keyCode == 27) {
            $("#modalwindow").hide();
        }
    });


    // About
    var $images = $(".fotos div");
    $images.hide();
    // Zuf�lliges Bild anzeigen
    var ix = Math.floor(Math.random() * $images.size());
    $images.eq(ix).show();
    setInterval(function () {
        changeImage();
    }, 5000);
    // Funktion zum wechseln der Bilder
    function changeImage() {
        // aktuelles sichtbares Bild nehmen
        var $currImg = $(".fotos div:visible");
        if ($currImg.next().attr("id") <= $images.size() - 1) {
            var $next = $images.eq(parseInt($currImg.attr("id")) + 1);
        } else {
            var $next = $images.eq(0);
        }
        $currImg.fadeOut(1000);
        $next.fadeIn(1000);
    }

    // Team
    $(".emp-box").hover(function () {
            $(this).children(".emp-details").stop().fadeIn();
        },
        function () {
            $(this).children(".emp-details").stop().fadeOut();
        });

    // Referenzen
    $(".reflookup").on("change", function () {
        var ref = $(this);
        $("#filterresult").load("?filter=" + $(this).val(), "", function () {
            $("#filterresult p:has(img)").css("padding-right", "0");
            $(".reflookup").not(ref).val(-1);
            hoverTables();
        });
    });


    var hash = window.location.hash;

    $(".flowbutton").each(function (index, element) {
        $(this).click(function (event) {
            event.preventDefault();
            showDetails($(this));
        });
        // Open Tab when hash is set
        if ((hash) && ($(this)[0].hash == hash)) {
            showDetails($(this));
        }
    });

    // Inpage Nav
    if (hash == "#inpagenav") {
        if ($(".navinpage").is(":visible")) {
            var st = $(".navinpage").offset().top;
        } else {
            var tmp = $("#navinpage-mobile").parents(".row");
            var st = tmp.offset().top + tmp.height();
        }
        $("body, html").animate({"scrollTop": st}, 400);
    }


    $(".box-flow-content .close").each(function (index, element) {
        $(this).click(function (event) {
            event.preventDefault();
            // Hide other contents
            $(this).parent().parent().hide();
            $(".box").removeClass("active");
            $(".flowbutton").show();
            $(".box.bgbox").each(function (index, element) {
                var bg = $(this).children(".bg");
                if (bg.length) {
                    $(this).css("background-image", bg.css("background-image"));
                    $(this).css("color", "#fff");
                }
            });
        });
    });


    var boxclasses = ["color-box1", "color-box2", "color-box3", "color-box4"];
    var lastclass = "";
    var usedclasses = [];
    var colorbox = 1;
    var i = 0;
    var tmpbox = "";
    $(".colorbox").each(function (index, element) {
        // remove last color used
        var lastindex = boxclasses.indexOf(lastclass);
        if (lastindex > -1) {
            boxclasses.splice(lastindex, 1);
        }

        if (i > 3) {
            var lastindex = boxclasses.indexOf(usedclasses[i - 4]);
            if (lastindex > -1) {
                tmpbox = usedclasses[i - 4];
                boxclasses.splice(lastindex, 1);
            }
        }

        var ix = Math.floor((Math.random() * boxclasses.length));
        $(this).addClass(boxclasses[ix]);
        if (lastclass != "") {
            boxclasses.push(lastclass);
        }
        if (tmpbox != "") {
            boxclasses.push(tmpbox);
            tmpbox = "";
        }
        lastclass = boxclasses[ix];
        // save used classes
        usedclasses.push(boxclasses[ix]);
        i++;
        if ($(this).hasClass("box-2")) {
            usedclasses.push(boxclasses[ix]);
            i++;
        }


    });


    $("nav").click(function () {
        $("body, html").animate({"scrollTop": "0"}, 800)
    });

    // Navigationbar
    var didScroll;
    var lastScrollTop = 0;
    var delta = 15;
    var navbarHeight = $("nav").outerHeight();


    $(window).scroll(function (event) {
        var st = $(this).scrollTop();

        // Main Navigation
        if (!boxOpened) {
            if (Math.abs(lastScrollTop - st) > delta) {
                if (st > lastScrollTop && st > navbarHeight) {
                    // Scroll Down
                    $("nav").removeClass("active");
                    $("nav").stop().animate({top: "-60px"}, 100);
                    $(".fixed").stop().animate({top: "0px"}, 100);
                } else {
                    // Scroll Up
                    if (st + $(window).height() < $(document).height()) {
                        $("nav").addClass("active");
                        $("nav").stop().animate({top: "0px"}, 100);
                        $(".fixed").stop().animate({top: "60px"}, 100);
                    }
                }
                lastScrollTop = st;
            }
        } else {
            boxOpened = false;
            lastScrollTop = st;
        }


        // Inpage Navigation

        $(".sticked:visible").each(function () {
            var prev_row = $(this).parents(".row");
            var next_row = prev_row.next(".row");
            if (!next_row.length) {
                next_row = prev_row.next(".flowgrid").children(".row:first-child");
            }
            var current_col_height = $(this).height();
            if ($(this).hasClass("fixed")) {
                if (st < prev_row.position().top + 95 - $(this).height()) {
                    $(this).removeClass("fixed");
                    prev_row.css("margin-bottom", "0");
                    $(this).stop().css("top", 0);
                }
            } else {
                if ((next_row.length) && (st >= next_row.position().top - current_col_height + 80)) {
                    $(this).addClass("fixed");
                    prev_row.css("margin-bottom", current_col_height);
                }
            }
        });


    });

    if ($("#container").width() > 1023)
        fixHeightBox();


    $(window).resize(function () {
        buildFlowgrid();
        $("#navmenu").height($("#container").height());
        if ($("#container").width() > 1023)
            fixHeightBox();

    });


});


function buildFlowgrid() {
    var pagewidth = $("#main-content").width();
    var currentwidth = pagewidth;
    var boxes = $(".flowgrid .box");
    var ixstart = 0;
    for (var i = 0; i < boxes.length; i++) {
        currentwidth = currentwidth - $(boxes[i]).width();
        if (currentwidth == 0) {
            boxes.slice(ixstart, i + 1).wrapAll("<div class='row'></div>");
            ixstart = i + 1;
            currentwidth = pagewidth;
        }
    }
    ;
}

function showDetails(linkbutton) {

    var currentColorBox = linkbutton.parent().parent();

    // show all link buttons
    $(".flowbutton").show();
    // hide current link button
    linkbutton.hide();
    // toggle active
    $(".bgbox.active").css("background-image", $(".bgbox.active").children(".bg").css("background-image"));
    $(".box.active").removeClass("active");
    currentColorBox.addClass("active");
    currentColorBox.css("background-image", $(currentColorBox).children(".bg.active").css("background-image"));
    // Show current content
    var content = linkbutton.attr("href");
    $(content).insertAfter($(currentColorBox).parent(".row"));
    $(content + " .arrow-down").css({"left": currentColorBox.position().left});

    $(".row-grid:visible").hide();
    $(content).show();

    var toScroll = currentColorBox.offset().top;
    if ($("#navinpage").is(':visible')) {
        toScroll = toScroll - $("#navinpage").height();
    }
    if (toScroll != $(window).scrollTop()) {
        setTimeout(function () {
            $('html, body').animate({scrollTop: toScroll}, 0, function () {
                $("nav").removeClass("active");
                $("nav").css({"top": "-60px"});
                boxOpened = true;
            });
        }, 1);
    }
}

function hoverTables() {
    $('table tr').not('table thead tr').hover(function () {
        $(this).addClass('hover');
    }, function () {
        $(this).removeClass('hover');
    });
}

function fixHeightBox() {
    var maxHeight = 0;
    $(".sameheight").children().each(function () {
        if ($(this).height() > maxHeight) {
            maxHeight = $(this).height();
        }
        //console.log($(this).height());
    });
    $(".sameheight").children().height(maxHeight);

};




