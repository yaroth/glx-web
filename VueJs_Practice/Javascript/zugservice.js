
var test = new Vue({
    el: '#app',
    data () {
        return {
            info: null
        }
    },
    mounted () {
        var uName = 'superuser';
        var pWord = 'superuser';
        axios
            .get('http://localhost:8080/.rest/demo/v1/getalltest', {
                headers: {
                    'Access-Control-Allow-Origin': '*'
                },
                auth: {
                    username: uName,
                    password: pWord
                }
            })
            .then(response => (this.info = response.data,
            console.log(response)))
            .catch(error => console.log(error))
    }
})

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