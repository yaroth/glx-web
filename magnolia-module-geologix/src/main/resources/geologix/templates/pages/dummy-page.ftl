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
</div>

<section id="js-grid-list" class="grid-list" v-cloak>

    <div class="tool-bar">
        <!-- These link buttons use Vue.js to bind click events to change the "layout" variable and bind an active class -->
        <a class="list-icon" v-on:click="layout = 'list'" v-bind:class="{ 'active': layout == 'list'}"
           title="List"></a>
        <a class="grid-icon" v-on:click="layout = 'grid'" v-bind:class="{ 'active': layout == 'grid'}"
           title="Grid"></a>
    </div>

    <!-- Vue.js lets us choose which UL to show depending on the "layout" variable -->

    <ul v-if="layout === 'grid'" class="grid">
        <!-- A "grid" view with photos only -->
        <li v-for="blog in blog_posts">
            <a v-bind:href="blog.url" v-bind:style="{ backgroundImage: 'url(' + blog.image.large + ')' }"
               target="_blank"></a>
        </li>
    </ul>

    <ul v-if="layout === 'list'" class="list">
        <!-- A "list" view with small photos and blog titles -->
        <li v-for="blog in blog_posts">
            <a v-bind:href="blog.url" target="_blank">
                <img v-bind:src="blog.image.small">
                <p>{{blog.title}}</p>
            </a>
        </li>
    </ul>
</section>
<div id="app">
    <div v-for="zugservice in info" class="currency">
        {{ zugservice.uuid }}<br>
        {{ zugservice.name }}<br>
    </div>
    <div>{{ info }}</div>
    <input v-on:click="getZugservices" type="submit" value="Submit">
</div>

</div>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<#--<script src="https://cdnjs.cloudflare.com/ajax/libs/vue/1.0.27/vue.min.js"></script>-->
<script src="${ctx.contextPath}/.resources/geologix/webresources/js/zugservice.js"></script>
<link rel="stylesheet" href="${ctx.contextPath}/.resources/geologix/webresources/css/listview.css">
</body>
</html>

