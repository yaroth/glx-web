new Vue({
    el: '#app',
    data() {
        return {
            info: 'Some bloody test',
            time: '09:30',
            from: 'Bern',
            to: 'Thun'
        }
    },
    mounted() {
        axios
            .post('http://localhost:8080/.rest/demo/v1/zugservices', {time:this.time, from:this.from, to:this.to})
            // .get('http://localhost:8080/.rest/demo/v1/getalltest')
            .then(response => (this.info = response))
            .catch(error => console.log(error))
    }
});

// var home = new Vue({
//     el: '#home',
//     data(){
//             return {
//                 intro: 'Your trains..',
//                 trainServices: null
//             }
//     },
//
//     getTrains() {
//         axios
//             .get('Access-Control-Allow-Origin: http://localhost:8080/.rest/demo/v1/getalltest')
//             .then(response => (this.trainServices = response,
//                 console.log(response)))
//     }
// })