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
            .then(response => (this.info = response))
            .catch(error => console.log(error))
    }
});