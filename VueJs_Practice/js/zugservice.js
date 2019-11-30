var app = new Vue({
    el: '#app',
    data: {
        info: 'Some bloody test',
        time: '09:30',
        from: 'Bern',
        to: 'Thun'
    },
    // mounted() {
    //     axios
    //         .post('http://localhost:8080/.rest/demo/v1/zugservices', {time:this.time, from:this.from, to:this.to})
    //         .then(response => (this.info = response.data))
    //         .catch(error => console.log(error))
    // }
});

var blog_list = new Vue({
    el: '#js-grid-list',
    data: {
        layout: 'list',
        zugserviceId: '',
        ok: true,
        zugservices: [{
            title: 'Zürich - Interlaken',
            uuid: '1234-5678',
            waggons: [{
                number: 1,
                image: '../images/sitzplan.jpg',
                seats: [{
                    number: 22,
                    reserved: 'yes'
                }, {
                    number: 33,
                    reserved: 'no'
                }]
            }, {
                number: 2,
                image: '../images/sitzplan.jpg',
                seats: [{
                    number: 44,
                    reserved: 'yes'
                }, {
                    number: 55,
                    reserved: 'no'
                }]
            }]
        }, {
            title: 'Basel - Interlaken',
            uuid: 'ab12-cd99',
            waggons: [{
                number: 44,
                image: '../images/sitzplan2.png',
                seats: [{
                    number: 1,
                    reserved: 'yes'
                }, {
                    number: 2,
                    reserved: 'no'
                }]
            }, {
                number: 45,
                image: '../images/sitzplan2.png',
                seats: [{
                    number: 3,
                    reserved: 'yes'
                }, {
                    number: 4,
                    reserved: 'no'
                }]
            }]
        }]
    },
    methods: {
        showZugserviceDetail(uuid) {
            this.layout = 'zugservice-detail';
            this.zugserviceId = uuid;
            console.log("Zugservicedetails line clicked, uuid: " + uuid);
        },
        showSeatDetail() {
            console.log("showSeatDetail line clicked");
        }

    }
});