[#--[#assign title = content.title!"Dummy page (created by maven archetype)"]--]
[#--<!DOCTYPE html>--]
[#--<html>--]
[#--<head>--]
[#--    <title>${title}</title>--]

[#--    <link rel="stylesheet" href="${ctx.contextPath}/.resources/geologix/webresources/css/style.css">--]

[#--[@cms.page /]--]
[#--</head>--]
[#--<body>--]
[#--<div class="container">--]
[#--    <header>--]
[#--        <h1>${title}</h1>--]
[#--    </header>--]

[#--    <div class="main">--]
[#--    [@cms.area name="main"/]--]
[#--    </div>--]

[#--</div>--]
[#--</body>--]
[#--</html>--]

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

<div id="home">
    <form>
        Zeit:<br>
        <input type="text" name="time" placeholder="hh:mm"><br>
        Von:<br>
        <input type="text" name="from"><br>
        Nach:<br>
        <input type="text" name="to"><br>
        <input type="submit" value="Submit">
    </form>
    <br>

    <br>{{ intro }} <br>

    <div>
        {{ trainServices }}
    </div>

    <div id="app">
        {{ info }}
    </div>

</div>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="${ctx.contextPath}/.resources/geologix/webresources/js/zugservice.js"></script>
</body>
</html>
