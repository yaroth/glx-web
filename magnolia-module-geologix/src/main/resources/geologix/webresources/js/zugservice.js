var app = new Vue({
    el: '#app',
    data() {
        return {
            info: 'Some bloody test',
        }
    },
    data: {
        info: 'Some bloody test',
        time: '09:30',
        from: 'Bern',
        to: 'Thun'
    },
    mounted() {
        axios.post('http://localhost:8080/.rest/demo/v1/zugservices', {time:this.time, from:this.from, to:this.to})
            .then(response => (this.info = response.data))
            .catch(error => console.log(error))
    }
});

var blog_list = new Vue({
    el: '#js-grid-list',
    data: {
        // The layout mode, possible values are "grid" or "list".
        layout: 'list',

        // demo data
        blog_posts: [{
            title: 'Tapping into UGC with Offerpop',
            url: 'https://voltagead.com/tapping-ugc-offerpop/',
            image: {
                'large': 'https://2e64oz2sjk733hqp882l9xbo-wpengine.netdna-ssl.com/wp-content/uploads/2016/08/header-960x500-copy-960x500.jpg',
                'small': 'https://2e64oz2sjk733hqp882l9xbo-wpengine.netdna-ssl.com/wp-content/uploads/2016/08/header-960x500-copy-300x300.jpg'
            }
        }, {
            title: '5 websites that get design right',
            url: 'https://voltagead.com/5-websites-get-design-right/',
            image: {
                'large': 'https://2e64oz2sjk733hqp882l9xbo-wpengine.netdna-ssl.com/wp-content/uploads/2016/08/HERO-960x500.jpg',
                'small': 'https://2e64oz2sjk733hqp882l9xbo-wpengine.netdna-ssl.com/wp-content/uploads/2016/08/HERO-300x300.jpg'
            }
        }]
    }
});