<!DOCTYPE html>
<html>
<head>
[@cms.page /]
[#assign title = content.title!]
    <title>${title}</title>

[@cms.area name="header"/]

</head>
<body>
[@cms.area name="nav"/]
[@cms.area name="main"/]
[@cms.area name="footer"/]
<div id="modalwindow"></div>

[#--TODO: load emo script--]
<!-- emo -->
<script>
    var emo_addr = new Array();
    emo_addr[0] = "Km5OvNPC1zUHM4rBuyolcIdLX9t6Aq8shpkR3bWJaGeDwjg7f0Fi2V.+xETnZQYS";
    emo_addr[1] = "BPvhtCzb9R2k6dNG6Cy7rWbg9WQK9.I76PQJtLhgX.hkBR0G6dAhX.0pA+MQ1WIj6VQb6dNG65mjXdbw1kmiAWMQ1kQitLybH+yb6LmwXLybAFQiqCbw9LM7tdVp9.IiH.VptdfgA+9J1RxZH.bj9ixZH.vY";
    emo_addr[2] = "BPvhX.0pA+MQ1WIj6VQb6dNG651htCzb9R2k6dNG6Cy7rWbg9WQK9.I76PQJtLhgX.hkBWbg9WQK9.I76PQJtLhgX.hZH.vY";
    if (window.addLoadEvent) addLoadEvent(emo_replace());
</script>

</body>
</html>