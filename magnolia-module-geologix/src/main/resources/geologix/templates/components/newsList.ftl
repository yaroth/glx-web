
[#assign newsRoot = cmsfn.contentByPath("/", "news")]
[#assign newsList = cmsfn.children(newsRoot, "news")]
[#assign newsListSorted = newsList?sort_by("date")?reverse]

<div class="row">
[#--<div class="col-2 box box-2 bg-white end-2">
    <div class="box-content">
        <span class="title">NEWS — 05.06.18</span>
        <span class="headline">Nidwalden setzt auch auf Logo</span>
        <a class="morelink flowbutton" href="#nidwalden-setzt-auch-auf-logo">mehr erfahren</a>
    </div>
</div>--]
[#list newsListSorted as newsItem ]
[#--[#list newsList as newsItem]--]
    <div class="col-1 box colorbox color-box1">
        <div class="box-content">
            <span class="title">NEWS — ${newsItem.date?string("dd.MM.yyyy")}</span>
            <span class="headline">${cmsfn.decode(newsItem).title!}</span>
            <a class="morelink flowbutton" href="#auch-neuenegg-ist-mit-von-der-partie">mehr erfahren</a>
        </div>
    </div>
[/#list]
[#--<div class="col-1 box colorbox color-box2">
    <div class="box-content">
        <span class="title">NEWS — 12.02.18</span>
        <span class="headline">SmartTraffic-Upgrade für den Kanton Schaffhausen</span>
        <a class="morelink flowbutton" href="#smarttraffic-upgrade-fur-den-kanton-schaffhausen">mehr erfahren</a>
    </div>
</div>--]
</div>