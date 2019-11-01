[#assign newsRoot = cmsfn.contentByPath("/", "news")]
[#assign newsList = cmsfn.children(newsRoot, "news")]
[#assign newsListSorted = newsList?sort_by("date")?reverse]

<div class="row">
[#list newsListSorted as newsItem ]
    <div class="col-1 box colorbox color-box1">
        <div class="box-content">
            <span class="title">NEWS — ${newsItem.date?string("dd.MM.yyyy")}</span>
            <span class="headline">${cmsfn.decode(newsItem).title!}</span>
            <a class="morelink flowbutton" href="#auch-neuenegg-ist-mit-von-der-partie">mehr erfahren</a>
        </div>
    </div>
[/#list]
</div>