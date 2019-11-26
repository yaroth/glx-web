[#--<!DOCTYPE html>--]
[#--<html>--]
[#--<head>--]
[#--    [#assign title = content.title!]--]
[#--    <title>${title}</title>--]

[#--    [@cms.area name="header"/]--]

[#--    [@cms.page /]--]
[#--</head>--]
[#--<body>--]
[#--    [@cms.area name="nav"/]--]
[#--    [@cms.area name="main"/]--]
[#--    [@cms.area name="footer"/]--]
[#--    <div id="modalwindow"></div>--]
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
        <input type="text" name="time" placeholder="hh:mm" ><br>
        Von:<br>
        <input type="text" name="from" ><br>
        Nach:<br>
        <input type="text" name="to" ><br>
        <input type="submit" value="Submit">
    </form><br>

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
<script src="../Javascript/zugservice.js"></script>
</body>
</html>