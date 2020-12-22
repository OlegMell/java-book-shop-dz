const API_URL = "http://localhost:3000/api/";


let token;
let header;
$(document).ready(() => {
    token = $("meta[name='_csrf']").attr("content");
    header = $("meta[name='_csrf_header']").attr("content");
});