<!DOCTYPE html>
<html>
<head>
    [#assign title = content.title!]
    <title>${title}</title>

    [@cms.area name="header"/]

    [@cms.page /]
</head>
<body>
    [@cms.area name="nav"/]
    [@cms.area name="main"/]
    [@cms.area name="footer"/]
    <div id="modalwindow"></div>
    [#--TODO: load emo script--]
</body>
</html>